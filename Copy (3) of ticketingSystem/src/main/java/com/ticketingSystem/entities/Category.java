package com.ticketingSystem.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;



@Entity
@Table(name="categories")

public class Category  extends AbstractEntityMaster{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView({MasterView.DomainView.class,MasterView.CategoryView.class,MasterView.TicketMasterView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@JsonView({MasterView.DomainView.class,MasterView.CategoryView.class,MasterView.TicketMasterView.class, MasterView.SubDomainView.class})
	@Column(name="category_description",unique=true)
	String categoryDescription;
	
	
	@OneToMany(mappedBy="categories",fetch=FetchType.EAGER)
	@JsonIgnore
	List<Domain> domains;
	
	
	
	@OneToMany(mappedBy="categories")
	@JsonIgnore
	List<TicketMaster>ticketmasters;
	
	
	

	public List<TicketMaster> getTicketmasters() {
		return ticketmasters;
	}

	public void setTicketmasters(List<TicketMaster> ticketmasters) {
		this.ticketmasters = ticketmasters;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String CategoryDescription) {
		this.categoryDescription =CategoryDescription;
	}
	
	
	
}
