package com.ticketingSystem.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.entities.ApproverLevelDetail;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.repositories.ApproverLevelDetailRepository;
import com.ticketingSystem.repositories.ApproverLevelRepository;
import com.ticketingSystem.repositories.DomainFieldRepository;
import com.ticketingSystem.repositories.DomainRepository;
import com.ticketingSystem.repositories.FileItemRepository;
import com.ticketingSystem.repositories.FileUploadRepository;
import com.ticketingSystem.repositories.StatusRepository;
import com.ticketingSystem.repositories.SubDomainExpertRepository;
import com.ticketingSystem.repositories.SubDomainFieldRepository;
import com.ticketingSystem.repositories.SubDomainRepository;
import com.ticketingSystem.repositories.TicketDetailRepository;
import com.ticketingSystem.repositories.TicketMasterRepository;
import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.repositories.UserRepository;
import com.ticketingSystem.rest.services.ActivityRestService;
import com.ticketingSystem.rest.services.CompletedTask;
import com.ticketingSystem.rest.services.MasterRestService;
import com.ticketingSystem.utilities.JsonPage;

@RestController
public class ActivityController {

	@Autowired
	MasterRestService defaultrestservice;

	@Value("${multipart.location}")
	String baseLocation;

	@Autowired
	DomainFieldRepository domainfieldrepository;

	@Autowired
	TaskService taskService;
	

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	StatusRepository statusrepository;
	
	
	
	@Autowired
	FileItemRepository fileitemrepository;

	@Autowired
	FileUploadRepository fileuploadrepository;

	
	@Autowired
	ApproverLevelRepository approverlevelrepository;
	
	
	@Autowired
	ApproverLevelDetailRepository approverleveldetailrepository;
	
	@Autowired
	HttpServletRequest request;

	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	SubDomainExpertRepository subdomainexpertrepository;

	@Autowired
	SubDomainFieldRepository subdomainfieldrepository;

	@Autowired
	SubDomainRepository subdomainrepository;

	@Autowired
	TicketDetailRepository ticketdetailrepository;

	@Autowired
	TicketMasterRepository ticketmasterrepository;

	@Autowired
	DomainRepository domainrepository;

	@Autowired
	UserRepository userrepository;

	@Autowired
	UserProfileRepository userprofilerepository;
	
	@Autowired
	ActivityRestService activityrestservice;
	
	@JsonView(MasterView.TicketMasterView.class)
	@RequestMapping("/user/getPersonalTasks")
	public Page<TicketMaster>getApprovalTasksAssigned(@RequestParam("PI")int index,@RequestParam("PG")int size, @RequestParam("searchParams") String searchParams)
	{
	
		return activityrestservice.getPersonalTasks(index,size, searchParams);
	}
	
	
	
	
	@Transactional
	@RequestMapping("/public/CompleteTask")
	public void completeTask(@RequestParam("id")Integer id,@RequestParam("comments")String comments,@RequestParam("statusCode")String status) throws ParseException
	{
		
		activityrestservice.completeTask(id, comments, status);
		
	}

	@JsonView(MasterView.ApproverDetailsView.class)
	@RequestMapping("/public/getApprovalHistory")
	public List<HashMap<String,Object>> getApproverLevelDetails(@RequestParam("id")Integer id)
	{
		return activityrestservice.getApproverLevelDetails(id);		
	}
	
	@JsonView(MasterView.TicketMasterView.class)
	@RequestMapping("/user/userGroupTasks")
	public Page<TicketMaster> getGroupTasks(@RequestParam("PI")int pageIndex,@RequestParam("PG")int pageSize,@RequestParam("paramsId")int id, @RequestParam("searchParams") String searchParams)
	{
		return activityrestservice.getGroupTasks(pageIndex,pageSize,id, searchParams);
		
	}
	@JsonView(MasterView.TicketMasterView.class)
	@RequestMapping("/user/getAllGroupTasks")
	public Page<TicketMaster> getAllGroupTasks()
	{
		return activityrestservice.getGroupTasks();
		
	}
	
	@RequestMapping("user/revokeTask/")
	public void revokeTask(@RequestParam("id")int id,@RequestParam("status")String code,@RequestParam("comments")String comments) throws ParseException
	{
		activityrestservice.revokeTask(id, code, comments);
		
	}
		
	@RequestMapping("user/claimTask")
	public void claimTask(@RequestParam("id")int id,@RequestParam("status")String code,@RequestParam("comments")String comments) throws ParseException
	{
		activityrestservice.claimTask(id, code, comments);
	
	}
	
	@JsonView(MasterView.SubDomainExpertView.class)
	@RequestMapping("user/getExpertOptions/")
	public List<SubDomianExpertDispositionMapping> getOptions(@RequestParam(value="id")int id)
	{
		return activityrestservice.getExpertOptions(id);
	}
	
	
	@JsonView(MasterView.TicketMasterView.class)
	@RequestMapping("/user/getCompletedTasks")
	public JsonPage<CompletedTask> getCompletedTasks(@RequestParam("PI")int index,@RequestParam("PG")int size, @RequestParam("searchParams") String searchParams){
		return activityrestservice.getCompletedTasks(index,size, searchParams);
	}
	
	@RequestMapping("/user/getTaskCountForUser")
	public Map<String,Integer> getTaskCountForUser(){
		return activityrestservice.getTaskCountForUser();
	}
	
	@JsonView(MasterView.ApproverDetailsView.class)
	@RequestMapping("/user/getTaskComments")
	public List<ApproverLevelDetail> getTaskComments(@RequestParam("ticketId")Integer ticketId){
		return activityrestservice.getTaskComments(ticketId);
		
	}
	
	@RequestMapping("/public/dashboardStats")
	public Map<String, Long> getCountForDashboard(){
		return activityrestservice.getCountForDashboard();
	}
	


	
	
	
}
