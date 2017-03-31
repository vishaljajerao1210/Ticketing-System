package com.ticketingSystem.rest.services;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ticketingSystem.activitiService.ManagerApprovalService;
import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.Category;
import com.ticketingSystem.entities.Domain;
import com.ticketingSystem.entities.DomainField;
import com.ticketingSystem.entities.ListMaster;
import com.ticketingSystem.entities.ListValue;
import com.ticketingSystem.entities.SubDomain;
import com.ticketingSystem.entities.SubDomainField;
import com.ticketingSystem.entities.SubDomianExpertDispositionMapping;
import com.ticketingSystem.entities.User;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.enums.ActorType;
import com.ticketingSystem.enums.FieldEnum;
import com.ticketingSystem.enums.FieldStatus;
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
import com.ticketingSystem.utilities.JsonPage;
import com.ticketingSystem.utilities.PageUtil;


@Service
public class MasterRestService {




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
	PageUtil pageUtil;





	


	@Autowired
	TicketDetailRepository ticketdetailrepository;


	@Autowired
	TicketMasterRepository ticketmasterrepository;


	@Autowired
	UserGroupMasterRepository usergroupmasterrepository;

	@Autowired
	UserRepository userrepository;

	@Autowired
	SubDomainExpertRepository subDomainExpertRepository;

	/* returns a list of categories */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Category>getCategories(int index,int size,String searchParam)
	{
		if(!searchParam.isEmpty()||!searchParam.equals(""))
			return new JsonPage(categoryrepository.findByCategoryDescriptionContaining(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else
			return new JsonPage(categoryrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}


	/* saves a category object */
	public HashMap<String, String>saveCategory(Category categories)
	{
		HashMap<String,String>returnmap=new HashMap<String,String>();
		try{
			returnmap.put("status","true");
			
			
			
			categoryrepository.save(categories);
		}
		catch(Exception e)

		{
			e.printStackTrace();
			returnmap.put("status","false");
		}
		return returnmap;
	}

	/* returns a particular category by its id*/
	public Category getCategory(int id)
	{

		return categoryrepository.findById(id);
	}


	/* returns a list of domains */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Domain>getDomains(int index,int size,String searchParam)
	{
		if((!searchParam.toString().isEmpty()||!searchParam.toString().equals(""))&&size>0)
			return new JsonPage(domainrepository.findBySearchString(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else 
			return new JsonPage(domainrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}

	/* returns a particular domain by its id*/
	public Domain getDomain(int id)
	{

		return domainrepository.findById(id);
	}

	/* saves a Domains object */
	public HashMap<String,String> saveDomain(Domain domains,Integer id)
	{
		HashMap<String,String>returnparam=new HashMap<String,String>();

		try{

			if(id!=null)
			{
			Category categories=categoryrepository.findById(id);
			domains.setCategories(categories);
			}
			domainrepository.save(domains);
			returnparam.put("status","true");
		}
		catch(Exception e)

		{
			returnparam.put("status", "false");
			e.printStackTrace();

		}
		return returnparam;

	}

	/* returns all subdomains */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<SubDomain> getSubDomains(int index,int size,String searchParam)
	{
		if((!searchParam.toString().isEmpty()||!searchParam.toString().equals(""))&&size>0)
			return new JsonPage(subdomainrepository.findBySearchFilter(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else 
			return new JsonPage(subdomainrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}

	/* returns a particular subdomain by its id*/
	public SubDomain getSubDomain(int id)
	{

		return subdomainrepository.findById(id);
	}

	/* saves a SubDomains object */
	public HashMap<String,String> saveSubDomain(SubDomain subdomains,Integer id,Integer groupId,String actorType)
	{
		HashMap<String,String>returnparam=new HashMap<String,String>();

		try{

              if(id!=null)
			subdomains.setDomains(domainrepository.findById(id));
			//String s=actorType.replaceAll("\\s","");

			if(ActorType.ExpertGroup.getActorName().equals(actorType))
			{
				if(groupId!=null)
				subdomains.setDefaultUserGroup(usergroupmasterrepository.findById(groupId));

				subdomains.setDefaultExpert(null);

				subdomains.setGroup(true);

				subdomains.setActorType(ActorType.ExpertGroup.getActorName());
			}
			else
			{
				subdomains.setGroup(false);
				subdomains.setDefaultUserGroup(null);

				subdomains.setActorType(ActorType.Expert.getActorName());

			}




			subdomainrepository.save(subdomains);
			returnparam.put("status","true");
		}
		catch(Exception e)

		{
			returnparam.put("status", "false");
			e.printStackTrace();

		}
		return returnparam;

	}


	/* checks whether category unique or not */
	public HashMap<Object,Object>checkIfCategoryIsUnique(String category)
	{
		HashMap<Object, Object>returnmap=new HashMap<Object,Object>();
		returnmap.put("status",false);
		if(categoryrepository.findByCategoryDescription(category)!=null)
			returnmap.put("status",true);
		return returnmap;

	}

	/* checks whether Domain unique or not */
	public boolean checkIfDomainIsUnique(String domainName, Integer categoryId)
	{
		Domain domain = null;
		if(categoryId != null){
		domain = domainrepository.findByCategoriesIdAndDomainDescription(categoryId, domainName);
		}
		if(domain == null) return true;
		else return false;	
	}
	
	
	
	
	



	/* checks whether SubDomain unique or not */
	public HashMap<Object,Object>checkIfSubDomainIsUnique(String subdomain,Integer id)
	{
		HashMap<Object, Object>returnmap=new HashMap<Object,Object>();
		returnmap.put("status",false);
		
		if(id!=null)
		{
			if(subdomainrepository.findByDomainsIdAndSubDomainDescription(id, subdomain)!=null)
				returnmap.put("status",true);
		}
		
		
		return returnmap;


	}
	/* saves a fields for domains object */
	public HashMap<String,String> saveFieldsForDomain(DomainField domainfields,Integer id,Integer listId)
	{
		HashMap<String,String>returnparam=new HashMap<String,String>();

		try{
			
			if(id!=null)
			{
			Domain domains=domainrepository.findById(id);
			domainfields.setDomain(domains);
			}
			if(domainfields.getFieldEnum()==FieldEnum.Mandatory)
				domainfields.setMandatory(true);

			if(listId!=null)
			{
				domainfields.setList(true);
				ListMaster listmaster=listmasterrepository.findById(listId);
				domainfields.setListmaster(listmaster);
			}
			DomainField dom=domainfieldrepository.findById(domainfields.getId());
			if(dom!=null)
			{

				if(domainfields.getFieldEnum()==FieldEnum.Optional)
					domainfields.setMandatory(false);

				if(listId==null)
				{
					domainfields.setList(false);
					domainfields.setListmaster(null);
				}
			}

			domainfields.setFieldStatus(FieldStatus.Active);
			domainfields.setFieldEnabled(true);
			
			domainfieldrepository.save(domainfields);
			returnparam.put("status","true");
		}
		catch(Exception e)

		{
			returnparam.put("status", "false");
			e.printStackTrace();

		}
		return returnparam;

	}

	/* returns fields of a various domains */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<DomainField>getAllDomainfields(int index,int size,String searchParam)
	{
		if((!searchParam.toString().isEmpty()||!searchParam.toString().equals(""))&&size>0)
			return new JsonPage(domainfieldrepository.findBySearchFilter(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else 
			return new JsonPage(domainfieldrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}


	/* saves listmasters */
	public HashMap<String,String>saveListMasters(ListMaster listmaster)
	{
		HashMap<String,String>returnmap=new HashMap<String,String>();

		try{
			String listmastervalue=listmaster.getListMasterValue();
			listmaster.setParentListValue(listmastervalue);
			listmaster.setParentlist(listmaster);
			listmasterrepository.save(listmaster);

			returnmap.put("status","true");
		}
		catch(Exception e)

		{
			returnmap.put("status", "false");
			e.printStackTrace();

		}
		return returnmap;




	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<ListMaster>getAllListMasters(int index,int size,String searchParam)
	{
		if((!searchParam.toString().isEmpty()||!searchParam.toString().equals(""))&&size>0)
			return new JsonPage(listmasterrepository.findBySearchFilter(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else 
			return new JsonPage(listmasterrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}



	/* saves listvalues */
	public HashMap<String,String>saveListValues(ListValue listvalues,Integer id)
	{
		HashMap<String,String>returnmap=new HashMap<String,String>();

		try{
			
			if(id!=null)
			{
			ListMaster listmasters=listmasterrepository.findById(id);
			listvalues.setListmaster(listmasters);
			}
			listvaluesrepository.save(listvalues);

			returnmap.put("status","true");
		}
		catch(Exception e)

		{
			returnmap.put("status", "false");
			e.printStackTrace();

		}
		return returnmap;




	}


	public List<ListValue> getAllListValues(int listId ,String searchParam)
	{

		return listvaluesrepository.findBySearchFilterAndListMasterId(searchParam, listId);

	}

	public ListMaster getListMasterById(int id)
	{
		return listmasterrepository.findById(id);
	}


	public ListValue getListValueById(int id)
	{
		return listvaluesrepository.findById(id);
	}

	public DomainField getDomainFieldById(int id)
	{
		return domainfieldrepository.findById(id);
	}

	public SubDomainField getSubDomainFieldById(int id)
	{
		return subdomainfieldrepository.findById(id);
	}





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<SubDomainField>getAllSubDomainfields(int index,int size,String searchParam)
	{
		if((!searchParam.toString().isEmpty()||!searchParam.toString().equals(""))&&size>0)
			return new JsonPage(subdomainfieldrepository.findBySearchFilter(searchParam,pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")), pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
		else 
			return new JsonPage(subdomainfieldrepository.findAll(pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate")),pageUtil.getPageRequest(index, size,Direction.DESC, "createdDate"));
	}




	/* saves a fields for sub domains object */
	public HashMap<String,String> saveFieldsForSubDomain(SubDomainField subdomainfields,Integer id,Integer listId)
	{
		HashMap<String,String>returnparam=new HashMap<String,String>();
		
		if(id!=null)
		{
		SubDomain subdomains=subdomainrepository.findById(id);
		subdomainfields.setSubdomains(subdomains);
		}
		try{


			if(listId!=null)
			{
				subdomainfields.setList(true);
				ListMaster listmaster=listmasterrepository.findById(listId);
				subdomainfields.setListmaster(listmaster);
			}
			SubDomainField sub=subdomainfieldrepository.findById(subdomainfields.getId());
			if(sub!=null)
			{
				if(subdomainfields.getFieldEnum()==FieldEnum.Optional)
					subdomainfields.setMandatory(false);


				if(listId==null)
				{
					subdomainfields.setList(false);
					subdomainfields.setListmaster(null);
				}
			}
			if(subdomainfields.getFieldEnum()==FieldEnum.Mandatory)
				subdomainfields.setMandatory(true);


			
			subdomainfields.setFieldStatus(FieldStatus.Active);
			subdomainfields.setFieldEnabled(true);
			subdomainfieldrepository.save(subdomainfields);
			returnparam.put("status","true");
		}
		catch(Exception e)

		{
			returnparam.put("status", "false");
			e.printStackTrace();

		}
		return returnparam;

	}

	/* returns fields for ticket*/






	/*save a raisedTicket*/







	/* lists all tickets */






















	public void toggleStatusForDomainFields(int id)
	{
		DomainField domainfield=domainfieldrepository.findById(id);
		if(domainfield.isFieldEnabled())
		{
			domainfield.setFieldStatus(FieldStatus.InActive);
			domainfield.setFieldEnabled(false);
			domainfieldrepository.save(domainfield);
		}
		else
		{
			domainfield.setFieldStatus(FieldStatus.Active);
			domainfield.setFieldEnabled(true);
			domainfieldrepository.save(domainfield);
		}
	}





	public void toggleStatusForSubDomainFields(int id)
	{
		SubDomainField subdomainfield=subdomainfieldrepository.findById(id);
		if(subdomainfield.isFieldEnabled())
		{
			subdomainfield.setFieldStatus(FieldStatus.InActive);
			subdomainfield.setFieldEnabled(false);
			subdomainfieldrepository.save(subdomainfield);
		}
		else
		{
			subdomainfield.setFieldStatus(FieldStatus.Active);
			subdomainfield.setFieldEnabled(true);
			subdomainfieldrepository.save(subdomainfield);
		}
	}



	public List<UserGroupMaster>getGroupMasters(String searchParam)
	{
		return usergroupmasterrepository.findByGroupNameContaining(searchParam);
	}


	public void addGroupMaster(UserGroupMaster usergroupmaster)
	{
		usergroupmasterrepository.save(usergroupmaster);
	}


	public UserGroupMaster getUserGroup(int id)
	{

		return usergroupmasterrepository.findById(id);
	}

	public List<UserGroupMaster>getUserGroups(String groupCode)
	{
		return usergroupmasterrepository.findByGroupCodeIgnoreCaseContaining(groupCode);
	}


	public UserProfile getUserProfileByUserId(int id)
	{

		UserProfile user=userprofilerepository.findByUserId(userrepository.findById(id).getId());
		return user;
	}

	


	public boolean isManagerApproved(boolean managerApproval)
	{
		return managerApproval;
	}



	public User loggedInUser()
	{

		User user = new User();
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
				&& SecurityContextHolder.getContext().getAuthentication()
				.isAuthenticated() == true) {

			user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

		}
		return user;


	}


	public Page<SubDomianExpertDispositionMapping> fetchDispositionsBySubDomain(Integer subDomainId, int index,
			int size) {
		PageRequest page = null;
		Page<SubDomianExpertDispositionMapping> dispositions = null;
		JsonPage<SubDomianExpertDispositionMapping> dispositionsPaged = null;
		try{
			page = pageUtil.getPageRequest(index, size);
			dispositions = subDomainExpertRepository.findBySubdomainDispositioning_Id(subDomainId, page);
			dispositionsPaged = new JsonPage<>(dispositions, page);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return dispositionsPaged;
	}


	public void saveDisposition(SubDomianExpertDispositionMapping disposition) {
		try{
			if(disposition.isCurrentGroup()){
				disposition.setCurrentAssignedUser(null);
			}else{
				disposition.setCurrentAssignedGroup(null);
			}

			if(disposition.isNextGroup()){
				disposition.setNextUser(null);
			}else{
				disposition.setNextUserGroup(null);
			}
			subDomainExpertRepository.save(disposition);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}