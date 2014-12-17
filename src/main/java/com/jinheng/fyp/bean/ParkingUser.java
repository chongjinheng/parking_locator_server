package com.jinheng.fyp.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ParkingUser extends BasicTable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userID;

	@Column(unique = true)
	private String email;

	private String userName;

	private String password;

	@Column(unique = true)
	private String facebookUID;

	@OneToOne
	private ForgotPasswordSession forgotPasswordSession;

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFacebookUID() {
		return facebookUID;
	}

	public void setFacebookUID(String facebookUID) {
		this.facebookUID = facebookUID;
	}

	public ForgotPasswordSession getForgotPasswordSession() {
		return forgotPasswordSession;
	}

	public void setForgotPasswordSession(ForgotPasswordSession forgotPasswordSession) {
		this.forgotPasswordSession = forgotPasswordSession;
	}

}
