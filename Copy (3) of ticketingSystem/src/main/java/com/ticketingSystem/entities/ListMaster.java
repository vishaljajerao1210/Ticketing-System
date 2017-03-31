package com.ticketingSystem.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name = "listmaster")
public class ListMaster extends AbstractEntityMaster{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Column(name="list_master_description")
	String listMasterDescription;
	
	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Column(name="list_master_value")
	String listMasterValue;
	
	@JsonView(MasterView.ListMasterView.class)
	@OneToMany(mappedBy="listmaster")
	List<ListValue>listvalues;

	@OneToMany(mappedBy="listmaster",fetch=FetchType.EAGER)
	@JsonIgnore
	List<DomainField>domainfields;
	
	
	@OneToMany(mappedBy="listmaster")
	@JsonIgnore
	List<SubDomainField> subdomainfields;
	
	
	@OneToOne
	ListMaster parentlist;
	
	@Column(name="parent_list_value")
	String parentListValue;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getListMasterDescription() {
		return listMasterDescription;
	}

	public void setListMasterDescription(String listMasterDescription) {
		this.listMasterDescription = listMasterDescription;
	}

	public String getListMasterValue() {
		return listMasterValue;
	}

	public void setListMasterValue(String listMasterValue) {
		this.listMasterValue = listMasterValue;
	}

	public List<ListValue> getListvalues() {
		return listvalues;
	}

	public void setListvalues(List<ListValue> listvalues) {
		this.listvalues = listvalues;
	}

	public List<DomainField> getDomainfields() {
		return domainfields;
	}

	public void setDomainfields(List<DomainField> domainfields) {
		this.domainfields = domainfields;
	}

	public List<SubDomainField> getSubdomainfields() {
		return subdomainfields;
	}

	public void setSubdomainfields(List<SubDomainField> subdomainfields) {
		this.subdomainfields = subdomainfields;
	}

	public ListMaster getParentlist() {
		return parentlist;
	}

	public void setParentlist(ListMaster parentlist) {
		this.parentlist = parentlist;
	}

	public String getParentListValue() {
		return parentListValue;
	}

	public void setParentListValue(String parentListValue) {
		this.parentListValue = parentListValue;
	}
	
	
	
	
}
