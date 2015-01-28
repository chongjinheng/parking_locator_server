package com.jinheng.test.controller;

import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.controller.MobileRequestController;
import com.jinheng.fyp.enums.ServiceNames;
import com.jinheng.fyp.util.JSONFactory;

public class MobileRequestControllerTest extends BaseTestCase {

	@Autowired
	private MobileRequestController ctlr = null;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	private Properties testValue;

	public void login() throws Exception {
		JSONServiceDTO dto = new JSONServiceDTO();
		dto.setServiceName(ServiceNames.LOGIN.name());
		dto.setEmail(testValue.getProperty("email"));
		dto.setPassword(testValue.getProperty("password"));

		String jsonFromMobile = JSONFactory.toJson(dto);
		request = newMobileRequest();
		response = newResponse();
		request.setCookies(response.getCookies());
		response = newResponse();
		ctlr.doProcessMobileRequest(request, jsonFromMobile, response, newLocale("en"));
	}

	@Test
	public void testService() throws Exception {
		/** User Services **/
		login();
	}
}
