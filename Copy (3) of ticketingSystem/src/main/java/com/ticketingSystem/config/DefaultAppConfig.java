package com.ticketingSystem.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.utilities.AutowireHelper;

@Configuration
public class DefaultAppConfig {


	@Autowired
	UserProfileRepository userprofilerepository;
	
	
	
	@Bean
	@Order(2)
	public AutowireHelper autowireHelper() {
		return new AutowireHelper();
	}
}
