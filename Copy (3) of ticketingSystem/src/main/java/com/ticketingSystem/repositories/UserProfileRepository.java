package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

	
	public UserProfile findById(Integer id);
	
	public UserProfile findByUserId(Integer id);
	
	
	public List<UserProfile>findByManager(UserProfile user);
	
	public List<UserProfile>findBySpecialManager(UserProfile user);
	
	
	@Query(name="select u from UserProfile u where u.userName LIKE %:userName% ")
	public List<UserProfile>findByUserNameStartingWith(@Param("userName") String userName);

}
