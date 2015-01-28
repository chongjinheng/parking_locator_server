package com.jinheng.fyp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jinheng.fyp.DAO.ParkingUserDAO;
import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.enums.ServiceNames;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.exceptions.MyRecoverableException;
import com.jinheng.fyp.exceptions.SessionTimedOutException;
import com.jinheng.fyp.util.Encryptor;
import com.jinheng.fyp.util.JSONFactory;

public abstract class AbstractController {

	@Autowired
	private ParkingUserDAO posUserDAO;

	private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * Perform checking if the service requested is valid
	 * 
	 * @param dto
	 * @return ServiceNames
	 * @throws MyMobileRequestException
	 *             if service name is invalid
	 */
	protected ServiceNames extractServiceName(final JSONServiceDTO dto) throws MyMobileRequestException {
		String serviceNameString = null;
		try {
			serviceNameString = dto.getServiceName();
			if (!StringUtils.hasText(serviceNameString)) {
				// no text?
				throw new IllegalArgumentException("invalid service name");
			}
			return ServiceNames.valueOf(serviceNameString);
		} catch (IllegalArgumentException e) {
			String msg = "service name : " + serviceNameString + " is not found";
			logger.error(msg, e);

			throw new MyMobileRequestException(ErrorStatus.ERROR_SERVICE_NOT_FOUND, msg);
		}
	}

	protected void flushResponse(final HttpServletResponse response, final JSONServiceDTO respObj) throws MyRecoverableException, MyMobileRequestException, Exception {
		try {
			String responseJSON = JSONFactory.toJson(respObj);

			StringBuilder maskedPasswordResponseJSON = Encryptor.hidePassword(responseJSON);
			StringBuilder maskedResponseJSON = Encryptor.hideSessionKey(maskedPasswordResponseJSON.toString());
			logger.debug("\nJSON to return to mobile app: {}", maskedResponseJSON);

			response.setContentType("text/plain");
			response.getWriter().println(responseJSON);
			response.flushBuffer();

			response.getWriter().println("   ");
			response.flushBuffer();
		} catch (IOException e) {
			throw new MyRecoverableException(ErrorStatus.ERROR_CLIENT_DISCONNECTED.getCode(), ErrorStatus.ERROR_CLIENT_DISCONNECTED.getDefaultMessage());
		}
	}

	protected void handleSessionTimedOut(final HttpServletRequest request, final HttpServletResponse response) throws SessionTimedOutException {
		response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);

		if (request.getSession() != null) {
			request.getSession().invalidate();
		}
		throw new SessionTimedOutException(ErrorStatus.SESSION_TIMED_OUT.getCode(), ErrorStatus.SESSION_TIMED_OUT.getDefaultMessage());
	}
}
