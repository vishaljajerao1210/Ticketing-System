package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name="status")
public class Status {
	
	@JsonView({MasterView.ApproverDetailsView.class,MasterView.SubDomainExpertView.class,MasterView.StatusView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@JsonView({MasterView.ApproverDetailsView.class,MasterView.SubDomainExpertView.class,MasterView.StatusView.class})
	@Column(name="code")
	String code;
	
	@JsonView({MasterView.ApproverDetailsView.class,MasterView.SubDomainExpertView.class,MasterView.StatusView.class})
	@Column(name="status_name")
	String statusName;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
	

}
