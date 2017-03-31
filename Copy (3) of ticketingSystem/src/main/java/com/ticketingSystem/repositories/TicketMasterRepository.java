package com.ticketingSystem.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.SubDomain;
import com.ticketingSystem.entities.TicketMaster;
import com.ticketingSystem.entities.UserGroupMaster;
import com.ticketingSystem.entities.UserProfile;



@Repository
public interface TicketMasterRepository  extends JpaRepository<TicketMaster, Integer>{

	public Page<TicketMaster> findByRaisedBy(UserProfile u,Pageable page);

	public List<TicketMaster>findBySubdomain(SubDomain subdomain);

	public Page<TicketMaster> findAll(Pageable page);

	public TicketMaster findById(Integer id);

	public List<TicketMaster>findByClaimedBy(UserProfile u);

	public Page<TicketMaster>findByCurrentExpert(UserProfile u,Pageable page);

	public Page<TicketMaster>findByCurrentGroupMasterAndIsClaimed(UserGroupMaster usergroup,boolean isClaimed,Pageable page);

	public Page<TicketMaster>findTop10ByCurrentGroupMasterInAndIsClaimedOrderByAssignedOnDesc(List<UserGroupMaster>usergroup,boolean isClaimed,Pageable pageable);

	public TicketMaster findByInstanceId(String processInstanceId);

	public Integer countByIsCurrentGroupAndIsClaimedAndCurrentGroupMaster_id(boolean isCurrentGroup, boolean isClaimed, Integer groupId);

	public Long countByCurrentExpert_UserId(Integer assignedTo);

	public Long countByRaisedBy_idAndCurrentStatus(Integer id , String currentStatus);

	
	@Query("SELECT t from TicketMaster t WHERE t.raisedBy = :raisedBy AND t.raisedDate BETWEEN :startDate AND :endDate AND "
			+ "(t.categories.categoryDescription LIKE %:searchStr% OR t.currentStatus LIKE %:searchStr% OR t.status "
			+ "LIKE %:searchStr% OR t.id LIKE %:searchStr%)")
	public Page<TicketMaster> findBySearchStringAndRaisedDateAndRaisedBy(@Param(value="raisedBy") UserProfile raisedBy,
			@Param(value="searchStr") String searchStr,
			@Param(value="startDate")Date startDate, @Param(value="endDate")Date endDate, Pageable page);

	
	@Query("SELECT t from TicketMaster t WHERE t.raisedBy = :raisedBy AND (t.categories.categoryDescription LIKE %:searchStr% OR "
			+ "t.currentStatus LIKE %:searchStr% OR t.status LIKE %:searchStr% OR t.id LIKE %:searchStr%)")	
	public Page<TicketMaster> findBySearchStringAndRaisedBy(@Param(value="raisedBy") UserProfile raisedBy,
			@Param(value="searchStr") String searchStr, Pageable page);

	
	@Query("SELECT t from TicketMaster t WHERE t.currentExpert = :assignedTo AND t.assignedOn BETWEEN :startDate AND :endDate AND "
			+ "(t.categories.categoryDescription LIKE %:searchStr% OR t.currentStatus LIKE %:searchStr% OR t.status"
			+ " LIKE %:searchStr% OR t.id LIKE %:searchStr%)")
	public Page<TicketMaster> findPersonalTasksBySearchStringAndAssignedDateAndCurrentExpert(@Param(value="assignedTo")
	UserProfile currentExpert, @Param(value="searchStr") String searchStr,
			@Param(value="startDate")Date startDate, @Param(value="endDate")Date endDate, Pageable page);

	
	@Query("SELECT t from TicketMaster t WHERE t.currentExpert = :currentExpert AND (t.categories.categoryDescription LIKE %:searchStr% "
			+ "OR t.currentStatus LIKE %:searchStr% OR t.status LIKE %:searchStr% OR t.id LIKE %:searchStr%)")
	public Page<TicketMaster> findPersonalTasksBySearchStringAndCurrentExpert(@Param(value="currentExpert") UserProfile currentExpert
			, @Param(value="searchStr") String searchStr, Pageable page);

	
	@Query("SELECT t from TicketMaster t WHERE t.isCurrentGroup = :isCurrentGroup AND t.isClaimed =:isClaimed AND"
			+ " t.currentGroupMaster.id = :currentGroupMaster"
			+ " AND t.assignedOn BETWEEN :startDate AND :endDate AND (t.categories.categoryDescription LIKE %:searchStr% OR "
			+ "t.currentStatus LIKE %:searchStr% OR t.status LIKE %:searchStr% OR t.id LIKE %:searchStr%)")
	public Page<TicketMaster> findGroupTasksBySearchStringAndAssignedDateAndCurrentGroup(@Param(value="isCurrentGroup") boolean isCurrentGroup,
			@Param(value="isClaimed")boolean isClaimed, @Param(value="currentGroupMaster")Integer currentGroupMaster, 
			@Param(value="searchStr") String searchStr, @Param(value="startDate")Date startDate, @Param(value="endDate")Date endDate, Pageable page);

	
	@Query("SELECT t from TicketMaster t WHERE t.isCurrentGroup = :isCurrentGroup AND t.isClaimed =:isClaimed AND"
			+ " t.currentGroupMaster.id = :currentGroupMaster AND (t.categories.categoryDescription LIKE %:searchStr% OR "
			+ "t.currentStatus LIKE %:searchStr% OR t.status LIKE %:searchStr% OR t.id LIKE %:searchStr%)")
	public Page<TicketMaster> findGroupTasksBySearchStringAndCurrentGroup(@Param(value="isCurrentGroup") boolean isCurrentGroup,
			@Param(value="isClaimed")boolean isClaimed, @Param(value="currentGroupMaster")Integer currentGroupMaster, 
			@Param(value="searchStr") String searchStr, Pageable page);
}
