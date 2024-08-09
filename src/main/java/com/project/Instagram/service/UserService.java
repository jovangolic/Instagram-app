package com.project.Instagram.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.project.Instagram.exception.UserAlreadyExistsException;
import com.project.Instagram.exception.ResourceNotFoundException;
import com.project.Instagram.exception.InternalServerException;
import com.project.Instagram.exception.NoSuchElementException;
import com.project.Instagram.model.Role;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.RoleRepository;
import com.project.Instagram.repository.UserRepository;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

	
private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	/*@Override
	public User registerUser(User user) {
		//da li postoji korisnik
		if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
	}*/

	@Override
	public User registerUser(User user) {
		logger.info("Registering user with email: {}", user.getEmail());
		// Da li postoji korisnik
		if (userRepository.existsByEmail(user.getEmail())){
            logger.warn("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        
        // Pronađi rolu ili baci izuzetak ako ne postoji
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> {
                logger.error("Role ROLE_USER not found");
                return new NoSuchElementException("Role not found");
            });
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.debug("Encoded password for user with email: {}", user.getEmail());
        user.setRoles(Collections.singletonList(userRole));
        
        User savedUser = userRepository.save(user);
        logger.info("User with email {} successfully registered", user.getEmail());
        return savedUser;
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(String email) {
		User theUser = getUser(email);
		if(theUser != null) {
			userRepository.deleteByEmail(email);
		}
	}

	@Override
	public User getUser(String email) {
		return userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public User updateUser(Long userId,String username, String email,byte[] photoBytes, String bio) {
		User existingUser = userRepository.findById(userId).orElseThrow(
				() -> new IllegalArgumentException("User not found " + userId));
		if(existingUser != null) {
			existingUser.setUsername(username);
			existingUser.setEmail(email);
			if(photoBytes != null && photoBytes.length > 0) {
				try {
					existingUser.setProfilePicture(new SerialBlob(photoBytes));
				}
				catch (SQLException ex) {
	                throw new InternalServerException("Fail updating user");
	            }
			}
			existingUser.setBio(bio);
		}
		return userRepository.save(existingUser);
	}

	@Override
	public List<User> searchUsersByUsername(String username) {
		return userRepository.findByUsernameContainingIgnoreCase(username);
	}

	@Override
	public void followUser(String followerEmail, String followeeEmail) {
		User follower = getUser(followerEmail);
        User followee = getUser(followeeEmail);
        if (!follower.getFollowing().contains(followee)) {
            follower.getFollowing().add(followee);
            followee.getFollowers().add(follower);
            userRepository.save(follower);
            userRepository.save(followee);
        }
        
	}

	@Override
	public void unfollowUser(String followerEmail, String followeeEmail) {
		User follower = getUser(followerEmail);
        User followee = getUser(followeeEmail);

        if (follower.getFollowing().contains(followee)) {
            follower.getFollowing().remove(followee);
            followee.getFollowers().remove(follower);
            userRepository.save(follower);
            userRepository.save(followee);
        }
	}

	@Override
	public List<User> getFollowers(String email) {
		return userRepository.findFollowersByEmail(email);
	}

	@Override
	public List<User> getFollowing(String email) {
		return userRepository.findFollowingByEmail(email);
	}

	@Override
	public byte[] getUserPhotoByUserId(Long userId) throws SQLException {
		Optional<User> theUser = userRepository.findById(userId);
		if(theUser.isEmpty()) {
			throw new ResourceNotFoundException("Sorry, User not found!");
		}
		Blob photoBlob = theUser.get().getProfilePicture();
		if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
		return null;
	}
}
