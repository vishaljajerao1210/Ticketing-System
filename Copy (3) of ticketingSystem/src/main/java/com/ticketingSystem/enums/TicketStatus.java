package com.ticketingSystem.enums;



public enum TicketStatus {

	
	Active("Active"),InActive("In Active"),Pending("Pending"),Resolved("Resolved"),Terminated("Terminated"),Closed("Closed");
public String ticketStatus;
TicketStatus(String ticketStatus)
{
	this.ticketStatus=ticketStatus;
	
}
	public String getTicketStatus()
	{
		return ticketStatus;
	}
	
}
