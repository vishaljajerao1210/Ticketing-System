package com.ticketingSystem.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name = "domains")
public class Domain  extends AbstractEntityMaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView({MasterView.DomainView.class,MasterView.SubDomainView.class,MasterView.DomainFieldView.class,
		MasterView.TicketMasterView.class})
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
    
	@JsonView({MasterView.DomainView.class,MasterView.SubDomainView.class,MasterView.DomainFieldView.class,
		MasterView.TicketMasterView.class})
	@Column(name = "domain_description")
	String domainDescription;

	@JsonView({MasterView.DomainView.class, MasterView.SubDomainView.class})
	@ManyToOne
	@JoinColumn
	Category categories;

	
	@OneToMany(mappedBy="domains")
	@JsonIgnore
	List<SubDomain> subdomains;
	
	
	@OneToMany(mappedBy="domain")
	@JsonIgnore
	List<DomainField> domainfields;
	
	@OneToMany(mappedBy="domains")
	@JsonIgnore
	List<TicketMaster>ticketmasters;
	
	
	
	
	

	public List<TicketMaster> getTicketmasters() {
		return ticketmasters;
	}

	public void setTicketmasters(List<TicketMaster> ticketmasters) {
		this.ticketmasters = ticketmasters;
	}

	public List<DomainField> getDomainfields() {
		return domainfields;
	}

	public void setDomainfields(List<DomainField> domainfields) {
		this.domainfields = domainfields;
	}

	public List<SubDomain> getSubdomains() {
		return subdomains;
	}

	public void setSubdomains(List<SubDomain> subdomains) {
		this.subdomains = subdomains;
	}

	public Category getCategories() {
		return categories;
	}

	public void setCategories(Category categories) {
		this.categories = categories;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	

}
