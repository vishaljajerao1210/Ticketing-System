package com.ticketingSystem.utilities;

import java.text.ParseException;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Autowired;

import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.AbstractEntityMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.repositories.UserProfileRepository;


public class AuditListener {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserProfileRepository userprofilerepostory;
	
	@PrePersist
	@PreUpdate
	public void setProperties(AbstractEntityMaster entity) throws ParseException
	{
		AutowireHelper.autowire(this, this.authService);
		
		if(entity.getCreatedBy()==null&&entity.getCreatedDate()==null)
		{
		entity.setCreatedBy(authService.loggedInUser().getUsername());
		entity.setCreatedDate(DateUtilities.getCurrentDateAndTime());
		}
		else
		{
			entity.setModifiedBy(authService.loggedInUser().getUsername());
			entity.setModifiedDate(DateUtilities.getCurrentDateAndTime());
		}
			
		
		
	}

	
	
}
