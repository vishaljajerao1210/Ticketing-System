package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.ApproverLevelDetail;

@Repository
public interface ApproverLevelDetailRepository extends JpaRepository<ApproverLevelDetail, Integer> {

	public ApproverLevelDetail findById(Integer id);

	public List<ApproverLevelDetail> findByApproverLevel_ticketmaster_idAndCommentsIsNotNullAndCommentsNot(Integer ticketId, String comment);
}
