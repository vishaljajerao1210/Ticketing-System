package com.ticketingSystem.rest.services;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.entities.TicketMaster;

public class CompletedTask {
	
	@JsonView(MasterView.TicketMasterView.class)
	private TicketMaster ticketMaster;
	
	@JsonView(MasterView.TicketMasterView.class)
	private Date endDate;
	
	@JsonView(MasterView.TicketMasterView.class)
	private String actionPerformed;

	@JsonView(MasterView.TicketMasterView.class)
	private String approverLevel;
	
	
	
	public String getApproverLevel() {
		return approverLevel;
	}

	public void setApproverLevel(String approverLevel) {
		this.approverLevel = approverLevel;
	}

	public TicketMaster getTicketMaster() {
		return ticketMaster;
	}

	public void setTicketMaster(TicketMaster ticketMaster) {
		this.ticketMaster = ticketMaster;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(String actionPerformed) {
		this.actionPerformed = actionPerformed;
	}
	
}
