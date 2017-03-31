package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.Status;
import com.ticketingSystem.entities.SubDomain;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;

@Repository
public interface SubDomainExpertRepository  extends JpaRepository<SubDomianExpertDispositionMapping, Integer>{

	
	public SubDomianExpertDispositionMapping findById(Integer id);
	public SubDomianExpertDispositionMapping findBySubdomainDispositioningAndCurrentAssignedGroupAndCurrentStatus(SubDomain subdomain,UserGroupMaster usergroup,Status status);
	public SubDomianExpertDispositionMapping findBySubdomainDispositioningAndCurrentAssignedUserAndCurrentStatus(SubDomain subdomain,UserProfile user,Status status);
	
	public List<SubDomianExpertDispositionMapping> findBySubdomainDispositioningAndCurrentAssignedUser(SubDomain subdomain,UserProfile user);
	
	public List<SubDomianExpertDispositionMapping> findBySubdomainDispositioningAndCurrentAssignedGroup(SubDomain s,UserGroupMaster usergroup);
	public Page<SubDomianExpertDispositionMapping> findBySubdomainDispositioning_Id(Integer subDomainId, Pageable page);
}

