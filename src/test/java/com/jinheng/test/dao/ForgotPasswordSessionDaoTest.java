package com.jinheng.test.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jinheng.fyp.DAO.ForgotPasswordSessionDAO;
import com.jinheng.fyp.DAO.PosUserDAO;
import com.jinheng.fyp.bean.ForgotPasswordSession;
import com.jinheng.fyp.util.Encryptor;
import com.jinheng.test.controller.BaseTestCase;

public class ForgotPasswordSessionDaoTest extends BaseTestCase {

	@Autowired
	private ForgotPasswordSessionDAO forgotPasswordSessionDAO;

	@Autowired
	private PosUserDAO posUserDAO;

	@Value("${userEmail}")
	private String userEmail;

	@Value("${oldPassword}")
	private String oldPassword;

	@Value("${newPassword}")
	private String newPassword;

	@Test
	public void testCreateForgetPasswordSession() {
		Long i = forgotPasswordSessionDAO.createForgetPasswordSession(userEmail, Encryptor.hashPassword(newPassword, userEmail));
		Assert.assertNotNull("ForgotPasswordSession failed to be created ", forgotPasswordSessionDAO.getForgetPasswordSessionByID(i));
	}

	@Test
	public void testGetPasswordSessionByID() {
		Long fpID = forgotPasswordSessionDAO.createForgetPasswordSession(userEmail, Encryptor.hashPassword(newPassword, userEmail));
		ForgotPasswordSession forgotPassworddSession = forgotPasswordSessionDAO.getForgetPasswordSessionByID(fpID);
		Assert.assertNotNull("ForgotPasswordSession with this ID doesnt exists", forgotPassworddSession);
	}

	@Test
	public void testUpdateForgotPasswordSession() {
		Long i = forgotPasswordSessionDAO.createForgetPasswordSession(userEmail, Encryptor.hashPassword(newPassword, userEmail));
		posUserDAO.updatePosUserForgetPasswordSession(userEmail, i);
		forgotPasswordSessionDAO.updateForgetPasswordSession(Encryptor.hashPassword(newPassword, userEmail), userEmail);
	}
}
