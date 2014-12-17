package com.jinheng.fyp.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User session store in database to determine who is accessing the database
 * 
 * @author chongjinheng
 * @author waikitchong
 */
@Entity
public class ParkingUserSession {

	private String username;

	private String sessionValue;

	private Date lastAccessTime;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * @return the userID
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the sessionValue
	 */
	public String getSessionValue() {
		return sessionValue;
	}

	/**
	 * @param sessionValue the sessionValue to set
	 */
	public void setSessionValue(String sessionValue) {
		this.sessionValue = sessionValue;
	}

	/**
	 * @return the lastAccessTime
	 */
	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
}
