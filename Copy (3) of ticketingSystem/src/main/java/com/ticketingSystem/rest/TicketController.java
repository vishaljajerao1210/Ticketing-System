package com.ticketingSystem.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Constants.URLConstants;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.authService.AuthService;
import com.ticketingSystem.entities.FileItem;
import com.ticketingSystem.entities.FileUpload;
import com.ticketingSystem.entities.TicketDetail;
import com.ticketingSystem.entities.TicketMaster;
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
import com.ticketingSystem.rest.services.TicketService;
import com.ticketingSystem.utilities.JsonPage;

@RestController
public class TicketController {

	
	
	@Autowired
	MasterRestService defaultrestservice;

	@Value("${multipart.location}")
	String baseLocation;

	@Autowired
	DomainFieldRepository domainfieldrepository;

	@Autowired
	TaskService taskService;
	
	
	@Autowired
	TicketService ticketservice;

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
		URLConstants urlConstants;
		

        @RequestMapping("/test")
        public void test1(){
        	System.out.println(urlConstants.getPersonalTaskURL());
        }
		
		@JsonView({ MasterView.SubDomainFieldView.class })
		@RequestMapping("public/returnFieldsForTicket")
		public Map<String, Object> returnBoth(@RequestParam("domainid") int domainId,
				@RequestParam("subdomainid") int subdomainId) {
			/* returns fields for ticket */
			return ticketservice.returnFieldsForTicket(domainId, subdomainId);
		}

		@RequestMapping("/public/raiseTicket/")
		public HashMap<String, Object> raiseTicket(@RequestBody Map<String, Object> ticket, @RequestParam("domainid") int domainid,
				@RequestParam("subdomainid") int subdomainid,@RequestParam("fileId")String fileId) {
		return	ticketservice.saveRaisedTicket(ticket, domainid, subdomainid,fileId);
		}
		
		@RequestMapping("/public/getTickets")
		@JsonView(MasterView.TicketMasterView.class)
		public JsonPage<TicketMaster> getTickets(@RequestParam("PI") int pageIndex,@RequestParam("PG")int pageSize, @RequestParam("searchParams") String searchParams){
			/* getting all tickets */
			return ticketservice.getTicketMasters(pageIndex, pageSize, searchParams);
		}
		
		
		@JsonView({MasterView.FileUploadView.class})
		@RequestMapping(value = "user/upload/", method = RequestMethod.POST)
		public FileUpload uploadFiles(@RequestPart("file") MultipartFile file, @RequestParam Map<String, Object> formData) {
			return ticketservice.uploadFiles(file, formData);
		}
		
		
		
		@RequestMapping("/user/removeFile")
		public void deleteFileFromServer(@RequestBody Map<String, Object> formData) {
			ticketservice.deleteFile(formData);

		}
		@RequestMapping("file/download")
		public void downloadFile(@RequestParam("referenceNo") int referenceNo, @RequestParam("fileName") String fileName)
				throws IOException {
			ticketservice.downloadFile(referenceNo, fileName);
		}

		
		
		@JsonView({MasterView.FileUploadView.class})
		@RequestMapping(value = "user/uploadWithTicket", method = RequestMethod.POST)
		// @RequestMapping("user/uploadWithTicket")
		public FileUpload saveFile(@RequestPart("file") MultipartFile file, @RequestParam Map<Object, Object> formData) {
			return ticketservice.saveFile(file, formData);
		}
		
		@JsonView({MasterView.FileItemView.class})
		@RequestMapping("public/getFilesForTicket/{id}")
		public List<FileItem> getFilesForTicket(@PathVariable("id") int id) {
			return ticketservice.getFilesForTicket(id);

		}
		@JsonView(MasterView.TicketDetailView.class)
		@RequestMapping("public/getFieldsForTicket/{id}")
		public List<TicketDetail> getFieldsForTicket(@PathVariable("id") int id) {
			return ticketdetailrepository.findByTicketmasterId(id);
		}
		
	
}
