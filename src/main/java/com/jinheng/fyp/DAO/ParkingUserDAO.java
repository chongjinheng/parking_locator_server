package com.jinheng.fyp.DAO;

import com.jinheng.fyp.bean.ParkingUser;

public interface ParkingUserDAO {

	public ParkingUser getUserByEmail(String email);

	public ParkingUser getUserByFacebookUID(String UID);

	public Long createParkingUser(String userEmail, String hashedPassword);

	public void updatePosUserForgetPasswordSession(String userEmail, Long forgotPasswordSessionID);

	public void updatePosUserPassword(String userEmail, String newHashedPassword);

	public Long createFacebookUser(String facebookUID, String userName);

}
