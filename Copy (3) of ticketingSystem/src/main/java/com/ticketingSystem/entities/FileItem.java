package com.ticketingSystem.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.ticketingSystem.Views.MasterView;


@Entity
@Table(name="file_items")
public class FileItem {
	
@JsonView({MasterView.FileItemView.class})
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
		
@JsonView({MasterView.FileItemView.class})
	@Column(name="file_name")
	String fileName;
	
@JsonView({MasterView.FileItemView.class})
	@Column(name="file_path")
	String filePath;
		
@JsonView({MasterView.FileItemView.class})
	@Column(name="canDelete",columnDefinition = "boolean default true")
	public Boolean canDelete=true;
	
		
@JsonView({MasterView.FileItemView.class})
	@ManyToOne
	FileUpload fileupload;
	
@JsonView({MasterView.FileItemView.class})
	@Column(name="uploaded_date")
	Date uploadedDate;

@JsonView({MasterView.FileItemView.class})
	@Column(name="uploaded_time")
	String uploadedTime;
	
	public FileUpload getFileupload() {
		return fileupload;
	}

	public void setFileupload(FileUpload fileupload) {
		this.fileupload = fileupload;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getUploadedTime() {
		return uploadedTime;
	}

	public void setUploadedTime(String uploadedTime) {
		this.uploadedTime = uploadedTime;
	}
	
	
	
	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}
}
