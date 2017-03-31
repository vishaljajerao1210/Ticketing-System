package com.ticketingSystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;
import com.ticketingSystem.enums.FieldEnum;
import com.ticketingSystem.enums.FieldStatus;

@Entity
@Table(name = "subdomain_field")
public class SubDomainField extends AbstractEntityMaster{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView(MasterView.SubDomainFieldView.class)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_name")
	String fieldName;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_type")
	String fieldType;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_label")
	String fieldLabel;
	
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_description")
	String fieldDescription;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="is_list")
	boolean isList;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@ManyToOne
	ListMaster listmaster;
	
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="is_mandatory")
	boolean isMandatory=false;
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_enum")
	@Enumerated(EnumType.STRING)
	FieldEnum fieldEnum;
	
	
	@JsonView(MasterView.SubDomainFieldView.class)
	@Column(name="field_status")
	@Enumerated(EnumType.STRING)
	FieldStatus fieldStatus;
	
	

	
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="is_fieldEnabled")
	boolean isFieldEnabled;
	
	
	

	@JsonView(MasterView.SubDomainFieldView.class)
	@ManyToOne
	@JoinColumn
	SubDomain subdomains;
	
	
	
	
	public boolean isFieldEnabled() {
		return isFieldEnabled;
	}

	public void setFieldEnabled(boolean isFieldEnabled) {
		this.isFieldEnabled = isFieldEnabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}


	
	
	
	public FieldStatus getFieldStatus() {
		return fieldStatus;
	}

	public void setFieldStatus(FieldStatus fieldStatus) {
		this.fieldStatus = fieldStatus;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}


	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public SubDomain getSubdomains() {
		return subdomains;
	}

	public void setSubdomains(SubDomain subdomains) {
		this.subdomains = subdomains;
	}

	public ListMaster getListmaster() {
		return listmaster;
	}

	public void setListmaster(ListMaster listmaster) {
		this.listmaster = listmaster;
	}
	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public FieldEnum getFieldEnum() {
		return fieldEnum;
	}

	public void setFieldEnum(FieldEnum fieldEnum) {
		this.fieldEnum = fieldEnum;
	}

	
	
}
