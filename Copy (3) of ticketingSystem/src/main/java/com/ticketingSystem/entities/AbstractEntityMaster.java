package com.ticketingSystem.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView.CategoryView;
import com.ticketingSystem.Views.MasterView.MasterDataView;
import com.ticketingSystem.utilities.AuditListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class,AuditListener.class})
public abstract class AbstractEntityMaster implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView(MasterDataView.class)
	@Column(name = "created_date")
	@CreatedDate
	Date createdDate;

@JsonView(MasterDataView.class)
	@Column(name = "modified_date")
	@LastModifiedDate
	Date modifiedDate;


@JsonView(MasterDataView.class)
	@CreatedBy
	String createdBy;



@JsonView(MasterDataView.class)
	@LastModifiedBy
	String modifiedBy;

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

	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
