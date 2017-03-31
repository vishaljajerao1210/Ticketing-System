package com.ticketingSystem.authService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ticketingSystem.entities.User;


@Service
public class AuthService {

	
	
	
	
	public User loggedInUser()
	{
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
}
