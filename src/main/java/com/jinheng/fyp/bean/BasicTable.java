package com.jinheng.fyp.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import com.jinheng.fyp.util.JSONExclusion;

/**
 * Basic class that will be extended from other children classes. <br>
 * Field: createdDate, modifiedDate, createdBy, modifiedBy
 * 
 * @param Date, Date, String, String
 * @author chengyang
 */

@MappedSuperclass
public abstract class BasicTable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Version
	@JSONExclusion
	protected int version = 1;

	@JSONExclusion
	protected Date createdDate;

	@JSONExclusion
	protected Date modifiedDate;

	@JSONExclusion
	protected String createdBy;

	@JSONExclusion
	protected String modifiedBy;

	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@JSONExclusion
	protected Boolean deleteFlag = false;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
