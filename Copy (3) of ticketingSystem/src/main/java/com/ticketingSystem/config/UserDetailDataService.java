package com.ticketingSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ticketingSystem.entities.User;
import com.ticketingSystem.repositories.UserRepository;

@Service
public class UserDetailDataService implements UserDetailsService{

	@Autowired
	UserRepository userrepository;
	
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userrepository.findByUsername(username);
		//System.out.print("\ncurrentpassword"+user.getPassword());
		 if (user==null)
		 {
	            throw new UsernameNotFoundException("username " + username + " not found");
		 }
        
	        return user;
	}

}
