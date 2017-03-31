package com.ticketingSystem.enums;

public enum ActorType {

	Manager("Manager"),
	
	SpecialManager("Special Manager"),
	Requestor("Requestor"),
	Expert("Expert"),
	ExpertGroup("Expert Group"),
	ExpertGroupTask("Expert Group Task"),
	ExpertTask("Expert Task");
	
	
	private String actorName;
	
	ActorType(String actorName){
		this.actorName=actorName;
	}
	
	public String getActorName(){
		return actorName;
	}
}
