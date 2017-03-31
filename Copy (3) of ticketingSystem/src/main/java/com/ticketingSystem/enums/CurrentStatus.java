package com.ticketingSystem.enums;

public enum CurrentStatus {

	;
	
	
	public String ticketStatus;
	CurrentStatus(String ticketStatus)
	{
		this.ticketStatus=ticketStatus;
		
	}
		public String getCurrentStatus()
		{
			return ticketStatus;
		}
}
