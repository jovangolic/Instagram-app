package com.project.Instagram.service;

import java.sql.SQLException;
import java.util.List;



import com.project.Instagram.model.User;



public interface IUserService {

	User registerUser(User user);
	List<User> getUsers();
	void deleteUser(String email);
	User getUser(String email);
	User updateUser(Long userId, String username, String email,byte[] photoBytes, String bio);
	List<User> searchUsersByUsername(String username);
	//metoda za pracenje i otpracivanje korisnika
	void followUser(String followerEmail, String followeeEmail);
	void unfollowUser(String followerEmail, String followeeEmail);
	//metode za dodavanje liste pratitelja i pratilaca odredjenog korisnika
	List<User> getFollowers(String email);
	List<User> getFollowing(String email);
	byte[] getUserPhotoByUserId(Long userId) throws SQLException;
}
