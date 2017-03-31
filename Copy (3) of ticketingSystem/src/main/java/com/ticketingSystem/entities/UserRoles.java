package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name="user_roles")
public class UserRoles implements GrantedAuthority {

	/**
	 * +
	 * 
	 */
	private static final long serialVersionUID = 4834174738140712286L;

	@JsonView(MasterView.UserView.class)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@JsonView(MasterView.UserView.class)
	@Column(name="role_type")
	String roleType;
	
	@ManyToOne
	@JsonIgnore
	User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user= user;
	}

	@Override
	@JsonIgnore
	public String getAuthority() {
		// TODO Auto-generated method stub
		return roleType;
	}
	
}
