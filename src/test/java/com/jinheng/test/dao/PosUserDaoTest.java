package com.jinheng.test.dao;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jinheng.fyp.DAO.ForgotPasswordSessionDAO;
import com.jinheng.fyp.DAO.ParkingUserDAO;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.util.Encryptor;
import com.jinheng.test.controller.BaseTestCase;

/**
 * A unit test for PosUserDao.
 * 
 * @author azliabdullah
 * @author chengyang
 */
public class PosUserDaoTest extends BaseTestCase {

	@Autowired
	private ParkingUserDAO posUserDAO;

	@Autowired
	private ForgotPasswordSessionDAO forgotPasswordSessionDAO;

	@Value("${userEmail}")
	private String userEmail;

	@Value("${firstTimeSignupEmail}")
	private String email;

	@Value("${newPassword}")
	private String password;

	@Value("${storeID}")
	private Long storeID;

	@Test
	public void testGetUserByEmail() throws Exception {
		ParkingUser posUser = posUserDAO.getUserByEmail(userEmail);
		Assert.assertNotNull("Pos User with this email not exist", posUser);

		// Test email not exist
		// posUser = posUserDAO.getUserByEmail(emailNotExist);
		// Assert.assertNull("Pos User with this email exist", posUser);

	}

	@Test
	public void testCreatePosUser() {
		posUserDAO.createPosUser(email, Encryptor.hashPassword(password, email), storeID);
		ParkingUser posUser = posUserDAO.getUserByEmail(email);
		Assert.assertNotNull("Created fail", posUser);
	}

	@Test
	public void updatePosUserForgetPasswordSession() {
		Long fpID = forgotPasswordSessionDAO.createForgetPasswordSession(userEmail, Encryptor.hashPassword(password, userEmail));
		posUserDAO.updatePosUserForgetPasswordSession(userEmail, fpID);
	}

	@Test
	public void testUpdatePosUserPassword() {
		posUserDAO.updatePosUserPassword(userEmail, Encryptor.hashPassword(password, userEmail));
		ParkingUser posUser = posUserDAO.getUserByEmail(userEmail);
		Assert.assertEquals(Encryptor.hashPassword(password, userEmail), posUser.getPassword());
	}
}
