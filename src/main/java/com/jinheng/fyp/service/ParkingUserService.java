package com.jinheng.fyp.service;

import com.jinheng.fyp.DTO.JSONServiceDTO;

/**
 * Pos User Services
 * 
 * @author Darren
 * @author cylim
 */
public interface ParkingUserService {

	// public JSONServiceDTO doSignUp(String storeName, String email, String password) throws Exception;

	public JSONServiceDTO doLogIn(String email, String password) throws Exception;

	public JSONServiceDTO doFBLogin(String facebookUID) throws Exception;

	// public JSONServiceDTO doForgotPassword(String email) throws Exception;
	//
	// public JSONServiceDTO doChangePassword(String email, String oldPassword, String newPassword, Boolean isForceChangePassword) throws Exception;
	//
	// public JSONServiceDTO doUpdateProfile(String email, Long storeId, String storeName, String storeImage) throws Exception;

}
