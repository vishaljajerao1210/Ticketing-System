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
@Table(name="domain_field")
public class DomainField extends AbstractEntityMaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonView(MasterView.DomainFieldView.class)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_name")
	String fieldName;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="is_mandatory")
	boolean isMandatory=false;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_enum")
	@Enumerated(EnumType.STRING)
	FieldEnum fieldEnum;
	
	
	
	

	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_type")
	String fieldType;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_label")
	String fieldLabel;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_description")
	String fieldDescription;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="is_list")
	boolean isList;
	
	@JsonView(MasterView.DomainFieldView.class)
	@ManyToOne
	@JoinColumn
	Domain domain;
	
	@JsonView(MasterView.DomainFieldView.class)
	@ManyToOne
	ListMaster listmaster;
	
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="field_status")
	@Enumerated(EnumType.STRING)
	FieldStatus fieldStatus;
	
	@JsonView(MasterView.DomainFieldView.class)
	@Column(name="is_fieldEnabled")
	boolean isFieldEnabled;
	
	
	
	
	
	
	

	public boolean isFieldEnabled() {
		return isFieldEnabled;
	}


	public void setFieldEnabled(boolean isFieldEnabled) {
		this.isFieldEnabled = isFieldEnabled;
	}


	public FieldStatus getFieldStatus() {
		return fieldStatus;
	}


	public void setFieldStatus(FieldStatus fieldStatus) {
		this.fieldStatus = fieldStatus;
	}


	public FieldEnum getFieldEnum() {
		return fieldEnum;
	}


	public void setFieldEnum(FieldEnum fieldEnum) {
		this.fieldEnum = fieldEnum;
	}


	public Domain getDomain() {
		return domain;
	}


	public int getId() {
		return id;
	}

	
	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
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

	public Domain getDomains() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public ListMaster getListmaster() {
		return listmaster;
	}

	public void setListmaster(ListMaster listmaster) {
		this.listmaster = listmaster;
	}
	
	
}
