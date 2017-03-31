package com.ticketingSystem.activitiService;

import java.text.ParseException;
import java.util.HashMap;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.dh.notification.channel.ChannelEnum;
import org.dh.notification.connect.NotificationRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.ApproverLevelDetail;
import com.ticketingSystem.entities.Status;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.enums.ActorType;
import com.ticketingSystem.enums.TicketStatus;
import com.ticketingSystem.repositories.ApproverLevelDetailRepository;
import com.ticketingSystem.repositories.ApproverLevelRepository;
import com.ticketingSystem.repositories.StatusRepository;
import com.ticketingSystem.repositories.SubDomainExpertRepository;
import com.ticketingSystem.repositories.SubDomainRepository;
import com.ticketingSystem.repositories.TicketMasterRepository;
import com.ticketingSystem.repositories.UserGroupMasterRepository;
import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.repositories.UserRepository;
import com.ticketingSystem.rest.services.MasterRestService;
import com.ticketingSystem.utilities.DateUtilities;



@Service
public class ExpertService {


	
	@Autowired
	MasterRestService defaultRestService;
	
	@Autowired
	ApproverLevelRepository approverlevelrepository;
	
	@Autowired
	ApproverLevelDetailRepository approverleveldetailrepository;
	
	@Autowired
	TicketMasterRepository ticketmasterrepository;
	
	@Autowired
	UserRepository userrepository;
	
	
	@Autowired
	NotificationRegistryImpl notification;

	@Autowired
	HistoryService historyservice;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	SubDomainExpertRepository subdomainexpertdispositioningmasterrepository;
	
	@Autowired
	StatusRepository statusrepository;
	
	@Autowired
	UserProfileRepository userprofilerepository;
	
	@Autowired
	SubDomainRepository subdomainrepository;
	
	@Autowired
	UserGroupMasterRepository usergroupmasterrepository;
	
	@Autowired
	AuthService authService;
	
	private final  Logger logger =LoggerFactory.getLogger(getClass());
	
	
	
	
	public void groupTaskCompleted(DelegateTask task)
	{
		DelegateExecution execution=task.getExecution();
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		ticketmaster.setClaimed(false);
		ticketmaster.setClaimedBy(null);
		ticketmaster.setCurrentGroupMaster(null);
		ticketmaster.setCurrentGroup(false);
		ticketmasterrepository.save(ticketmaster);
	}
	
	
	
	
	
	
	
	public void assignExpert(DelegateTask task)throws ParseException
	{
		DelegateExecution execution=task.getExecution();
		boolean isGroupTask=(boolean)execution.getVariable("isGroup");
		ApproverLevelDetail approverDetail=new ApproverLevelDetail();
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		ticketmaster.setExpertTask(true);
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("date", DateUtilities.getCurrentDateAndTime().toString());
		if(isGroupTask)
		{
		
		/*ticketmaster.setStatus(TicketStatus.AssignedToExpertGroup.getTicketStatus());*/
			hashMap.put("expert",ticketmaster.getCurrentGroupMaster().getGroupName());
			hashMap.put("level",ActorType.ExpertGroup.getActorName());
		approverDetail.setAssignedGroup(ticketmaster.getCurrentGroupMaster());
		
		approverDetail.setApproverType(ActorType.ExpertGroup.getActorName());
		approverDetail.setStatus(statusrepository.findByCode("STS07"));
		ticketmaster.setCurrentStatus(statusrepository.findByCode("STS07").getStatusName());
		}
		else
		{
			
			//ticketmaster.setStatus(TicketStatus.AssignedToExpert.getTicketStatus());
			approverDetail.setAssignedTo(ticketmaster.getCurrentExpert());
			hashMap.put("expert",ticketmaster.getCurrentExpert().getUserName());
			hashMap.put("level",ActorType.Expert.getActorName());
			approverDetail.setApproverType(ActorType.Expert.getActorName());
			approverDetail.setStatus(statusrepository.findByCode("STS09"));
			ticketmaster.setCurrentStatus(statusrepository.findByCode("STS09").getStatusName());
		}	
		ticketmaster.setAssignedOn(DateUtilities.getCurrentDateAndTime());
		approverDetail.setActionDate(DateUtilities.getCurrentDateAndTime());
		approverDetail.setApproverLevel(approverlevelrepository.findById((Integer) execution.getVariable("approverLevelId")));
		approverleveldetailrepository.save(approverDetail);
		ticketmasterrepository.save(ticketmaster);
try {
			
			notification.raisedEvent("Expert Level Status to Requestor",ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void assignedToNextActor(DelegateExecution execution) throws ParseException
	{
		TicketMaster t=ticketmasterrepository.findById((Integer)execution.getVariable("ticketId"));
		String code=(String) execution.getVariable("statusCode");
		ApproverLevelDetail approverDetail=new ApproverLevelDetail();
	Status s=statusrepository.findByCode(code);
	SubDomianExpertDispositionMapping subdomaindispositioning;
	
	if((boolean)execution.getVariable("isGroup"))
	{
		UserGroupMaster usergroup=usergroupmasterrepository.findById((Integer)execution.getVariable("expertGroup"));
		subdomaindispositioning=subdomainexpertdispositioningmasterrepository.findBySubdomainDispositioningAndCurrentAssignedGroupAndCurrentStatus(t.getSubdomain(), usergroup, s);
	}
	else
	{
		UserProfile user=userprofilerepository.findById((Integer)execution.getVariable("expert"));
		subdomaindispositioning=subdomainexpertdispositioningmasterrepository.findBySubdomainDispositioningAndCurrentAssignedUserAndCurrentStatus(t.getSubdomain(), user, s);
	}
	if(subdomaindispositioning.isNextGroup())
	{
		execution.setVariable("expertGroup",subdomaindispositioning.getNextUserGroup().getId());
		
		t.setCurrentGroupMaster(subdomaindispositioning.getNextUserGroup());
		t.setCurrentGroup(true);
		t.setCurrentExpert(null);
		execution.setVariable("isGroup", true);
	}
	else
	{
		execution.setVariable("expert",subdomaindispositioning.getNextUser().getId());
		approverDetail.setAssignedTo(subdomaindispositioning.getNextUser());
		t.setCurrentGroup(false);
		t.setCurrentGroupMaster(null);
		t.setCurrentExpert(subdomaindispositioning.getNextUser());
		execution.setVariable("isGroup", false);
	}
	
	t.setAssignedOn(DateUtilities.getCurrentDateAndTime());
	ticketmasterrepository.save(t);
	}
	
	
	public void actionPerformed(DelegateExecution execution)
	{
		logger.info("action Performed");
		String code=(String) execution.getVariable("statusCode");
		Status s=statusrepository.findByCode(code);
		SubDomianExpertDispositionMapping subdomaindispositioning;
		TicketMaster t=ticketmasterrepository.findById((Integer)execution.getVariable("ticketId"));
		if((boolean)execution.getVariable("isGroup"))
		{
			UserGroupMaster usergroup=usergroupmasterrepository.findById((Integer)execution.getVariable("expertGroup"));
			subdomaindispositioning=subdomainexpertdispositioningmasterrepository.findBySubdomainDispositioningAndCurrentAssignedGroupAndCurrentStatus(t.getSubdomain(), usergroup, s);
			execution.setVariable("isTerminal",(Boolean)subdomaindispositioning.isTerminal() );
		}
		else
		{
			UserProfile user=userprofilerepository.findById((Integer)execution.getVariable("expert"));
			subdomaindispositioning=subdomainexpertdispositioningmasterrepository.findBySubdomainDispositioningAndCurrentAssignedUserAndCurrentStatus(t.getSubdomain(), user, s);
			execution.setVariable("isTerminal",(Boolean)subdomaindispositioning.isTerminal());
		}
		
	}
	
	
	public void notifyRequestor(DelegateExecution execution) throws ParseException
	{
		logger.info("Ticket terminated");
		TicketMaster t=ticketmasterrepository.findById((Integer)execution.getVariable("ticketId"));
		
		
		
		if(execution.getVariable("statusCode").toString().equals("STS02")||execution.getVariable("statusCode").toString().equals("STS10"))
		t.setStatus(TicketStatus.Terminated.getTicketStatus());
		else if(execution.getVariable("statusCode").toString().equals("STS13"))
			t.setStatus(TicketStatus.Closed.getTicketStatus());
		
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+t.getId().toString());
		hashMap.put("status", t.getCurrentStatus());
		hashMap.put("expert", t.getCurrentExpert().getUserName());
		hashMap.put("date", DateUtilities.getCurrentDateAndTime().toString());
try {
			
			notification.raisedEvent("Terminated", t.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
t.setCurrentExpert(null);
t.setCurrentGroupMaster(null);
t.setCurrentGroup(false);
t.setExpertTask(false);
ticketmasterrepository.save(t);
	}
	
	
	
	
	
	
	
	
}
