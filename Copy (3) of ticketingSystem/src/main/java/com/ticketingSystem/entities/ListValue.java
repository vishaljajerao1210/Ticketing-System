package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;

@Entity
@Table(name ="listvalues")
public class ListValue extends AbstractEntityMaster {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Column(name="list_item_storage_value")
	String listItemStorageValue;
	
	
	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Column(name="list_item_display_value")
	String listItemDisplayValue;
	
	@JsonView({MasterView.ListMasterView.class,MasterView.ListValueView.class})
	@Column(name="list_item_description")
	String listItemDescription;
	
	@JsonView(MasterView.ListValueView.class)
	@ManyToOne
	@JoinColumn
	ListMaster listmaster;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getListItemStorageValue() {
		return listItemStorageValue;
	}


	public void setListItemStorageValue(String listItemStorageValue) {
		this.listItemStorageValue = listItemStorageValue;
	}


	public String getListItemDisplayValue() {
		return listItemDisplayValue;
	}


	public void setListItemDisplayValue(String listItemDisplayValue) {
		this.listItemDisplayValue = listItemDisplayValue;
	}


	public String getListItemDescription() {
		return listItemDescription;
	}


	public void setListItemDescription(String listItemDescription) {
		this.listItemDescription = listItemDescription;
	}


	public ListMaster getListmaster() {
		return listmaster;
	}


	public void setListmaster(ListMaster listmaster) {
		this.listmaster = listmaster;
	}
	
	
	
	
	
}
