package com.ticketingSystem.enums;

public enum NextActor {
Expert("Expert"),
ExpertGroup("Expert Group");

public String expertName;
NextActor(String expertName)
{
	this.expertName=expertName;
}

public String getExpertName()
{
	return this.expertName;
}
}
