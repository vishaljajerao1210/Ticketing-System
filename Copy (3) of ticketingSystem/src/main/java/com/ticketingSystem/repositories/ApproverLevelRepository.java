package com.ticketingSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.ApproverLevel;
import com.ticketingSystem.entities.TicketMaster;

@Repository
public interface ApproverLevelRepository  extends JpaRepository<ApproverLevel, Integer>{
	
	public ApproverLevel findById(Integer id);
	
	
	public ApproverLevel findByTicketmaster(TicketMaster ticketmaster);

}
