
package com.ticketingSystem.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name="file_uploads")
public class FileUpload {


	@JsonView({MasterView.FileUploadView.class,MasterView.FileItemView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@OneToMany(mappedBy="fileupload",cascade=CascadeType.ALL)
	List<FileItem>fileItems;
	
	@OneToOne(mappedBy="fileupload")
	@JsonIgnore
	TicketMaster ticketmaster;
	
	
	@Column(name="has_ticket")
	boolean hasTicket=false;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="uploaded_by",referencedColumnName="id")	
	UserProfile uploadedBy;
	
@Column(name="expiry_time")
Date expiryTime;
	


@ManyToOne
@JoinColumn(name="modified_by",referencedColumnName="id")
User modifiedBy;
	


	
	@Column(name="uploaded_date")
	Date uploadedDate;
	
	/*@Column(name="uploaded_time")
	String uploadedTime;*/
	
	@Column(name="modified_date")
	Date modifiedDate;
	
	
	
	
	public boolean isHasTicket() {
		return hasTicket;
	}


	public void setHasTicket(boolean hasTicket) {
		this.hasTicket = hasTicket;
	}


	

	public Date getUploadedDate() {
		return uploadedDate;
	}


	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	
	public User getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


		


		public UserProfile getUploadedBy() {
		return uploadedBy;
	}


	public void setUploadedBy(UserProfile uploadedBy) {
		this.uploadedBy = uploadedBy;
	}


		public Date getExpiryTime() {
		return expiryTime;
	}


	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}


	
	
	
	public List<FileItem> getFileItems() {
		return fileItems;
	}


	public void setFileItems(List<FileItem> fileitems) {
		this.fileItems = fileitems;
	}


	public int getId() {
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



	
	
	
}
