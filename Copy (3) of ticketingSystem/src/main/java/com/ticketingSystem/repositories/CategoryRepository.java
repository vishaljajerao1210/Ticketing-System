package com.ticketingSystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.Category;



@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

	public Category findById(int id);
	
	public Category findByCategoryDescription(String categoryName);
	
	
	public Page<Category>findAll(Pageable pageable);
	
	
	public Page<Category>findByCategoryDescriptionContaining(String param,Pageable pageable);
	

	@Query("select count(c.id) from Category c")
	public Integer findCount();
	
	
	
	
}
