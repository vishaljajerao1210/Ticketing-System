package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.ListValue;




@Repository
public interface ListValuesRepository extends JpaRepository<ListValue,Integer>{

	public ListValue findById(int id);

	
public Page<ListValue>findAll(Pageable pageable);
	
	
	@Query("select l from ListValue l where l.listmaster.id = :listId AND (l.listmaster.listMasterDescription LIKE %:searchParam% OR l.listItemStorageValue LIKE %:searchParam% OR l.listItemDisplayValue LIKE %:searchParam%)")
	public List<ListValue>findBySearchFilterAndListMasterId(@Param(value="searchParam")String searchParam, @Param(value="listId")Integer listId);
	
	@Query("select count(l.id) from ListValue l ")
	public Integer findRecordCount();
}
