package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.SubDomain;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;



@Repository
public interface SubDomainRepository extends JpaRepository<SubDomain,Integer> {

	public SubDomain findById(int id);

	
	public SubDomain findBySubDomainDescription(String subdomaindescription);
	
	


	
	
	public List<SubDomain>findByDomainsIdAndSubDomainDescriptionContaining(int id, String subDomainName);
	
	public SubDomain findByDomainsIdAndSubDomainDescription(Integer id,String subDomainName);


	
	public List<SubDomain>findByDefaultUserGroup(UserGroupMaster userGroup);
	
	public Page<SubDomain>findAll(Pageable pageable);
	
	
	@Query("select s from SubDomain s where s.subDomainDescription LIKE %:searchParam% OR s.domains.domainDescription LIKE %:searchParam%")
	public Page<SubDomain>findBySearchFilter(@Param(value="searchParam")String searchParam,Pageable pageable);
	
	@Query("select count(s.id) from SubDomain s")
	public Integer findRecordCount();
	
	public List<SubDomain>findByDefaultExpert(UserProfile user);

	
	
}
