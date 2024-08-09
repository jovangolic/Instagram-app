package com.project.Instagram.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.Instagram.exception.PhotoRetrievalException;
import com.project.Instagram.model.User;
import com.project.Instagram.response.UserResponse;
import com.project.Instagram.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/update{userId}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) MultipartFile photo,
			@RequestParam(required = false) String bio) throws SQLException, IOException{
		byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : userService.getUserPhotoByUserId(userId);
		Blob photoBlob = photoBytes != null && photoBytes.length >0 ? new SerialBlob(photoBytes): null;
		User theUser = userService.updateUser(userId, username, email, photoBytes, bio);
		theUser.setProfilePicture(photoBlob);
		UserResponse userResponse = getuserResponse(theUser);
		return ResponseEntity.ok(userResponse);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/search")
	public ResponseEntity<?> searchUsersByUsername(@RequestParam String username){
		try {
			List<User> users = userService.searchUsersByUsername(username);
			return ResponseEntity.ok(users);
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching users: " + e.getMessage());
	    }
	}
	
	//metode za pracenje i otpracivanje korisnika
	@PostMapping("/follow")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> followUser(@RequestParam String followerEmail, @RequestParam String followeeEmail){
		try {
			userService.followUser(followerEmail, followeeEmail);
			return ResponseEntity.ok("User followed successfully!");
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error following user: " + e.getMessage());
	    }
	}
	
	@PostMapping("/unfollow")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> unfollowUser(@RequestParam String followerEmail, @RequestParam String followeeEmail){
		try {
			userService.unfollowUser(followerEmail, followeeEmail);
			return ResponseEntity.ok("User unfollowed successfully!");
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error following user: " + e.getMessage());
	    }
	}
	
	//metode za pratioce i za pracenje
	@GetMapping("/{email}/followers")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getFollowers(@PathVariable String email){
		try {
			List<User> users = userService.getFollowers(email);
			return ResponseEntity.ok(users);
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching followers: " + e.getMessage());
	    }
	}
	
	@GetMapping("/{email}/following")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getFollowing(@PathVariable String enail){
		try {
			List<User> users = userService.getFollowing(enail);
			return ResponseEntity.ok(users);
		}
		catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching followers: " + e.getMessage());
	    }
	}
	
	@GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(){

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
        try{
            User theUser = userService.getUser(email);
            return ResponseEntity.ok(theUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email){
        try{
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }
    
    
    private UserResponse getuserResponse(User user) {
    	byte[] photoBytes = null;
    	Blob photoBlob = user.getProfilePicture();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new UserResponse(user.getId(),
        		user.getUsername(),
        		user.getEmail(),
        		photoBytes,
        		user.getBio());
    }
}
