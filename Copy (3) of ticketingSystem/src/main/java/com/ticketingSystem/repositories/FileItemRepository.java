package com.ticketingSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.FileItem;



@Repository 
public interface FileItemRepository extends JpaRepository<FileItem, Integer>{

	
	
	
	public FileItem findByFileName(String fileName);
	
	
	public FileItem findByFileuploadIdAndFileName(int id,String name);
	
}
