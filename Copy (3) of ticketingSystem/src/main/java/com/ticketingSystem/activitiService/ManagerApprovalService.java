package com.ticketingSystem.activitiService;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.dh.notification.channel.ChannelEnum;
import org.dh.notification.connect.NotificationRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketingSystem.Constants.URLConstants;
import com.ticketingSystem.entities.ApproverLevel;
import com.ticketingSystem.entities.ApproverLevelDetail;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.enums.ActorType;
import com.ticketingSystem.enums.TicketStatus;
import com.ticketingSystem.repositories.ApproverLevelDetailRepository;
import com.ticketingSystem.repositories.ApproverLevelRepository;
import com.ticketingSystem.repositories.StatusRepository;
import com.ticketingSystem.repositories.TicketMasterRepository;
import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.repositories.UserRepository;
import com.ticketingSystem.rest.services.MasterRestService;
import com.ticketingSystem.utilities.DateUtilities;





@Service
public class ManagerApprovalService {

	
	@Autowired
	TaskService taskService;
	
	@Autowired
	MasterRestService defaultRestService;
	
	@Autowired
	ApproverLevelRepository approverlevelrepository;
	
	@Autowired
	URLConstants url;
	
	@Autowired
	ApproverLevelDetailRepository approverleveldetailrepository;
	
	@Autowired
	TicketMasterRepository ticketmasterrepository;
	
	@Autowired
	UserRepository userrepository;
	
	
	@Autowired
	NotificationRegistryImpl notification;
	
@Autowired
UserProfileRepository userprofilerepository;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	StatusRepository statusrepository;
	
	private final  Logger logger =LoggerFactory.getLogger(getClass());
	
	public void startManagerApprovalProcess(String name,Map<String,Object>parameters)
	{
		runtimeService.startProcessInstanceByKey(name, parameters);
	}
	
	@Transactional
	public void ticketRaised(DelegateExecution execution) throws ParseException
	{
		//System.out.println("Ticket Raised");
		logger.info("Ticket Raised");
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer)execution.getVariable("ticketId"));
		String instanceId=execution.getProcessInstanceId();
		ticketmaster.setInstanceId(instanceId);
	
		ApproverLevel approver=new ApproverLevel();
			approver.setTicketmaster(ticketmaster);
			approver.setCreatedDate(DateUtilities.getCurrentDateAndTime());
			approverlevelrepository.save(approver);
			
			
			execution.setVariable("approverLevelId", approver.getId());
			execution.setVariable("statusCode","STS14");
			
			
			HashMap<String,Object> hashMap=new HashMap<>();
			
			boolean managerApproval=(boolean)execution.getVariable("isManagerApprovalRequired");
			boolean specialManagerApproval=(boolean)execution.getVariable("isSpecialManagerApprovalRequired");
			hashMap.put("Id", "#"+ticketmaster.getId().toString());
		if(managerApproval&&!specialManagerApproval||managerApproval&&specialManagerApproval)
		{
			hashMap.put("manager_name",ticketmaster.getRaisedBy().getManager().getUserName());
			execution.setVariable("managerFlag", false);
		}
		else if(!managerApproval&&specialManagerApproval)
		{
			hashMap.put("manager_name",ticketmaster.getRaisedBy().getSpecialManager().getUserName());
			execution.setVariable("managerFlag", true);
		}
			
			
			
			try {
				notification.raisedEvent("Ticket Raised", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	
	public void managerTaskCreated(DelegateTask task) throws ParseException
	{
		DelegateExecution execution=task.getExecution();
		
		
		ApproverLevelDetail approverDetail=new ApproverLevelDetail();
		approverDetail.setAssignedTo(userprofilerepository.findById((Integer) execution.getVariable("manager")));
		approverDetail.setApproverType(ActorType.Manager.getActorName());
		approverDetail.setActionDate(DateUtilities.getCurrentDateAndTime());
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		/*ticketmaster.setStatus(TicketStatus.SentForManagerApproval.getTicketStatus());*/
		ticketmaster.setAssignedOn(DateUtilities.getCurrentDateAndTime());
		ticketmaster.setCurrentExpert(userprofilerepository.findById((Integer)execution.getVariable("manager")));
		ticketmaster.setCurrentStatus(statusrepository.findByCode("STS05").getStatusName());
		ticketmasterrepository.save(ticketmaster);
		
		approverDetail.setStatus(statusrepository.findByCode("STS05"));
		
		approverDetail.setApproverLevel(approverlevelrepository.findById((Integer) execution.getVariable("approverLevelId")));
		approverleveldetailrepository.save(approverDetail);
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("requestor",ticketmaster.getRaisedBy().getUserName());
		
		hashMap.put("link", "http://192.168.10.22:8084/#/personaltasks");
		try {
			if(!execution.getVariable("statusCode").equals("STS04"))
			{
				hashMap.put("date", ticketmaster.getRaisedDate().toString());
			notification.raisedEvent("Manager Task Created", ticketmaster.getRaisedBy().getManager().getEmail(), ChannelEnum.EMAIL,hashMap);
			}
			else
			{
				hashMap.put("date", DateUtilities.getCurrentDateAndTime().toString());
			notification.raisedEvent("Clarified By Requestor", ticketmaster.getRaisedBy().getManager().getEmail(), ChannelEnum.EMAIL, hashMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void specialManagerTaskCreated(DelegateTask task) throws ParseException
	{
		DelegateExecution execution=task.getExecution();
		
		
		ApproverLevelDetail approverDetail=new ApproverLevelDetail();
		
		
			approverDetail.setAssignedTo(userprofilerepository.findById((Integer) execution.getVariable("specialManager")));
			approverDetail.setApproverType(ActorType.SpecialManager.getActorName());
			
			TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
			//ticketmaster.setStatus(execution.getVariable("status").toString());
			ticketmaster.setCurrentStatus(statusrepository.findByCode("STS06").getStatusName());
			ticketmaster.setAssignedOn(DateUtilities.getCurrentDateAndTime());
			ticketmaster.setCurrentExpert(userprofilerepository.findById((Integer)execution.getVariable("specialManager")));
			ticketmasterrepository.save(ticketmaster);
		approverDetail.setActionDate(DateUtilities.getCurrentDateAndTime());
		approverDetail.setStatus(statusrepository.findByCode("STS06"));
		approverDetail.setApproverLevel(approverlevelrepository.findById((Integer) execution.getVariable("approverLevelId")));
		approverleveldetailrepository.save(approverDetail);
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("requestor",ticketmaster.getRaisedBy().getUserName());
	
		try {
			if(!execution.getVariable("statusCode").equals("STS04")&&(boolean)execution.getVariable("managerFlag"))
			{
				hashMap.put("date", ticketmaster.getRaisedDate().toString());
			notification.raisedEvent("Manager Task Created", ticketmaster.getRaisedBy().getSpecialManager().getEmail(), ChannelEnum.EMAIL,hashMap);
			}
			else
			{
				hashMap.put("date", DateUtilities.getCurrentDateAndTime().toString());
			notification.raisedEvent("Clarified By Requestor", ticketmaster.getRaisedBy().getSpecialManager().getEmail(), ChannelEnum.EMAIL, hashMap);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void calledListener(DelegateExecution execution)
	{
		logger.info("CALLED");
	}
	
	public void completedManagerTask(DelegateTask task)
	{
		
		logger.info("task completed");
		DelegateExecution execution=task.getExecution();
		HashMap<String,Object> hashMap=new HashMap<>();
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		try
		{
		
		
			//ticketmaster.setStatus(execution.getVariable("status").toString());
			ticketmaster.setCurrentExpert(null);
		ticketmasterrepository.save(ticketmaster);
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("manager",ticketmaster.getRaisedBy().getManager().getUserName());
		hashMap.put("level",ActorType.Manager.getActorName());
		hashMap.put("date", DateUtilities.getCurrentDateAndTime().toString());
		hashMap.put("status",execution.getVariable("status").toString());
		
		if(execution.getVariable("statusCode").equals("STS01")||execution.getVariable("statusCode").equals("STS02"))
			notification.raisedEvent("Approved Or Rejected", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
		else
			notification.raisedEvent("Sent For Clarification", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL, hashMap);
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void completeSpecialManagerTask(DelegateTask task)
	{
		logger.info("task completed");
		DelegateExecution execution=task.getExecution();
		
		
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		HashMap<String,Object> hashMap=new HashMap<>();
		try
		{
		//ticketmaster.setStatus(execution.getVariable("status").toString());
		ticketmaster.setCurrentExpert(null);
		ticketmasterrepository.save(ticketmaster);
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("manager",ticketmaster.getRaisedBy().getSpecialManager().getUserName());
		hashMap.put("level",ActorType.SpecialManager.getActorName());
		
		
		hashMap.put("date",DateUtilities.getCurrentDateAndTime().toString());
		hashMap.put("status",execution.getVariable("status").toString());
		
		if(execution.getVariable("statusCode").equals("STS01")||execution.getVariable("statusCode").equals("STS02"))
			notification.raisedEvent("Approved Or Rejected", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
		else
			notification.raisedEvent("Sent For Clarification", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL, hashMap);
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public void requestTaskAssigned(DelegateTask task) throws ParseException
	{
		DelegateExecution execution=task.getExecution();
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer) execution.getVariable("ticketId"));
		if(execution.getVariable("statusCode").equals("STS03"))
		{
			ticketmaster.setCurrentExpert(userprofilerepository.findById((Integer)execution.getVariable("requestorId")));
			ticketmaster.setAssignedOn(DateUtilities.getCurrentDateAndTime());
		}
		else if(execution.getVariable("statusCode").equals("STS04"))
			ticketmaster.setCurrentExpert(null);
		
		ticketmasterrepository.save(ticketmaster);
		
	}
	
	
	
	
	
	public void endResult(DelegateExecution execution)
	{
		logger.info("final status"+execution.getVariable("status"));
	
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer)execution.getVariable("ticketId"));
		
		ticketmaster.setCurrentExpert(null);
		ticketmaster.setCurrentGroup(false);
	
		ticketmaster.setStatus(TicketStatus.Terminated.getTicketStatus());
		ticketmasterrepository.save(ticketmaster);
	}
	
	
	public void requestedInformation(Map<String,Object>variables)
	{
		
		this.startManagerApprovalProcess("ticketingProcess", variables);
	}
	
	
	
	public void assignTaskToExpert(DelegateExecution execution) throws ParseException
	{
		
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer)execution.getVariable("ticketId"));
		
		boolean isGroupTask=(boolean)execution.getVariable("isGroup");
		ticketmaster.setExpertTask(true);
		if(isGroupTask)
		{
			ticketmaster.setCurrentGroupMaster(ticketmaster.getSubdomain().getDefaultUserGroup());
		
			ticketmaster.setCurrentGroup(true);
		}
		else
		{
			ticketmaster.setCurrentExpert(ticketmaster.getSubdomain().getDefaultExpert());
			
			ticketmaster.setCurrentGroup(false);
		}
		ticketmasterrepository.save(ticketmaster);
		
		
		
	}
	
	
	public void decisionEndPoint(DelegateExecution execution)
	{
		TicketMaster ticketmaster=ticketmasterrepository.findOne((Integer)execution.getVariable("ticketId"));
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("manager",ticketmaster.getRaisedBy().getManager().getUserName());
		hashMap.put("date", ticketmaster.getRaisedDate().toString());
		hashMap.put("requestor",ticketmaster.getRaisedBy().getUserName());
		hashMap.put("specialManager", ticketmaster.getRaisedBy().getSpecialManager().getUserName());
			try {
				if((boolean)execution.getVariable("isSpecialManagerApprovalRequired"))
				{
					/*ticketmaster.setStatus(TicketStatus.SentForSpecialManagerApproval.getTicketStatus());
					ticketmasterrepository.save(ticketmaster);*/
					execution.setVariable("status", statusrepository.findByCode("STS06").getStatusName());
					execution.setVariable("managerFlag", true);
				notification.raisedEvent("Approver level Status to Requestor", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);
				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	

}
