package com.ticketingSystem.entities;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name="users")
public class User implements UserDetails {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6166599801005567812L;


	@JsonView({MasterView.UserView.class/*,MasterView.UserGroupView.class,MasterView.UserManagerView.class,MasterView.TicketMasterView.class,MasterView.ApproverDetailsView.class */})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	
	@JsonView({MasterView.UserView.class})
	@Column(name="user_name")
	String username;
	
	@JsonView({MasterView.UserView.class})
	@Column(name="user_status")
	String userStatus;
	
	@JsonView({MasterView.UserView.class})
	@Column(name="user_code")
	String userCode;
	
	
	@Column(name="password")
	String password;
	
	
	
	
	@JsonView(MasterView.UserView.class)
	@OneToMany(mappedBy="user",fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	List<UserRoles>userRoles;
	
	
	
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
	
	
	
	
	
	public void setPassword(String password) {
		//BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		this.password = password;//encoder.encode(password);
	}

	public List<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	
	@Override
	public Collection<UserRoles> getAuthorities() {
		// TODO Auto-generated method stub
		return this.userRoles;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
}

	

