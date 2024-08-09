package com.project.Instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Instagram.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	void deleteByEmail(String email);

	Optional<User> findByEmail(String email);
	
	//metoda za pretragu korisnika koja sadrzi odredjena slova ili rec
	List<User> findByUsernameContainingIgnoreCase(String username);
	
	@Query("SELECT u.followers FROM User u WHERE u.email = :email")
    List<User> findFollowersByEmail(@Param("email") String email);

    @Query("SELECT u.following FROM User u WHERE u.email = :email")
    List<User> findFollowingByEmail(@Param("email") String email);
}
