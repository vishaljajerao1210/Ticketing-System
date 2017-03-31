package com.ticketingSystem.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name = "user_group_masters")
public class UserGroupMaster {

	@JsonView({MasterView.UserProfileView.class,MasterView.UserGroupView.class,MasterView.SubDomainView.class,MasterView.TicketMasterView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@JsonView({MasterView.UserProfileView.class,MasterView.UserGroupView.class,MasterView.SubDomainView.class,MasterView.TicketMasterView.class})
	@Column(name="group_name")
	String groupName;
	
	@JsonView({MasterView.UserProfileView.class,MasterView.UserGroupView.class,MasterView.SubDomainView.class})
	@Column(name="group_code")
	String groupCode;

	@JsonView(MasterView.UserGroupView.class)
	@ManyToMany
	 @JoinTable(
		      name="user_profile_group",
		      joinColumns=@JoinColumn(name="group_id", referencedColumnName="id"),
		      inverseJoinColumns=@JoinColumn(name="user_id", referencedColumnName="id"))
	List<UserProfile>users;
	
	
	
	
	

	public List<UserProfile> getUsers() {
		return users;
	}

	public void setUsers(List<UserProfile> users) {
		this.users = users;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	
	
	
	
	
	
}
