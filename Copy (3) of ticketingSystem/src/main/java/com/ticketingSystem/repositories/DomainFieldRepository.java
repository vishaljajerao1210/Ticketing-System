package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.DomainField;



@Repository
public interface DomainFieldRepository extends JpaRepository<DomainField, Integer>{

	
	public DomainField findById(int id);
	
	public List<DomainField> findByDomainId(int domainId);
	
	
	public Page<DomainField>findAll(Pageable pageable);
	
	
	@Query("select d from DomainField d where d.domain.domainDescription LIKE %:searchParam% OR d.fieldName LIKE %:searchParam% OR d.fieldType LIKE %:searchParam% ")
	public Page<DomainField>findBySearchFilter(@Param(value="searchParam")String searchParam,Pageable pageable);
	
	@Query("select count(d.id) from DomainField d ")
	public Integer findRecordCount();
	
}
