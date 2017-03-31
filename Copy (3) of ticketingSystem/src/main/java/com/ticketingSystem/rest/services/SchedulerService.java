package com.ticketingSystem.rest.services;


import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ticketingSystem.entities.FileUpload;
import com.ticketingSystem.repositories.FileUploadRepository;
import com.ticketingSystem.utilities.DateUtilities;


@Component
@EnableAsync
@EnableScheduling
public class SchedulerService {

	@Value("${multipart.location}")
	String location;
	
	
	
	
	@Autowired
	FileUploadRepository fileuploadrepository;
	
	
@Scheduled(fixedRate=60*60*1000)	
	public void doDeletion() throws ParseException
	{
	
	
		
		List<FileUpload>fileupload=fileuploadrepository.findByHasTicket(false);
		Date currentDate=DateUtilities.getCurrentDateAndTime();
		
		
		
		
		Iterator<FileUpload>it=fileupload.iterator();
		if(it.hasNext())
		{
			FileUpload f=it.next();
			Date expiryDate=DateUtilities.getCurrentDateAndTime(f.getExpiryTime());
			if(currentDate.compareTo(expiryDate)>0)
			{
				File file=new File(location+f.getId());
				if (file.isFile() && file.exists()) {
					file.delete();
				}
				fileuploadrepository.delete(f.getId());
				
			}
		}
		
		
	}
	
	
	
}
