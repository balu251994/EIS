package com.ey.hcp.jpa;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Document {
	
	@Id @GeneratedValue
	private String id;
	
	@Basic
	private String docName;
	
	@Basic
	private String docUploadedBy;
	
	@Basic
	private Date docUploadedDate;
	
	@Basic
	private String docId;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getId() {
		return id;
	}
	
	public String getDocName() {
		return docName;
	}

	public String getDocUploadedBy() {
		return docUploadedBy;
	}

	public Date getDocUploadedDate() {
		return docUploadedDate;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public void setDocUploadedBy(String docUploadedBy) {
		this.docUploadedBy = docUploadedBy;
	}

	public void setDocUploadedDate(Date docUploadedDate) {
		this.docUploadedDate = docUploadedDate;
	}
}