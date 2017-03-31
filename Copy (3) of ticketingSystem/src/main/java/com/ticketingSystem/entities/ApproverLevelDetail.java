package com.ticketingSystem.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name = "approver_level_detail")
public class ApproverLevelDetail {

	@JsonView(MasterView.ApproverDetailsView.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@JsonView(MasterView.ApproverDetailsView.class)
	@OneToOne
	@JoinColumn(name = "assigned_to")
	private UserProfile assignedTo;

	
	@JsonView(MasterView.ApproverDetailsView.class)
	@OneToOne
	@JoinColumn(name = "assigned_group")
	private UserGroupMaster assignedGroup;

	@JsonView(MasterView.ApproverDetailsView.class)
	@OneToOne
	@JoinColumn(name = "approved_by")
	private UserProfile approvedBy;

	@JsonView(MasterView.ApproverDetailsView.class)
	@Column(name = "action_date")
	private Date actionDate;

	

	@JsonView(MasterView.ApproverDetailsView.class)
	@Column(name = "comments")
	private String comments;

	@JsonView(MasterView.ApproverDetailsView.class)
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", name = "approver_level_id")
	@JsonIgnore
	ApproverLevel approverLevel;

	@JsonView(MasterView.ApproverDetailsView.class)
	@OneToOne
	@JoinColumn(referencedColumnName = "id", name = "status_id")
	Status status;

	@JsonView(MasterView.ApproverDetailsView.class)
	@Column(name="approver_type")
	String approverType;

	public UserGroupMaster getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(UserGroupMaster assignedGroup) {
		this.assignedGroup = assignedGroup;
	}
	public Date getActionDate() {
		return actionDate;
	}

	public void setAssignedTo(UserProfile assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public String getApproverType() {
		return approverType;
	}

	public void setApproverType(String approverType) {
		this.approverType = approverType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public UserProfile getAssignedTo() {
		return assignedTo;
	}

	
	
	
	public UserProfile getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UserProfile approvedBy) {
		this.approvedBy = approvedBy;
	}

	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ApproverLevel getApproverLevel() {
		return approverLevel;
	}

	public void setApproverLevel(ApproverLevel approverLevel) {
		this.approverLevel = approverLevel;
	}

}
