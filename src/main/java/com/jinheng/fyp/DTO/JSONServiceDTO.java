package com.jinheng.fyp.DTO;

import java.util.List;

import com.jinheng.fyp.bean.Lot;

/**
 * General JSON Transfer Object
 * 
 * @author original author
 */
public class JSONServiceDTO {

	/****** GENERAL ******/
	private String serviceName;
	private JSONServiceError error;
	private String language;
	private String sessionKey;

	/***** IDs *****/
	private String facebookUID;

	/****** ACTIVATION/LOGIN ******/
	private String email;// email
	private String name;
	private String password;
	private String newPassword;
	private Integer loginMode;

	/****** MAPS ******/
	private Double longitude;
	private Double latitude;
	private String groupType;
	private String criteria;
	private List<Lot> parkingLots;

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Lot> getParkingLots() {
		return parkingLots;
	}

	public void setParkingLots(List<Lot> parkingLots) {
		this.parkingLots = parkingLots;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitute(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitute(Double latitude) {
		this.latitude = latitude;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public JSONServiceError getError() {
		return error;
	}

	public void setError(JSONServiceError error) {
		this.error = error;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getFacebookUID() {
		return facebookUID;
	}

	public void setFacebookUID(String facebookUID) {
		this.facebookUID = facebookUID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Integer getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(Integer loginMode) {
		this.loginMode = loginMode;
	}

}
