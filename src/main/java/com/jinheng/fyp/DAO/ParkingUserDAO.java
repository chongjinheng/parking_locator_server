package com.jinheng.fyp.DAO;

import com.jinheng.fyp.bean.ParkingUser;

/**
 * Pos User DAO Class
 * 
 * @author original author
 * @author Darren
 * @author chengyang
 */
public interface ParkingUserDAO {

	public ParkingUser getUserByEmail(String email);

	public ParkingUser getUserByFacebookUID(String UID);

	public Long createPosUser(String userEmail, String hashedPassword, Long storeID);

	public void updatePosUserForgetPasswordSession(String userEmail, Long forgotPasswordSessionID);

	public void updatePosUserPassword(String userEmail, String newHashedPassword);

}
