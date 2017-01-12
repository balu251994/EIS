package com.ey.hcp.jpa;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({@NamedQuery(name="getDoc", query="Select d from Document2 d where d.parentId=:id")})
@Entity 
public class Document2 {
	
	@Id @GeneratedValue
	private String SNo;
	
	@Basic
	private String docId;
	@Basic
	private String docName;
	@Basic
	private String DisplayName;
	@Basic
	private String mimeType;
	@Basic
	private Date uploadDate;
	
	@Basic
	private String parentId;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("docId=").append(docId)
		.append("  ,docName=").append(docName)
		.append("  ,DisplayName=").append(DisplayName)
		.append("  ,mimeType=").append(mimeType)
		.append("  ,parentId=").append(parentId);
		return sb.toString();
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getSNo() {
		return SNo;
	}

	public void setSNo(String sNo) {
		SNo = sNo;
	}
	
}

