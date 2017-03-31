package com.ticketingSystem.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.enums.StandardTATUnitType;
import com.ticketingSystem.enums.TATType;

@Entity
@Table(name="subdomains")
public class SubDomain extends AbstractEntityMaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView({MasterView.SubDomainView.class,MasterView.SubDomainFieldView.class,MasterView.TicketMasterView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@JsonView({MasterView.SubDomainView.class,MasterView.SubDomainFieldView.class,MasterView.TicketMasterView.class})
	@Column(name="subdomain_description")
	String subDomainDescription;

	@JsonView(MasterView.SubDomainView.class)
	@ManyToOne
	@JoinColumn
	Domain domains;
		
	@OneToMany(mappedBy="subdomainDispositioning")
	@JsonIgnore
	List<SubDomianExpertDispositionMapping> subdomainexpertdispositioning;
	
	//@JsonView(MasterView.SubDomainView.class)
	@OneToMany(mappedBy="subdomains")
	@JsonIgnore
	List<SubDomainField>  subdomainfield;
	
	@JsonView({MasterView.SubDomainView.class,MasterView.TicketMasterView.class})
	@Column(name="ismanager_approval")
	boolean isManagerApproval=true;
	
	@JsonView({MasterView.SubDomainView.class,MasterView.TicketMasterView.class})
	@Column(name="isspecialmanager_approval")
	boolean isSpecialManagerApproval=false;
	
	@JsonView(MasterView.SubDomainView.class)
	@Column(name="issendto_sameexpert")
	boolean isSendToSameExpert=false;
	
	@JsonView(MasterView.SubDomainView.class)
	@Column(name="standard_tatvalue")
	Date standardTATValue;
	
	@JsonView(MasterView.SubDomainView.class)
	@Enumerated(value = EnumType.STRING)
	StandardTATUnitType standardTATUnitType;
	
	@JsonView(MasterView.SubDomainView.class)
	@Enumerated(value = EnumType.STRING)
	TATType TATType;
	
	@OneToMany(mappedBy="subdomain")
	@JsonIgnore
	List<TicketMaster>ticketmasters;
	
	@JsonView(MasterView.SubDomainView.class)
	@ManyToOne
	UserGroupMaster defaultUserGroup;
	
	@JsonView(MasterView.SubDomainView.class)
	@ManyToOne
	UserProfile defaultExpert;
	
	@JsonView(MasterView.SubDomainView.class)
	@Column(name="actor_type")
	String actorType;
	
	@JsonView({MasterView.SubDomainView.class,MasterView.TicketMasterView.class})
	@Column(name="is_group")
	boolean isGroup=false;



	public UserProfile getDefaultExpert() {
		return defaultExpert;
	}


	public void setDefaultExpert(UserProfile defaultExpert) {
		this.defaultExpert = defaultExpert;
	}


	public String getActorType() {
		return actorType;
	}


	public void setActorType(String actorType) {
		this.actorType = actorType;
	}


	public boolean isGroup() {
		return isGroup;
	}


	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}


	public List<SubDomianExpertDispositionMapping> getSubdomainexpertdispositioning() {
		return subdomainexpertdispositioning;
	}


	public void setSubdomainexpertdispositioning(List<SubDomianExpertDispositionMapping> subdomainexpertdispositioning) {
		this.subdomainexpertdispositioning = subdomainexpertdispositioning;
	}


	
	
	public UserGroupMaster getDefaultUserGroup() {
		return defaultUserGroup;
	}


	public void setDefaultUserGroup(UserGroupMaster defaultUserGroup) {
		this.defaultUserGroup = defaultUserGroup;
	}


	


	public List<SubDomainField> getSubdomainfield() {
		return subdomainfield;
	}


	public void setSubdomainfield(List<SubDomainField> subdomainfield) {
		this.subdomainfield = subdomainfield;
	}


	public List<TicketMaster> getTicketmasters() {
		return ticketmasters;
	}


	public void setTicketmasters(List<TicketMaster> ticketmasters) {
		this.ticketmasters = ticketmasters;
	}


	public Date getStandardTATValue() {
		return standardTATValue;
	}

	
	public void setStandardTATValue(Date standardTATValue) {
		this.standardTATValue = standardTATValue;
	}


	public boolean isManagerApproval() {
		return isManagerApproval;
	}

	public void setManagerApproval(boolean isManagerApproval) {
		this.isManagerApproval = isManagerApproval;
	}

	public boolean isSpecialManagerApproval() {
		return isSpecialManagerApproval;
	}

	public void setSpecialManagerApproval(boolean isSpecialManagerApproval) {
		this.isSpecialManagerApproval = isSpecialManagerApproval;
	}

	public boolean isSendToSameExpert() {
		return isSendToSameExpert;
	}

	public void setSendToSameExpert(boolean isSendToSameExpert) {
		this.isSendToSameExpert = isSendToSameExpert;
	}

	public StandardTATUnitType getStandardTATUnitType() {
		return standardTATUnitType;
	}

	public void setStandardTATUnitType(StandardTATUnitType standardTATUnitType) {
		this.standardTATUnitType = standardTATUnitType;
	}

	public TATType getTATType() {
		return TATType;
	}

	public void setTATType(TATType tATType) {
		TATType = tATType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getSubDomainDescription() {
		return subDomainDescription;
	}

	public void setSubDomainDescription(String subDomainDescription) {
		this.subDomainDescription = subDomainDescription;
	}

	public Domain getDomains() {
		return domains;
	}

	public void setDomains(Domain domains) {
		this.domains = domains;
	}
	
	@JsonView(MasterView.SubDomainView.class)
	public boolean isNoDispositions(){	
		return this.subdomainexpertdispositioning == null || this.subdomainexpertdispositioning.isEmpty();
	}
	
	
}
