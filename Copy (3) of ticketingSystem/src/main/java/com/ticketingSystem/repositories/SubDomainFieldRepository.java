package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.SubDomainField;

;

@Repository
public interface SubDomainFieldRepository  extends JpaRepository<SubDomainField,Integer>{

	public SubDomainField findById(int id);

	
	
	public List<SubDomainField> findBySubdomainsId(int id);
	
	
public Page<SubDomainField>findAll(Pageable pageable);
	
	
	@Query("select d from SubDomainField d where d.subdomains.subDomainDescription LIKE %:searchParam% OR d.fieldName LIKE %:searchParam% OR d.fieldType LIKE %:searchParam% ")
	public Page<SubDomainField>findBySearchFilter(@Param(value="searchParam")String searchParam,Pageable pageable);
	
	@Query("select count(d.id) from SubDomainField d ")
	public Integer findRecordCount();
	
}
