package com.jinheng.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.service.PosUserService;
import com.jinheng.test.controller.BaseTestCase;

/**
 * A unit test for MobileRequestController.<br/>
 * In this unit test, we can test a mobile service requested from mobile app.
 * 
 * @author chongjinheng
 */
public class PosUserServiceTest extends BaseTestCase {

	@Autowired
	private PosUserService posUserService;

	@Value("${userEmail}")
	private String userEmail;

	@Value("${storeID}")
	private Long storeID;

	@Value("${storeName}")
	private String storeName;

	@Value("${password}")
	private String password;

	@Value("${firstTimeSignupEmail}")
	private String email;

	@Value("${oldPassword}")
	private String oldPassword;

	@Value("${newPassword}")
	private String newPassword;

	@Value("${forceChangePassword}")
	private Boolean forceChangePassword;

	@Value("${newStoreName}")
	private String newStoreName;

	@Value("${productImage3}")
	private String storeImage;

	JSONServiceDTO dto = new JSONServiceDTO();

	@Test
	public void testDoSignUp() throws Exception {
		// dto = posUserService.doSignUp(storeName, email, password);
	}

	@Test
	public void testDoLogIn() throws Exception {
		dto = posUserService.doLogIn(userEmail, password);
		Assert.assertNotNull("login mode is not returned", dto.getLoginMode());

		if (dto.getLoginMode() == 10) {
		} else if (dto.getLoginMode() == 20) {
			// Assert.assertEquals("wrong user's store name", "my store 1", dto.getUserProfile().getStoreName());
		}
	}

	@Test
	public void testDoForgotPass() throws Exception {
		// dto = posUserService.doForgotPassword(userEmail);
	}

	@Test
	public void testDoChangePass() throws Exception {
		// dto = posUserService.doChangePassword(userEmail, oldPassword, newPassword, forceChangePassword);
	}

	@Test
	public void testDoUpdateProfile() throws Exception {
		// posUserService.doUpdateProfile(userEmail, storeID, newStoreName, storeImage);
	}
}
