package com.ticketingSystem.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.Category;
import com.ticketingSystem.entities.Domain;
import com.ticketingSystem.entities.DomainField;
import com.ticketingSystem.entities.ListMaster;
import com.ticketingSystem.entities.ListValue;
import com.ticketingSystem.entities.Status;
import com.ticketingSystem.entities.SubDomain;
import com.ticketingSystem.entities.SubDomainField;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.User;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.repositories.ApproverLevelDetailRepository;
import com.ticketingSystem.repositories.ApproverLevelRepository;
import com.ticketingSystem.repositories.CategoryRepository;
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
import com.ticketingSystem.repositories.UserGroupMasterRepository;
import com.ticketingSystem.repositories.UserProfileRepository;
import com.ticketingSystem.repositories.UserRepository;
import com.ticketingSystem.rest.services.MasterRestService;


@RestController
public class MasterController {

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
	AuthService authService;
	
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
UserGroupMasterRepository usergroupmasterrepository;
	
	@Autowired
	CategoryRepository categoryrepository;

	@RequestMapping("/user")
	public Principal getUser(Principal principal) {
		return principal;
	}

	
	@JsonView(MasterView.CategoryView.class)
	@RequestMapping("public/categories")
	public Page<Category> getAllCategories(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		/* returns a list of categories */
		return defaultrestservice.getCategories(index,size,searchParam);
	}
	
	
	@JsonView(MasterView.SubDomainExpertView.class)
	@RequestMapping("public/getDispositionings")
	public Page<SubDomianExpertDispositionMapping>getDispositionings(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("subDomainId")Integer subDomainId)
	{
		return defaultrestservice.fetchDispositionsBySubDomain(subDomainId, index, size);
	}
	
	@JsonView(MasterView.UserGroupView.class)
	@RequestMapping("user/getGroups")
	public List<UserGroupMaster>getLoggedInUserGroups()
	{
		return usergroupmasterrepository.findByUsersId(authService.loggedInUser().getId());
	}
	
	
	
	

	@RequestMapping("admin/addCategory")
	public HashMap<String, String> saveCategory(@RequestBody Category categories) {
		/* saves a category object */

		return defaultrestservice.saveCategory(categories);
	}

	@JsonView(MasterView.CategoryView.class)
	@RequestMapping("public/getCategoryById/{id}")
	public Category getCategoryById(@PathVariable("id") int id) {
		/* returns a particular category by its id */
		return defaultrestservice.getCategory(id);
	}

	@JsonView(MasterView.DomainView.class)
	@RequestMapping("public/domains")
	public Page<Domain> getDomains(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		/* returns a list of domains */
		return defaultrestservice.getDomains(index,size,searchParam);
	}

	@JsonView(MasterView.DomainView.class)
	@RequestMapping("public/getDomainById/{id}")
	public Domain getDomainById(@PathVariable("id") int id) {
		/* returns a particular domain by its id */
		return defaultrestservice.getDomain(id);
	}
	
	@JsonView(MasterView.StatusView.class)
	@RequestMapping("public/getStatusMaster")
	public List<Status>getStatusMaster()
	{
		return statusrepository.findAll();
	}
	
	
	
	
	

	@RequestMapping("admin/addDomain")
	public HashMap<String, String> saveDomain(@RequestBody Domain domains, @RequestParam("id") Integer id) {
		/* saves a Domains object */
		return defaultrestservice.saveDomain(domains, id);
	}

	@RequestMapping("public/subdomains")
	@JsonView(MasterView.SubDomainView.class)
	public Page<SubDomain> getAllSubDomains(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		/* returns all subdomains */
		return defaultrestservice.getSubDomains(index,size,searchParam);

	}

	@JsonView(MasterView.SubDomainView.class)
	@RequestMapping("public/getSubDomainById/{id}")
	public SubDomain getSubDomainById(@PathVariable("id") int id) {
		/* returns a particular subdomain by its id */
		return defaultrestservice.getSubDomain(id);
	}

	@RequestMapping("admin/addSubDomain")
	public HashMap<String, String> saveSubDomain(@RequestBody SubDomain subdomains,
			@RequestParam("id") Integer id,@RequestParam("groupId")Integer groupId,@RequestParam("actorType")String actorType) {
		/* saves a SubDomains object */
		return defaultrestservice.saveSubDomain(subdomains, id,groupId,actorType);
	}

	@RequestMapping("admin/checkCategory")
	public HashMap<Object, Object> checkIfUniqueCategory(@RequestParam("category") String category) {
		/* checks whether category unique or not */
		return defaultrestservice.checkIfCategoryIsUnique(category);
	}

	@RequestMapping("admin/checkDomain")
	public boolean checkIfUniqueDomain(@RequestParam("domain") String domain, @RequestParam("categoryId") Integer categoryId) {
		/* checks whether Domain unique or not */
		return defaultrestservice.checkIfDomainIsUnique(domain, categoryId);
	}

	@RequestMapping("admin/checkSubDomain")
	public boolean checkIfUniqueSubDomain(@RequestParam("subdomain") String subdomain) {
		/* checks whether SubDomain unique or not */
		return defaultrestservice.checkIfDomainIsUnique(subdomain, 0);
	}

	@RequestMapping("admin/addDomainFields")
	public HashMap<String, String> saveDomainFields(@RequestBody DomainField domainfields,
			@RequestParam("id") Integer id, @RequestParam("listId") Integer listId) {
		/* saves a fields for domains object */
		return defaultrestservice.saveFieldsForDomain(domainfields, id, listId);
	}

	@JsonView(MasterView.DomainFieldView.class)
	@RequestMapping("public/getFieldsOfAllDomains")
	public Page<DomainField> getAllDomainFields(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		return defaultrestservice.getAllDomainfields(index,size,searchParam);
	}

	@JsonView(MasterView.DomainFieldView.class)
	@RequestMapping("public/getFieldOfDomain/{id}")
	public DomainField getDomainFieldById(@PathVariable("id") int id)

	{
		return defaultrestservice.getDomainFieldById(id);
	}

	@RequestMapping("/admin/addListMasters")
	public HashMap<String, String> saveDomainListMaster(@RequestBody ListMaster listmasters) {
		return defaultrestservice.saveListMasters(listmasters);
	}

	@JsonView(MasterView.ListMasterView.class)
	@RequestMapping("/public/getAllListMasters")
	public Page<ListMaster> getAllListMasters(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		return defaultrestservice.getAllListMasters(index,size,searchParam);
	}

	@JsonView(MasterView.ListMasterView.class)
	@RequestMapping("public/getListMasterById/{id}")
	public ListMaster getListMaster(@PathVariable("id") int id) {
		return defaultrestservice.getListMasterById(id);
	}

	@RequestMapping("/admin/addListValues")
	public HashMap<String, String> saveDomainListMaster(@RequestBody ListValue listvalues,
			@RequestParam("id") Integer id) {
		return defaultrestservice.saveListValues(listvalues, id);
	}

	@JsonView(MasterView.ListValueView.class)
	@RequestMapping("/public/getListValuesByListId")
	public List<ListValue> getListValuesByListId(@RequestParam("listId")int listId, @RequestParam("searchParam")String searchParam) {
		return defaultrestservice.getAllListValues(listId, searchParam);
	}

	@JsonView(MasterView.ListValueView.class)
	@RequestMapping("/public/getListValueById/{id}")
	public ListValue getAllListValues(@PathVariable("id") int id) {
		return defaultrestservice.getListValueById(id);
	}

	@JsonView(MasterView.SubDomainFieldView.class)
	@RequestMapping("public/getFieldsOfAllSubDomains")
	public Page<SubDomainField> getAllSubDomainFields(@RequestParam("PI")int index,@RequestParam("PG")int size,@RequestParam("searchParam")String searchParam) {
		return defaultrestservice.getAllSubDomainfields(index,size,searchParam);
	}

	@RequestMapping("admin/addSubDomainFields")
	public HashMap<String, String> saveSubDomainFields(@RequestBody SubDomainField subdomainfield,
			@RequestParam("id") Integer id, @RequestParam("listId") Integer listId) {
		/* saves a fields for domains object */
		return defaultrestservice.saveFieldsForSubDomain(subdomainfield, id, listId);
	}

	@JsonView(MasterView.SubDomainFieldView.class)
	@RequestMapping("public/getFieldOfSubDomain/{id}")
	public SubDomainField getSubDomainFieldById(@PathVariable("id") int id)

	{
		return defaultrestservice.getSubDomainFieldById(id);
	}

	
	
	@JsonView(MasterView.DomainView.class)
	@RequestMapping("public/getDomainByCategoryId/{categoryid}")
	public List<Domain> getDomainsByCategoryId(@PathVariable("categoryid") int categoryid, @RequestParam("domainName") String domainName) {

		return domainrepository.findByCategoriesIdAndDomainDescriptionContaining(categoryid, domainName);
	}

	
	
	@JsonView(MasterView.SubDomainView.class)
	@RequestMapping("public/getSubDomainsByDomainId/{domainid}")
	public List<SubDomain> getDomainsByDomainId(@PathVariable("domainid") int domainid, @RequestParam("subDomainName") String subDomainName) {

		return subdomainrepository.findByDomainsIdAndSubDomainDescriptionContaining(domainid, subDomainName);
	}

	
	

	

	

	


	
	/**
	 * @param file
	 * @param id
	 */
	@RequestMapping("admin/toggleDomainFieldStatus")
	public void toggleDomainFieldStatus(@RequestParam("id")int id)
	{
		defaultrestservice.toggleStatusForDomainFields(id);
	}

	
	
	
	@RequestMapping("admin/toggleSubDomainFieldStatus")
	public void toggleSubDomainFieldStatus(@RequestParam("id")int id)
	{
		defaultrestservice.toggleStatusForSubDomainFields(id);
	}
	
	
	@JsonView({MasterView.UserGroupView.class})
	@RequestMapping("public/getGroups")
	public List<UserGroupMaster>getUserGroups(@RequestParam("searchParam")String searchParam)
	{
		return defaultrestservice.getGroupMasters(searchParam);
	}
	
	
	@RequestMapping("/admin/addGroup")
	public void addGroupMaster(@RequestBody UserGroupMaster usergroupmaster)
	{
		defaultrestservice.addGroupMaster(usergroupmaster);
	}
	
	
	@JsonView({MasterView.UserGroupView.class})
@RequestMapping("public/getGroupById/{id}")
public UserGroupMaster getUserGroup(@PathVariable("id") int id)
{
	return defaultrestservice.getUserGroup(id);
}
	
@JsonView({MasterView.UserView.class})
	@RequestMapping("admin/listUsers")
	public List<User>getUsers()
	{
		return userrepository.findAll();
	}

	

	@JsonView({MasterView.UserProfileView.class})
	@RequestMapping("public/getProfileByUserId/")
	public UserProfile getUserProfile(@RequestParam("id")int id)
	{
		
		return defaultrestservice.getUserProfileByUserId(id);
	}
	

	
	@JsonView(MasterView.UserView.class)
	@RequestMapping("/public/login")
	public User login() {
		
		return defaultrestservice.loggedInUser();
	}
	

@RequestMapping("admin/addProfile")
public void addProfile(@RequestBody UserProfile userprofile,@RequestParam("userId")int userId,@RequestParam("groups")List<String>groups)
{
	
	if(userprofile.getId()==null)
	{
		User user=userrepository.findById(userId);
		userprofile.setUserId(user.getId());
		
		userprofile.setUserName(user.getUsername());
	}
	
	
	
	 
	List<UserGroupMaster>userGroups =new ArrayList<UserGroupMaster>();
	Iterator<String>it=groups.iterator();
	 
	while(it.hasNext())
	{
		String groupName=(String)it.next();
		UserGroupMaster usergroupmaster=usergroupmasterrepository.findByGroupName(groupName);
		userGroups.add(usergroupmaster);
	}

	userprofile.setUsergroupmaster(userGroups);
	userprofilerepository.save(userprofile);
	
	
}


@RequestMapping("admin/getGroupsForUser")
@JsonView(MasterView.UserGroupView.class)
public List<UserGroupMaster>getGroups(@RequestParam("id")int id)
{
	
	List<UserGroupMaster>usergroups=new ArrayList<UserGroupMaster>();
	
	User user=userrepository.findById(id);
	UserProfile userprofile=userprofilerepository.findByUserId(user.getId());
	
	if(userprofile!=null)
		usergroups=usergroupmasterrepository.findByUsersId(userprofile.getId());
	
	
	return usergroups;
	
}


@JsonView(MasterView.UserProfileView.class)
@RequestMapping("admin/getManagers/")
public List<UserProfile> getUserNames(@RequestParam(value="q")String query)
{
	
	List<UserProfile>users=userprofilerepository.findByUserNameStartingWith(query);
	return users;
	
}


@RequestMapping("admin/saveDisposition")
public void saveDispositioning(@RequestBody SubDomianExpertDispositionMapping disposition)
{
	defaultrestservice.saveDisposition(disposition);

	
	
}

@JsonView(MasterView.SubDomainExpertView.class)
@RequestMapping("admin/getExpertDispositioningById/{id}")
public SubDomianExpertDispositionMapping getDispositioning(@PathVariable("id")int id)
{
	return subdomainexpertrepository.findById(id);
}



}
