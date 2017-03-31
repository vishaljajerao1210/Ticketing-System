package com.ticketingSystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.ListMaster;


@Repository
public interface ListMasterRepository  extends JpaRepository<ListMaster,Integer>{

	public ListMaster findByListMasterValue(String listmastervalue);
	
	public ListMaster findById(int id);
	
	
public Page<ListMaster>findAll(Pageable pageable);
	
	
	@Query("select l from ListMaster l where l.listMasterDescription LIKE %:searchParam% OR l.listMasterValue LIKE %:searchParam% ")
	public Page<ListMaster>findBySearchFilter(@Param(value="searchParam")String searchParam,Pageable pageable);
	
	@Query("select count(l.id) from ListMaster l ")
	public Integer findRecordCount();
	
	
	
}
