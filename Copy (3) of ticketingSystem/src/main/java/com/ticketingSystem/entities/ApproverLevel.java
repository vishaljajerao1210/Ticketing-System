package com.ticketingSystem.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="approver_level")
public class ApproverLevel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@ManyToOne
	TicketMaster ticketmaster;
	
	@Column(name="created_date")
	Date createdDate;
	
	@Column(name="modified_date")
	Date modifiedDate;
	
	@OneToMany(mappedBy="approverLevel")
	List<ApproverLevelDetail>approvalleveldetail;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TicketMaster getTicketmaster() {
		return ticketmaster;
	}

	public void setTicketmaster(TicketMaster ticketmaster) {
		this.ticketmaster = ticketmaster;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<ApproverLevelDetail> getApprovalleveldetail() {
		return approvalleveldetail;
	}

	public void setApprovalleveldetail(List<ApproverLevelDetail> approvalleveldetail) {
		this.approvalleveldetail = approvalleveldetail;
	}
	
	
	
	
	
	
}
