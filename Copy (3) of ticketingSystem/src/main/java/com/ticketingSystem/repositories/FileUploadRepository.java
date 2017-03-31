package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.FileUpload;



@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload,Integer> {

	
	public FileUpload findByTicketmasterId(int id);
	
	
	public List<FileUpload>findByHasTicket(boolean flag);
}
