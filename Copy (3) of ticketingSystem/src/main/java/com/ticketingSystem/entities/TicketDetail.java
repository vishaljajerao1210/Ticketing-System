package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.enums.TicketType;

@Entity
@Table(name = "ticket_details")
public class TicketDetail {

	
	@JsonView({MasterView.TicketDetailView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@JsonView(MasterView.TicketDetailView.class)
	@ManyToOne
	@JoinColumn
	TicketMaster ticketmaster;
	
	@JsonView({MasterView.TicketDetailView.class})
	@Column(name="field_name")
	@NotNull
	String fieldName;
	
	
	
	@JsonView({MasterView.TicketDetailView.class})
	@Column(name="value")
	@NotNull
	String value;
	
	@JsonView({MasterView.TicketDetailView.class})
	@Enumerated(value = EnumType.STRING)
	TicketType tickettype;


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

	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public TicketType getTickettype() {
		return tickettype;
	}


	public void setTickettype(TicketType tickettype) {
		this.tickettype = tickettype;
	}	
}
