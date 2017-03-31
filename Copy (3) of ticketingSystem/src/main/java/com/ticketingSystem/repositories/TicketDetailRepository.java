package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.TicketDetail;



@Repository
public interface TicketDetailRepository  extends JpaRepository<TicketDetail, Integer>{

	
	
	public List<TicketDetail>findByTicketmasterId(int id);
}
