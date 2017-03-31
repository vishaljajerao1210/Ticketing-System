package com.ticketingSystem.config;
	
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailDataService userdetaildataservice;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		
		
		http
			.httpBasic().and()
			.authorizeRequests()
				.antMatchers("/public/**").hasAuthority("USER")
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				
				.antMatchers("/user/**","/user/upload/").hasAuthority("USER")
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index.html").and()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
			.csrf().disable();
				
			// @formatter:on
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userdetaildataservice);
		auth.authenticationProvider(authenticationProvider());

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userdetaildataservice);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	
	@Bean
	public AuthenticationEntryPoint getEntryPoint()
	{
		
		BasicAuthenticationEntryPoint b=new BasicAuthenticationEntryPoint();
		b.setRealmName("Spring");
		return b;
		
	}
	
	
	public UserDetailDataService getUserDetails()
	{
		return userdetaildataservice;
	}
	
	

}
