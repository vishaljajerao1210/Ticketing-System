package com.ticketingSystem.rest.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.dh.notification.channel.ChannelEnum;
import org.dh.notification.connect.NotificationRegistryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ticketingSystem.activitiService.ManagerApprovalService;
import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.ApproverLevel;
import com.ticketingSystem.entities.ApproverLevelDetail;
import com.ticketingSystem.entities.Status;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.entities.User;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.enums.ActorType;
import com.ticketingSystem.enums.TicketAudit;
import com.ticketingSystem.enums.TicketStatus;
import com.ticketingSystem.repositories.ApproverLevelDetailRepository;
import com.ticketingSystem.repositories.ApproverLevelRepository;
import com.ticketingSystem.repositories.CategoryRepository;
import com.ticketingSystem.repositories.DomainFieldRepository;
import com.ticketingSystem.repositories.DomainRepository;
import com.ticketingSystem.repositories.FileItemRepository;
import com.ticketingSystem.repositories.FileUploadRepository;
import com.ticketingSystem.repositories.ListMasterRepository;
import com.ticketingSystem.repositories.ListValuesRepository;
import com.ticketingSystem.repositories.StatusRepository;
import com.ticketingSystem.repositories.SubDomainExpertRepository;
import com.ticketingSystem.repositories.SubDomainFieldRepository;
import com.ticketingSystem.repositories.SubDomainRepository;
import com.ticketingSystem.repositories.TicketDetailRepository;
import com.ticketingSystem.repositories.TicketMasterRepository;
import com.ticketingSystem.repositories.UserGroupMasterRepository;
import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.repositories.UserRepository;
import com.ticketingSystem.utilities.DateUtilities;
import com.ticketingSystem.utilities.JsonPage;
import com.ticketingSystem.utilities.PageUtil;


@Service
public class ActivityRestService {


	@Autowired
	CategoryRepository categoryrepository;

	@Autowired
	DomainRepository domainrepository;

	@Autowired
	SubDomainRepository subdomainrepository;

	@Autowired 
	ManagerApprovalService activityService;

	@Autowired
	SubDomainFieldRepository subdomainfieldrepository;

	@Autowired
	PageUtil pageUtil;

	@Value("${multipart.location}")
	String baseLocation;

	@Autowired
	AuthService authService;


	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Autowired
	FileItemRepository fileitemrepository;

	@Autowired
	FileUploadRepository fileuploadrepository;


	@Autowired
	DomainFieldRepository domainfieldrepository;

	@Autowired
	ApproverLevelDetailRepository approverleveldetailrepository;


	@Autowired
	UserProfileRepository userprofilerepository;


	@Autowired
	ListMasterRepository listmasterrepository;

	@Autowired
	ListValuesRepository listvaluesrepository;


	@Autowired
	ApproverLevelRepository approverlevelrepository;

	@Autowired
	StatusRepository statusrepository;

	@Autowired
	TaskService taskService;


	@Autowired
	RuntimeService runtimeService;

	@Autowired
	HistoryService historyService;




	@Autowired
	SubDomainExpertRepository subdomainexpertrepository;





	@Autowired
	NotificationRegistryImpl notification;


	@Autowired
	TicketDetailRepository ticketdetailrepository;


	@Autowired
	TicketMasterRepository ticketmasterrepository;


	@Autowired
	UserGroupMasterRepository usergroupmasterrepository;

	@Autowired
	UserRepository userrepository;

public Page<TicketMaster>getPersonalTasks(int index,int size, String searchParams)
{
	JsonPage<TicketMaster>  personalTasksPaged = null;
	Date startDate = null;
	Date endDate = null;
	UserProfile assignedTo = null;
	Pageable page = null;
	Page<TicketMaster> personalTasks = null;
	QueryParams queryParams = null;
	String searchString = "";
	try{
		
		assignedTo = userprofilerepository.findByUserId(authService.loggedInUser().getId());
		page = pageUtil.getPageRequest(index, size, Direction.DESC, "raisedDate");
		queryParams = QueryParams.returnObject(searchParams);
		searchString = queryParams.getSearchStr() == null ? "" : queryParams.getSearchStr();
		
		if(queryParams != null && queryParams.getStartDate() != null && queryParams.getEndDate() != null){
			startDate = new Date(queryParams.getStartDate());
			endDate = new Date(queryParams.getEndDate());
			personalTasks = ticketmasterrepository.findPersonalTasksBySearchStringAndAssignedDateAndCurrentExpert(assignedTo,
					searchString, startDate, endDate, page);			
		}
		else
		{	
			personalTasks = ticketmasterrepository.findPersonalTasksBySearchStringAndCurrentExpert(assignedTo, searchString, page);
		}
		personalTasksPaged = new JsonPage<TicketMaster>(personalTasks, page);
	
	} catch(Exception e){
		e.printStackTrace();
	}
	
	return personalTasksPaged;
}


	public void completeTask(Integer id,String comments,String status) throws ParseException
	{

		UserProfile user=userprofilerepository.findByUserId(authService.loggedInUser().getId());
		TicketMaster ticketmaster=ticketmasterrepository.findOne(id);
		String instanceId=ticketmaster.getInstanceId();

		Task task=taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
		if(task!=null)
		{
			//String name=task.getName().replaceAll("\\s","");

			ApproverLevel approver=approverlevelrepository.findByTicketmaster(ticketmaster);

			ApproverLevelDetail approverleveldetail=new ApproverLevelDetail();
			approverleveldetail.setComments(comments);

			approverleveldetail.setApproverLevel(approver);
			if(ActorType.Manager.getActorName().equals(task.getName())||task.getName().equalsIgnoreCase(ActorType.Manager.getActorName()))
			{
				approverleveldetail.setApproverType(ActorType.Manager.getActorName());
				
			}
			else if(ActorType.SpecialManager.getActorName().equals(task.getName())||task.getName().equalsIgnoreCase(ActorType.SpecialManager.getActorName()))
			{
				approverleveldetail.setApproverType(ActorType.SpecialManager.getActorName());
				
			}
			else if(ActorType.Requestor.equals(task.getName())||task.getName().equalsIgnoreCase(ActorType.Requestor.getActorName()))
			{
				approverleveldetail.setApproverType(ActorType.Requestor.getActorName());
				//approverleveldetail.setApprovedBy(user);
			}
			else 
			{
				approverleveldetail.setApproverType(ActorType.Expert.getActorName());


			}
			approverleveldetail.setAssignedTo(user);
			approverleveldetail.setActionDate(DateUtilities.getCurrentDateAndTime());
			Status s=statusrepository.findByCode(status);
			approverleveldetail.setStatus(s);
	
			approverleveldetailrepository.save(approverleveldetail);
			ticketmaster.setCurrentStatus(s.getStatusName());
ticketmasterrepository.save(ticketmaster);
			HashMap<String,Object>taskVariables=new HashMap<String,Object>();
			taskVariables.put("status",s.getStatusName() );
			taskVariables.put("statusCode", s.getCode());
			taskVariables.put("ticketId", id);
			taskService.setVariableLocal(task.getId(), "actionPerformed", s.getStatusName());
			taskService.complete(task.getId(), taskVariables);	
		}
	}


	public List<HashMap<String,Object>>getApproverLevelDetails(Integer id)
	{

		List<HashMap<String,Object>>approvalHistory=new ArrayList<HashMap<String,Object>>();
		List<ApproverLevelDetail> approverLevelDetails=new ArrayList<ApproverLevelDetail>();
		TicketMaster ticketmaster=ticketmasterrepository.findOne(id);
		if(ticketmaster!=null){
			String approverName="";
			String level="";
			String group="";
			String assigneeName="";

			ApproverLevel approverlevel=approverlevelrepository.findByTicketmaster(ticketmaster); 
			approverLevelDetails=approverlevel.getApprovalleveldetail();
			for(ApproverLevelDetail approverleveldetail:approverLevelDetails)
			{
				HashMap<String,Object>params=new HashMap<String,Object>();
				if(approverleveldetail.getApprovedBy()!=null)
					approverName=approverleveldetail.getApprovedBy().getUserName();


				if(approverleveldetail.getApproverType().toString()!=null)
					level=approverleveldetail.getApproverType().toString();

				if(approverleveldetail.getAssignedGroup()!=null)
					group=approverleveldetail.getAssignedGroup().getGroupName();

				if(approverleveldetail.getAssignedTo()!=null)
					assigneeName=approverleveldetail.getAssignedTo().getUserName();

				
				String statement=TicketAudit.valueOf(approverleveldetail.getStatus().getCode()).returnStatus(approverName,group,assigneeName, level,approverleveldetail.getStatus());
				params.put("code", approverleveldetail.getStatus().getCode());
				params.put("date", approverleveldetail.getActionDate());
				params.put("statement", statement);
				approvalHistory.add(params);
			}

		}

		return approvalHistory;

	}


	public Page<TicketMaster> getGroupTasks(int index,int size,int groupId, String searchParams)
	{
		JsonPage<TicketMaster>  groupTasksPaged = null;
		Date startDate = null;
		Date endDate = null;
		Pageable page = null;
		Page<TicketMaster> groupTasks = null;
		QueryParams queryParams = null;
		String searchString = "";
		boolean isCurrentGroup = true;
		boolean isClaimed = false;
		try{
			
			page = pageUtil.getPageRequest(index, size, Direction.DESC, "raisedDate");
			queryParams = QueryParams.returnObject(searchParams);
			searchString = queryParams.getSearchStr() == null ? "" : queryParams.getSearchStr();
			
			if(queryParams != null && queryParams.getStartDate() != null && queryParams.getEndDate() != null){
				startDate = new Date(queryParams.getStartDate());
				endDate = new Date(queryParams.getEndDate());
				groupTasks = ticketmasterrepository.findGroupTasksBySearchStringAndAssignedDateAndCurrentGroup(isCurrentGroup, isClaimed,
						groupId, searchString, startDate, endDate, page);			
			}
			else
			{	
				groupTasks = ticketmasterrepository.findGroupTasksBySearchStringAndCurrentGroup(isCurrentGroup, isClaimed, groupId,
						searchString, page);
			}
			groupTasksPaged = new JsonPage<TicketMaster>(groupTasks, page);
		
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return groupTasksPaged;
	}
	
	
	public Page<TicketMaster> getGroupTasks()
	{
		

		UserProfile user=userprofilerepository.findByUserId(authService.loggedInUser().getId());

		if(user!=null)
			{
			
			List<UserGroupMaster>groupMasters=user.getUsergroupmaster();
			return new JsonPage<TicketMaster>(ticketmasterrepository.findTop10ByCurrentGroupMasterInAndIsClaimedOrderByAssignedOnDesc(groupMasters,false,pageUtil.getPageRequest(0, 2,Sort.Direction.DESC,"assignedOn")),pageUtil.getPageRequest(0, 2,Sort.Direction.DESC,"assignedOn"));
			}
		return null;
		}
		
	

	public void revokeTask(int id,String code,String comments)throws ParseException
	{
		User u=authService.loggedInUser();
		UserProfile user=userprofilerepository.findById(u.getId());
		ApproverLevelDetail approverleveldetail=new ApproverLevelDetail();
		TicketMaster ticketmaster=ticketmasterrepository.findById(id);
		ticketmaster.setClaimed(false);
		ticketmaster.setClaimedBy(null);
		ticketmaster.setCurrentExpert(null);

		Task task=taskService.createTaskQuery().processInstanceId(ticketmaster.getInstanceId()).singleResult();
		task.setAssignee(null);
		taskService.saveTask(task);

		approverleveldetail.setActionDate(DateUtilities.getCurrentDateAndTime());
		approverleveldetail.setAssignedTo(user);
		approverleveldetail.setAssignedGroup(ticketmaster.getCurrentGroupMaster());
		approverleveldetail.setStatus(statusrepository.findByCode(code));
		approverleveldetail.setApproverType(ActorType.Expert.getActorName());
		approverleveldetail.setComments(comments);
		ApproverLevel approverlevel=approverlevelrepository.findByTicketmaster(ticketmaster);
		approverleveldetail.setApproverLevel(approverlevel);
		approverleveldetailrepository.save(approverleveldetail);
		ticketmasterrepository.save(ticketmaster);
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("date",DateUtilities.getCurrentDate().toString());
		hashMap.put("expert", user.getUserName());
		hashMap.put("expertGroup",ticketmaster.getCurrentGroupMaster().getGroupName());
		try {

			notification.raisedEvent("Revoked Ticket", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void claimTask(int id,String code,String comments)throws ParseException
	{
		User u=authService.loggedInUser();
		UserProfile user=userprofilerepository.findById(u.getId());
		ApproverLevelDetail approverleveldetail=new ApproverLevelDetail();
		TicketMaster ticketmaster=ticketmasterrepository.findById(id);
		ticketmaster.setClaimed(true);
		ticketmaster.setClaimedBy(user);
		ticketmaster.setCurrentExpert(user);

		Task task=taskService.createTaskQuery().processInstanceId(ticketmaster.getInstanceId()).singleResult();
		task.setAssignee(user.getId().toString());
		taskService.saveTask(task);

		approverleveldetail.setActionDate(DateUtilities.getCurrentDateAndTime());
		approverleveldetail.setAssignedTo(user);
		approverleveldetail.setStatus(statusrepository.findByCode(code));
		approverleveldetail.setApproverType(ActorType.Expert.getActorName());
		approverleveldetail.setComments(comments);
		ApproverLevel approverlevel=approverlevelrepository.findByTicketmaster(ticketmaster);
		approverleveldetail.setApproverLevel(approverlevel);
		approverleveldetailrepository.save(approverleveldetail);
		ticketmasterrepository.save(ticketmaster);
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("Id", "#"+ticketmaster.getId().toString());
		hashMap.put("date",DateUtilities.getCurrentDate().toString());
		hashMap.put("expert", ticketmaster.getClaimedBy().getUserName());
		try {

			notification.raisedEvent("Claimed Ticket", ticketmaster.getRaisedBy().getEmail(), ChannelEnum.EMAIL,hashMap);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public List<SubDomianExpertDispositionMapping> getExpertOptions(int id)
	{

		TicketMaster ticketmaster=ticketmasterrepository.findById(id);
		List<SubDomianExpertDispositionMapping>subdomainexpertoptions;
		User u=authService.loggedInUser();
		UserProfile user=userprofilerepository.findById(u.getId());

		if(ticketmaster.isCurrentGroup())
		{
			subdomainexpertoptions=subdomainexpertrepository.findBySubdomainDispositioningAndCurrentAssignedGroup(ticketmaster.getSubdomain(),ticketmaster.getCurrentGroupMaster());
		}
		else
		{
			subdomainexpertoptions=subdomainexpertrepository.findBySubdomainDispositioningAndCurrentAssignedUser(ticketmaster.getSubdomain(),user);
		}
		return subdomainexpertoptions;
	}




	public JsonPage<CompletedTask> getCompletedTasks(int index,int size, String searchParams){

		User user=authService.loggedInUser();
		List<HistoricTaskInstance> historyTasks = new ArrayList<>();
		List<CompletedTask> completedTasks = new ArrayList<>();
		PageRequest pagerequest=new PageRequest(index, size,Sort.Direction.DESC,"assignedOn");
		long count = 0L;
		String searchString = "";
		QueryParams queryParams = null;
		HistoricTaskInstanceQuery historyTaskQuery =null;
		try{
			
			queryParams = QueryParams.returnObject(searchParams);
			searchString = queryParams.getSearchStr() == null ? "" : queryParams.getSearchStr();
			historyTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(String.valueOf(user.getId())).includeProcessVariables().includeTaskLocalVariables().or()
					.taskVariableValueLikeIgnoreCase("actionPerformed", "%" +searchString+ "%").processVariableValueLikeIgnoreCase("ticketIdStr", "%" +searchString+ "%")
					.processVariableValueLikeIgnoreCase("category", "%" +searchString+ "%").processVariableValueLikeIgnoreCase("requestor", "%" +searchString+ "%")
					.endOr();
					
			if(queryParams.getStartDate() != null && queryParams.getEndDate() !=  null){
			historyTaskQuery = historyTaskQuery.taskCompletedAfter(new Date(queryParams.getStartDate())).taskCompletedBefore(new Date(queryParams.getEndDate()));
					}
					 
			historyTasks = historyTaskQuery.finished().orderByHistoricTaskInstanceEndTime().desc().listPage(index*10, size);
			count =  historyTaskQuery.finished().list().size();
			for(HistoricTaskInstance historyTask : historyTasks){
				CompletedTask completedTask = new CompletedTask();
				TicketMaster ticket = ticketmasterrepository.findByInstanceId(historyTask.getProcessInstanceId());
				completedTask.setTicketMaster(ticket);
				completedTask.setActionPerformed(historyTask.getTaskLocalVariables().get("actionPerformed").toString());
				completedTask.setEndDate(historyTask.getEndTime());
				completedTask.setApproverLevel(historyTask.getName());
				completedTasks.add(completedTask);
				}

		}catch (Exception e){

			e.printStackTrace();	
		}
		return new JsonPage<CompletedTask>(completedTasks, pagerequest,count);
		
	}




	public Map<String, Integer> getTaskCountForUser(){
		User loggedInUser = null;
		List<UserGroupMaster> userGroups = null;
		Map<String, Integer> taskCount = new HashMap<>();

		try{
			loggedInUser = authService.loggedInUser();
			userGroups = usergroupmasterrepository.findByUsersId(loggedInUser.getId());
			for(UserGroupMaster userGroup : userGroups){	

				Integer groupCount = ticketmasterrepository.countByIsCurrentGroupAndIsClaimedAndCurrentGroupMaster_id(true, false, userGroup.getId());
				taskCount.put(userGroup.getGroupName(), groupCount);

			}

		} catch(Exception e){
			e.printStackTrace();
		}
		return taskCount;
	}





	public Map<String,Long> getCountForDashboard(){
		Map<String, Long> taskCount = new HashMap<>();
		User loggedInUser = null;
		List<UserGroupMaster> userGroups = new ArrayList<>();
		Long pendingGroupTasks = 0L;
		Long pendingTasks = 0L;
		Long pendingTickets = 0L;
		Long resolvedTickets = 0L;
		try{
			loggedInUser = authService.loggedInUser();
			userGroups = usergroupmasterrepository.findByUsersId(loggedInUser.getId());
			pendingTasks = taskService.createTaskQuery().taskAssignee(loggedInUser.getId().toString()).count();
			Long completedTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(String.valueOf(loggedInUser.getId())).finished().count();

			for(UserGroupMaster userGroup : userGroups){
				pendingGroupTasks = pendingGroupTasks + taskService.createTaskQuery().taskCandidateGroup(userGroup.getId().toString()).active().count();	
			}

			pendingTickets = ticketmasterrepository.countByRaisedBy_idAndCurrentStatus(loggedInUser.getId(), TicketStatus.Pending.getTicketStatus());
			resolvedTickets = ticketmasterrepository.countByRaisedBy_idAndCurrentStatus(loggedInUser.getId(), TicketStatus.Resolved.getTicketStatus());

			taskCount.put("pendingTasks", pendingTasks);
			taskCount.put("completedTasks", completedTasks);
			taskCount.put("pendingTickets", pendingTickets);
			taskCount.put("resolvedTickets", resolvedTickets);
			taskCount.put("unclaimedTasks", pendingGroupTasks);
		} catch(Exception e){
			e.printStackTrace();
		}
		return taskCount;

	}





	public List<ApproverLevelDetail> getTaskComments(Integer ticketId) {
		return approverleveldetailrepository.findByApproverLevel_ticketmaster_idAndCommentsIsNotNullAndCommentsNot(ticketId , "");
	}


}
