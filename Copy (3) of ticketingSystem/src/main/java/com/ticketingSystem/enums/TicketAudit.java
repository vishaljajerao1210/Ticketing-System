package com.ticketingSystem.enums;

import java.util.Date;

import org.apache.commons.lang3.text.StrBuilder;

import com.ticketingSystem.entities.Status;

public enum TicketAudit {

	STS01("#status by #level - #assignee"),

	STS02("Ticket #status by #level - #assignee"),

	STS03("#status by #level - #assignee"),

	STS04("#status"),

	STS05("#status-#assignee"),

	STS06("#status-#assignee"),

	STS07("#status - #group"),

	STS09("#status - #assignee"),

	STS08("#status by #assignee"),

	STS10("Ticket #status"),

	STS11("Ticket #status"),

	STS12("#status - #group  by #assignee"),
	
	STS13("#status   by #assignee");
	
	private TicketAudit(String template) {
		this.template = template;
	}

	public static String getApprovalStatus() {
		return "Status is '#status' by '#assignee','#level'  on '#date'";
	}

	public static String getAssigneeStatus()
	{
		return "Status is '#status' by '#assignee', '#level'  on '#date'";
	}
	
	private String template;

	public String returnStatus(String approverName, String group, String assignee, String level,
			Status s) {

		return new StrBuilder(template).replaceAll("#status", s.getStatusName()).replaceAll("#level", level)
				.replaceAll("#group", group).replaceAll("#assignee", assignee)
				.replaceAll("#approverName", approverName).toString();
	}

}
