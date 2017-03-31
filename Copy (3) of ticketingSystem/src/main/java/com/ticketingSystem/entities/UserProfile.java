package com.ticketingSystem.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name = "user_profile")
public class UserProfile {

	@JsonView({ MasterView.UserProfileView.class, MasterView.UserGroupView.class,MasterView.TicketMasterView.class,MasterView.SubDomainView.class })
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@JsonView({ MasterView.UserProfileView.class, MasterView.UserGroupView.class,MasterView.TicketMasterView.class,MasterView.SubDomainView.class,MasterView.ApproverDetailsView.class })
	@Column(name = "user_name")
	String userName;

	@JsonView({ MasterView.UserProfileView.class, MasterView.UserGroupView.class })
	@Column(name = "user_id", unique = true)
	Integer userId;


	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(name = "user_profile_group", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private List<UserGroupMaster> usergroupmaster;

	@JsonView({ MasterView.UserProfileView.class })
	@ManyToOne(optional=true)
	UserProfile manager;

	@JsonView({ MasterView.UserProfileView.class })
	@Column(name = "email")
	String email;

	public UserProfile() {
	}

	public UserProfile(Integer id, String userName,String email) {
		this.id = id;
		this.userName = userName;
		this.email=email;
	}

	@JsonView({ MasterView.UserProfileView.class })
	@ManyToOne(optional=true)
	UserProfile specialManager;

	@JsonView({ MasterView.UserProfileView.class })
	@Column(name = "contact_no")
	String contactNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<UserGroupMaster> getUsergroupmaster() {
		return usergroupmaster;
	}

	public void setUsergroupmaster(List<UserGroupMaster> usergroupmaster) {
		this.usergroupmaster = usergroupmaster;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public UserProfile getManager() {
		if(manager==null)
		{
			return null;
		}
		
		return new UserProfile(manager.id, manager.userName,manager.email);
	}

	public void setManager(UserProfile manager) {
		this.manager = manager;
	}

	@JsonView({ MasterView.UserProfileView.class })
	public UserProfile getSpecialManager() {
		if(specialManager==null)
		{
			return null;
		}
		
		return new UserProfile(specialManager.id, specialManager.userName,specialManager.email);
	}

	public void setSpecialManager(UserProfile specialManager) {
		this.specialManager = specialManager;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

}
