
package com.ticketingSystem.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ticket_masters")
public class TicketMaster {

	@JsonView({MasterView.TicketMasterView.class,MasterView.TicketDetailView.class})
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

	@JsonView(MasterView.TicketMasterView.class)
	@OneToMany(mappedBy = "ticketmaster")
	@JsonIgnore
	List<TicketDetail> ticketdetails;

	@JsonView({MasterView.TicketMasterView.class })
	@ManyToOne
	@JoinColumn
	Domain domains;

	@JsonView({MasterView.TicketMasterView.class })
	@ManyToOne
	@JoinColumn
	SubDomain subdomain;

	@JsonView({MasterView.TicketMasterView.class })
	@ManyToOne
	@JoinColumn
	Category categories;

	@JsonView({MasterView.TicketMasterView.class })
	@CreatedDate
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "raised_date", nullable = false)
	public Date raisedDate;

	@JsonView({MasterView.TicketMasterView.class })
	@Column(name = "raised_time", nullable = false)
	public String raisedTime;

	@JsonView({MasterView.TicketMasterView.class })
	@Column(name ="current_status")
	String currentStatus;

	@OneToOne
	FileUpload fileupload;

	@JsonView({MasterView.TicketMasterView.class })
	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="raised_by",referencedColumnName="id")
	UserProfile raisedBy;
	
	
	@JsonView({MasterView.TicketMasterView.class })
	@Column(name="instance_id")
	String instanceId;

	
	
	

	@JsonView({MasterView.TicketMasterView.class })
	@Column(name="assigned_on")
	Date assignedOn;

	
	
	
	@JsonView({MasterView.TicketMasterView.class })
	String status;
	
	
	@JsonView({MasterView.TicketMasterView.class })
	boolean isClaimed=false;
	
	@JsonView({MasterView.TicketMasterView.class })
	 @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="claimed_By",referencedColumnName="id")
	UserProfile claimedBy;
	
	@JsonView({MasterView.TicketMasterView.class,MasterView.TicketDetailView.class})
	@Column(name="is_expert_task")
	boolean isExpertTask=false;
	
	
	@JsonView({MasterView.TicketMasterView.class })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="current_expert",referencedColumnName="id")
	UserProfile currentExpert;
	
	
	@JsonView({MasterView.TicketMasterView.class })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="current_group",referencedColumnName="id")
	UserGroupMaster currentGroupMaster;
	
	@JsonView({MasterView.TicketMasterView.class })
	@Column(name="is_current_group")
	boolean isCurrentGroup=false;
	
	
	
	
	

	
	public boolean isCurrentGroup() {
		return isCurrentGroup;
	}

	public void setCurrentGroup(boolean isCurrentGroup) {
		this.isCurrentGroup = isCurrentGroup;
	}

	public UserProfile getCurrentExpert() {
		return currentExpert;
	}

	public void setCurrentExpert(UserProfile currentExpert) {
		this.currentExpert = currentExpert;
	}

	public UserGroupMaster getCurrentGroupMaster() {
		return currentGroupMaster;
	}

	public void setCurrentGroupMaster(UserGroupMaster currentGroupMaster) {
		this.currentGroupMaster = currentGroupMaster;
	}

	public UserProfile getClaimedBy() {
		return claimedBy;
	}

	public void setClaimedBy(UserProfile claimedBy) {
		this.claimedBy = claimedBy;
	}

	public boolean isExpertTask() {
		return isExpertTask;
	}

	public void setExpertTask(boolean isExpertTask) {
		this.isExpertTask = isExpertTask;
	}

	public boolean isClaimed() {
		return isClaimed;
	}

	public void setClaimed(boolean isClaimed) {
		this.isClaimed = isClaimed;
	}

	
	

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	
	public Date getAssignedOn() {
		return assignedOn;
	}

	public void setAssignedOn(Date assignedOn) {
		this.assignedOn = assignedOn;
	}
	

	

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserProfile getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(UserProfile raisedBy) {
		this.raisedBy = raisedBy;
	}

	public FileUpload getFileupload() {
		return fileupload;
	}

	public void setFileupload(FileUpload fileupload) {
		this.fileupload = fileupload;
	}

	

	public Domain getDomains() {
		return domains;
	}

	public void setDomains(Domain domains) {
		this.domains = domains;
	}



	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SubDomain getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(SubDomain subdomain) {
		this.subdomain = subdomain;
	}

	public Date getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}

	public String getRaisedTime() {
		return raisedTime;
	}

	public void setRaisedTime(String raisedTime) {
		this.raisedTime = raisedTime;
	}



	

	

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public List<TicketDetail> getTicketdetails() {
		return ticketdetails;
	}

	public void setTicketdetails(List<TicketDetail> ticketdetails) {
		this.ticketdetails = ticketdetails;
	}

	public Category getCategories() {
		return categories;
	}

	public void setCategories(Category categories) {
		this.categories = categories;
	}

}
