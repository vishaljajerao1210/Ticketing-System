package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name="subdomain_expert_disposition_mapping")
public class SubDomianExpertDispositionMapping extends AbstractEntityMaster {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


@JsonView(MasterView.SubDomainExpertView.class)
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
int id;
	

@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
SubDomain subdomainDispositioning;
	
	
	 

@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
UserGroupMaster currentAssignedGroup;
	

@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
UserProfile currentAssignedUser;


@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="assigned_actor")
String assignedActor;

	




@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="is_terminal")
boolean isTerminal;
	
	

@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
UserProfile nextUser;
	
@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="next_actor")
String nextActor;
	


@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
UserGroupMaster nextUserGroup;
	

@JsonView(MasterView.SubDomainExpertView.class)
@ManyToOne
Status currentStatus;



@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="action_name")
String actionName;
	


	
@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="is_show_to_user")
boolean isShowToUser=true;
	


@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="is_next_group")
boolean isNextGroup;
	
@JsonView(MasterView.SubDomainExpertView.class)
@Column(name="is_current_group")
boolean isCurrentGroup;
	







public String getActionName() {
	return actionName;
}

public void setActionName(String actionName) {
	this.actionName = actionName;
}

public UserProfile getCurrentAssignedUser() {
	return currentAssignedUser;
}

public void setCurrentAssignedUser(UserProfile currentAssignedUser) {
	this.currentAssignedUser = currentAssignedUser;
}

public String getAssignedActor() {
	return assignedActor;
}

public void setAssignedActor(String assignedActor) {
	this.assignedActor = assignedActor;
}

public String getNextActor() {
	return nextActor;
}

public void setNextActor(String nextActor) {
	this.nextActor = nextActor;
}

public boolean isNextGroup() {
	return isNextGroup;
}

public void setNextGroup(boolean isNextGroup) {
	this.isNextGroup = isNextGroup;
}

public boolean isCurrentGroup() {
	return isCurrentGroup;
}

public void setCurrentGroup(boolean isCurrentGroup) {
	this.isCurrentGroup = isCurrentGroup;
}

	public SubDomain getSubdomainDispositioning() {
		return subdomainDispositioning;
	}

	public void setSubdomainDispositioning(SubDomain subdomainDispositioning) {
		this.subdomainDispositioning = subdomainDispositioning;
	}

	
	public UserGroupMaster getCurrentAssignedGroup() {
		return currentAssignedGroup;
	}

	public void setCurrentAssignedGroup(UserGroupMaster currentAssignedGroup) {
		this.currentAssignedGroup = currentAssignedGroup;
	}

	public UserProfile getNextUser() {
		return nextUser;
	}

	public void setNextUser(UserProfile nextUser) {
		this.nextUser = nextUser;
	}

	public String getNextactor() {
		return nextActor;
	}

	public void setNextactor(String nextactor) {
		this.nextActor = nextactor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	
	

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}


	public UserGroupMaster getNextUserGroup() {
		return nextUserGroup;
	}

	public void setNextUserGroup(UserGroupMaster nextUserGroup) {
		this.nextUserGroup = nextUserGroup;
	}

	

	

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public boolean isShowToUser() {
		return isShowToUser;
	}

	public void setShowToUser(boolean isShowToUser) {
		this.isShowToUser = isShowToUser;
	}

	
	
	
}
