package com.ticketingSystem.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ticketingSystem.entities.Domain;



@Repository
public interface DomainRepository extends JpaRepository<Domain,Integer> {
 public Domain findById(int id);
 
 public Domain findByDomainDescription(String domain);
 
 
 /*@Query("select d from Domains d where d.categories.categoryDescription=:categoryname")
 
 
 public List<Domains>findByCategoryName(@Param("categoryname")String categoryName);*/

 public Domain findByCategoriesIdAndDomainDescription(int id, String domainDescription);

public List<Domain> findByCategoriesIdAndDomainDescriptionContaining(int id, String domainDescription);

public Page<Domain> findAll(Pageable pageable);

@Query("select d from Domain d where d.domainDescription LIKE %:searchParam% OR d.categories.categoryDescription LIKE %:searchParam% ")
public Page<Domain>findBySearchString(@Param(value="searchParam")String searchParam,Pageable pageable);


@Query("select count(d.id) from Domain d")
public Integer findRecordCount();

 
}
