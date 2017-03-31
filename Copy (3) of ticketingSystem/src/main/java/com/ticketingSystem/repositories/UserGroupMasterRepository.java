package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.UserGroupMaster;




@Repository
public interface UserGroupMasterRepository extends JpaRepository<UserGroupMaster, Integer> {

	
	
	public UserGroupMaster findById(int id);
	
	
	public List<UserGroupMaster>findByGroupCodeIgnoreCaseContaining(String groupCode);
	
	
	public UserGroupMaster findByGroupName(String groupName);
	
	public List<UserGroupMaster>findByUsersId(Integer id);
	
	public List<UserGroupMaster>findByGroupNameContaining(String param);
	
}
