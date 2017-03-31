package com.ticketingSystem.rest.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ticketingSystem.activitiService.ManagerApprovalService;
import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.Domain;
import com.ticketingSystem.entities.DomainField;
import com.ticketingSystem.entities.FileItem;
import com.ticketingSystem.entities.FileUpload;
import com.ticketingSystem.entities.SubDomainField;
import com.ticketingSystem.entities.TicketDetail;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.entities.UserProfile;
import com.ticketingSystem.enums.TicketStatus;
import com.ticketingSystem.enums.TicketType;
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
import com.ticketingSystem.utilities.DateUtilities;
import com.ticketingSystem.utilities.JsonPage;
import com.ticketingSystem.utilities.PageUtil;

@Service
public class TicketService {

	@Autowired
	MasterRestService defaultrestservice;

	@Value("${multipart.location}")
	String baseLocation;

	@Autowired
	PageUtil pageUtil;
	
	
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


	@Autowired
	ManagerApprovalService activityService;


	public Map<String,Object> returnFieldsForTicket(int domainId,int subdomainId)
	{
		Map<String,Object> returnboth=new HashMap<String, Object>();
		List<DomainField> domainfields=domainfieldrepository.findByDomainId(domainId);
		List<SubDomainField> subdomainfields=subdomainfieldrepository.findBySubdomainsId(subdomainId);
		returnboth.put("domainfields",domainfields);
		returnboth.put("subdomainfields",subdomainfields);
		return returnboth;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public HashMap<String,Object> saveRaisedTicket(Map<String,Object>ticket,int domainid,int subdomainid,String fileId)
	{
		

		TicketMaster ticketmaster=new TicketMaster();
		try {
			ticketmaster.setRaisedDate(DateUtilities.getCurrentDateAndTime());
			ticketmaster.setRaisedTime(DateUtilities.getCurrentTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ticketmaster.setStatus(TicketStatus.Pending.getTicketStatus());
		Domain domain=domainrepository.findById(domainid);
		ticketmaster.setDomains(domain);
		ticketmaster.setCategories(domain.getCategories());

		ticketmaster.setSubdomain(subdomainrepository.findById(subdomainid));
		ticketmaster.setRaisedBy(userprofilerepository.findByUserId(authService.loggedInUser().getId()));
		ticketmasterrepository.save(ticketmaster);

		Map<Integer,Object>domains=new HashMap<Integer, Object>();

		Map<Integer,Object>subdomains=new HashMap<Integer, Object>();

		List<Map<Integer,Object>>multipleValues=new ArrayList<Map<Integer,Object>>();



		if(ticket.containsKey("domain"))
			domains=(Map<Integer, Object>) ticket.get("domain");

		if(ticket.containsKey("subdomain"))
			subdomains=(Map<Integer, Object>)ticket.get("subdomain");

		if(ticket.containsKey("multipleValues"))
			multipleValues=(List<Map<Integer,Object>>) ticket.get("multipleValues");



		if(!fileId.isEmpty())
		{
			FileUpload fileupload=fileuploadrepository.findOne(Integer.parseInt(fileId));
		
			fileupload.setHasTicket(true);
			fileuploadrepository.save(fileupload);
			ticketmaster.setFileupload(fileupload);
		}


		if(!multipleValues.isEmpty())
		{
			Iterator<Map<Integer, Object>>it=multipleValues.iterator();
			while(it.hasNext())
			{
				Map<Integer,Object>map=it.next();
				String type=(String)map.get("type");
				if(type.equals("domains"))
				{
					int id= (int) map.get("id");

					List<String>values=(List<String>) map.get("data");
					if(values.size()>0)
					{
						String value=values.toString().replace("[", "")
								.replace("]", "").replace(", ", ",");
						TicketDetail ticketdetails=new TicketDetail();
						ticketdetails.setFieldName(domainfieldrepository.findById(id).getListmaster().getListMasterValue());
						ticketdetails.setValue(value);
						ticketdetails.setTickettype(TicketType.domain);
						ticketdetails.setTicketmaster(ticketmaster);
						ticketdetailrepository.save(ticketdetails);
					}
				}
				else
				{

					int id= (int) map.get("id");
					List<String>values=(List<String>) map.get("data");
					if(values.size()>0)
					{
						String value=values.toString().replace("[", "")
								.replace("]", "").replace(", ", ",");
						TicketDetail ticketdetails=new TicketDetail();
						ticketdetails.setFieldName(subdomainfieldrepository.findById(id).getListmaster().getListMasterValue());
						ticketdetails.setValue(value);
						ticketdetails.setTickettype(TicketType.subdomain);
						ticketdetails.setTicketmaster(ticketmaster);
						ticketdetailrepository.save(ticketdetails);
					}

				}
			}
		}
		if(!domains.isEmpty())
		{
			Set domainfields=domains.entrySet();
			Iterator it1=domainfields.iterator();
			while(it1.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) it1.next();
				TicketDetail ticketdetails=new TicketDetail();
				//String s=it.next().toString();
				String id= (String) mapEntry.getKey();
				DomainField domainfield=domainfieldrepository.findById(Integer.parseInt(id));
				ticketdetails.setTickettype(TicketType.domain);
				if(domainfield.isList())
					ticketdetails.setFieldName(domainfield.getListmaster().getListMasterValue());
				else
					ticketdetails.setFieldName(domainfield.getFieldName());
				ticketdetails.setValue(mapEntry.getValue().toString());
				ticketdetails.setTicketmaster(ticketmaster);
				ticketdetailrepository.save(ticketdetails);
			}
		}
		if(!subdomains.isEmpty())
		{
			Set subdomainfields=subdomains.entrySet();
			Iterator it2=subdomainfields.iterator();
			while(it2.hasNext())
			{
				Map.Entry mapEntry = (Map.Entry) it2.next();
				TicketDetail ticketdetails=new TicketDetail();
				//String s=it.next().toString();
				String id= (String) mapEntry.getKey();
				SubDomainField subdomainfield=subdomainfieldrepository.findById(Integer.parseInt(id));
				ticketdetails.setTickettype(TicketType.subdomain);
				if(subdomainfield.isList())
					ticketdetails.setFieldName(subdomainfield.getListmaster().getListMasterValue());
				else
					ticketdetails.setFieldName(subdomainfield.getFieldName());
				ticketdetails.setValue(mapEntry.getValue().toString());
				ticketdetails.setTicketmaster(ticketmaster);
				ticketdetailrepository.save(ticketdetails);
			}
		}

	return 	this.triggerProcess(ticketmaster);
	}



	public 	JsonPage<TicketMaster> getTicketMasters(int index, int size, String searchParams)
	{
		JsonPage<TicketMaster>  ticketsPaged = null;
		Date startDate = null;
		Date endDate = null;
		UserProfile raisedBy = null;
		Pageable page = null;
		Page<TicketMaster> tickets = null;
		QueryParams queryParams = null;
		String searchString = "";
		try{
			raisedBy = userprofilerepository.findByUserId(authService.loggedInUser().getId());
			page = pageUtil.getPageRequest(index, size, Direction.DESC, "raisedDate");
			queryParams = QueryParams.returnObject(searchParams);
			searchString = queryParams.getSearchStr() == null ? "" : queryParams.getSearchStr();
			
			if(queryParams != null && queryParams.getStartDate() != null && queryParams.getEndDate() != null){
				startDate = new Date(queryParams.getStartDate());
				endDate = new Date(queryParams.getEndDate());
				tickets = ticketmasterrepository.findBySearchStringAndRaisedDateAndRaisedBy(raisedBy, searchString, startDate, endDate , page);			
			}
			else
			{	
				tickets = ticketmasterrepository.findBySearchStringAndRaisedBy(raisedBy, searchString, page);
			}
			ticketsPaged = new JsonPage<TicketMaster>(tickets, page);
		
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return ticketsPaged;
	}	



	/* file upload without ticket id */
	public FileUpload uploadFiles( MultipartFile file,Map<String, Object> formData)
	{
		FileUpload fileupload = new FileUpload();
		if (!file.isEmpty()) {
			try {
				FileItem fileitem = new FileItem();
				String path;
				boolean flag=true;
				String name = file.getOriginalFilename();
				if (formData.get("referenceNo").toString().isEmpty()) {
					FileUpload filemaster = new FileUpload();
					filemaster.setUploadedDate(DateUtilities.getCurrentDate());
					filemaster.setHasTicket(false);
					filemaster.setUploadedBy(userprofilerepository.findByUserId(authService.loggedInUser().getId()));
					filemaster.setExpiryTime(DateUtilities.getCurrentDateAndTimeAfterHour());
					fileuploadrepository.save(filemaster);
					fileitem.setFileupload(filemaster);
					path = baseLocation + "/" + filemaster.getId();
					fileupload = fileuploadrepository.findOne(filemaster.getId());
				} else {
					int id=Integer.parseInt(formData.get("referenceNo").toString());

					if(fileitemrepository.findByFileuploadIdAndFileName(id,name)!=null)
					{

						flag=false;
					}




					path = baseLocation + "/" + Integer.parseInt(formData.get("referenceNo").toString());
					FileUpload filemaster = fileuploadrepository
							.findOne(Integer.parseInt(formData.get("referenceNo").toString()));
					filemaster.setModifiedDate(DateUtilities.getCurrentDate());
					filemaster.setModifiedBy(authService.loggedInUser());

					fileuploadrepository.save(filemaster);
					fileitem.setFileupload(filemaster);
					fileupload = fileuploadrepository.findOne(filemaster.getId());

				}
				if(flag)
				{
					fileitem.setFileName(name);
					fileitem.setFilePath(path + "/" + name);
					fileitem.setUploadedDate(DateUtilities.getCurrentDate());
					fileitem.setUploadedTime(DateUtilities.getCurrentTime());

					File dir = new File(path);
					if (!dir.exists())
						dir.mkdirs();

					File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(file.getBytes());
					stream.close();
					fileitemrepository.save(fileitem);


				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}

		return fileupload;
	}




	public void deleteFile(Map<String, Object> formData)
	{
		String referenceNo = formData.get("referenceNo").toString();
		String fileName = (String) formData.get("fileName");
		if (!referenceNo.equals(null) && !fileName.equals(null)) {
			String filePath = "";

			FileUpload fileUpload = fileuploadrepository.findOne(Integer.parseInt(referenceNo));
			List<FileItem> fileitems = fileUpload.getFileItems();

			for (Iterator<FileItem> itr = fileitems.iterator(); itr.hasNext();) {
				FileItem fileitem = (FileItem) itr.next();
				if (fileName.equalsIgnoreCase(fileitem.getFileName())) {
					filePath = fileitem.getFilePath();
					itr.remove();
					fileitemrepository.delete(fileitem);
				}

			}
			if (!filePath.isEmpty()) {
				File fileToDelete = new File(filePath);
				if (fileToDelete.isFile() && fileToDelete.exists()) {
					fileToDelete.delete();
				}
			}
			fileUpload.setFileItems(fileitems);
			try {
				fileUpload.setModifiedDate(DateUtilities.getCurrentDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileuploadrepository.save(fileUpload);

		}
	}










	public void downloadFile(int referenceNo,String fileName) throws IOException
	{

		FileUpload fileUpload = fileuploadrepository.findOne(referenceNo);
		String filePath = "";
		ServletContext context = request.getServletContext();
		List<FileItem> fileitems = fileUpload.getFileItems();
		for (FileItem fileitem : fileitems) {
			if (fileName.equalsIgnoreCase(fileitem.getFileName())) {
				filePath = fileitem.getFilePath();
			}
		}

		File fileToDownload = new File(filePath);

		FileInputStream inputStream = new FileInputStream(fileToDownload);

		// get MIME type of the file
		String mimeType = context.getMimeType(fileName);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) fileToDownload.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; fileName=\"%s\"", fileToDownload.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		IOUtils.copy(inputStream, outStream);

		inputStream.close();
		outStream.close();

	}

	public FileUpload saveFile(MultipartFile file,Map<Object, Object>formData)
	{
		TicketMaster ticketmaster = ticketmasterrepository
				.findOne(Integer.parseInt(formData.get("referenceNo").toString()));
		FileUpload fileupload = new FileUpload();

		if (!file.isEmpty()) {
			try {
				FileItem fileitem = new FileItem();
				String path;
				String name = file.getOriginalFilename();

				if (ticketmaster.getFileupload() == null) {
					FileUpload filemaster = new FileUpload();
					filemaster.setUploadedDate(DateUtilities.getCurrentDate());
					filemaster.setHasTicket(true);
					filemaster.setExpiryTime(DateUtilities.getCurrentDateAndTimeAfterHour());
					filemaster.setUploadedBy(userprofilerepository.findByUserId(authService.loggedInUser().getId()));

					fileuploadrepository.save(filemaster);
					fileitem.setFileupload(filemaster);
					path = baseLocation + "/" + filemaster.getId();
					ticketmaster.setFileupload(filemaster);
					ticketmasterrepository.save(ticketmaster);
					fileupload = fileuploadrepository.findOne(filemaster.getId());

				} else {
					FileUpload filemaster = ticketmaster.getFileupload();

					if(fileitemrepository.findByFileuploadIdAndFileName(filemaster.getId(), name)!=null)
						fileitem=fileitemrepository.findByFileuploadIdAndFileName(filemaster.getId(), name);
					filemaster.setModifiedBy(authService.loggedInUser());
					filemaster.setModifiedDate(DateUtilities.getCurrentDate());
					fileuploadrepository.save(filemaster);
					path = baseLocation + "/" + filemaster.getId();
					fileitem.setFileupload(filemaster);
					fileupload = fileuploadrepository.findOne(filemaster.getId());
				}
				fileitem.setFileName(name);
				fileitem.setFilePath(path + "/" + name);
				fileitem.setUploadedDate(DateUtilities.getCurrentDate());
				fileitem.setUploadedTime(DateUtilities.getCurrentTime());

				File dir = new File(path);
				if (!dir.exists())
					dir.mkdirs();

				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file.getBytes());
				stream.close();
				fileitemrepository.save(fileitem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileupload;
	}

	public List<FileItem> getFilesForTicket(int id)
	{

		List<FileItem>fileItems=new ArrayList<FileItem>();
		if (ticketmasterrepository.findOne(id).getFileupload() != null) {
			FileUpload	fileupload = fileuploadrepository.findByTicketmasterId(id);

			fileItems=fileupload.getFileItems();
			return fileItems;
		} else {
			return fileItems;
		}
	}


	public HashMap<String,Object> triggerProcess(TicketMaster ticketmaster)
	{
		HashMap<String,Object>returnParams=new HashMap<String, Object>();
		
		Map<String,Object>variables=new HashMap<String,Object>();
		variables.put("requestorId",userprofilerepository.findByUserId(authService.loggedInUser().getId()).getId());
		variables.put("ticketId", ticketmaster.getId());
		variables.put("ticketIdStr", ticketmaster.getId().toString());
		variables.put("category", ticketmaster.getCategories().getCategoryDescription());
		variables.put("requestor",authService.loggedInUser().getUsername());
		if(ticketmaster.getSubdomain().isGroup())
			variables.put("expertGroup", ticketmaster.getSubdomain().getDefaultUserGroup().getId());
		else
			variables.put("expert", ticketmaster.getSubdomain().getDefaultExpert().getId());
		variables.put("isGroup",ticketmaster.getSubdomain().isGroup());
		variables.put("isManagerApprovalRequired",ticketmaster.getSubdomain().isManagerApproval());
		variables.put("isSpecialManagerApprovalRequired",ticketmaster.getSubdomain().isSpecialManagerApproval());
		variables.put("manager", ticketmaster.getRaisedBy().getManager().getId());
		variables.put("specialManager",ticketmaster.getRaisedBy().getSpecialManager().getId());
		activityService.requestedInformation(variables);
		 returnParams.put("id", ticketmaster.getId());
		return  returnParams;
	}











}
