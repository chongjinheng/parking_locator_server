package com.jinheng.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.service.ParkingUserService;
import com.jinheng.test.controller.BaseTestCase;

public class ParkingUserServiceTest extends BaseTestCase {

	@Autowired
	private ParkingUserService posUserService;

	@Value("${email}")
	private String userEmail;

	@Value("${password}")
	private String password;

	@Value("${oldPassword}")
	private String oldPassword;

	@Value("${newPassword}")
	private String newPassword;

	@Value("${forceChangePassword}")
	private Boolean forceChangePassword;

	JSONServiceDTO dto = new JSONServiceDTO();

	@Test
	public void testDoSignUp() throws Exception {
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
	}

	@Test
	public void testDoChangePass() throws Exception {
	}
}
