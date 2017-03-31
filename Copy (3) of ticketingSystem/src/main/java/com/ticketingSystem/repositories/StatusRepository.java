package com.ticketingSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

	
	public Status findByStatusName(String name);
	
	public Status findById(Integer id);
	public Status findByCode(String code);
}
