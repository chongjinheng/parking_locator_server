package com.jinheng.fyp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jinheng.fyp.DAO.PosUserDAO;
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
	private PosUserDAO posUserDAO;

	// @Autowired
	// private SessionService sessionService;

	// private int SESSION_TIME_OUT = 60 * 60 * 24;

	// public enum SESSION_KEYS {
	// KEY_PAIRS, SENDER_PUBLIC_KEY, CURRENT_LOGIN_STAFF
	// }
	//
	// public static final String COOKIE_NAME = "ztoken";

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

	// protected String generateAndSetSessionKey(final JSONServiceDTO dto) {
	// String sessionKey = generateSessionKey();
	//
	// // set session key into dto, won't return if error occured
	// dto.setSessionKey(sessionKey);
	//
	// return sessionKey;
	// }
	//
	// protected String generateSessionKey() {
	// // generate new session key
	// return UUID.randomUUID().toString();
	// }

	/**
	 * Sends the response back to the client. Phase 1,publicKey no encryption needed
	 */
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

	// protected void checkCookie(final HttpServletRequest request, final HttpServletResponse response) throws SessionTimedOutException {
	// String sessionCookieValue = null;
	//
	// // check cookie in session if it is valid
	// if (request.getSession() == null) {
	// handleSessionTimedOut(request, response);
	// }
	//
	// // get the cookie name from session
	// sessionCookieValue = (String) request.getSession().getAttribute(COOKIE_NAME);
	// if ((sessionCookieValue == null)) {
	// logger.error("No cookie in session, sessionCookieName: {}, sessionCookieValue: {}", COOKIE_NAME, sessionCookieValue);
	//
	// handleSessionTimedOut(request, response);
	// }
	//
	// // find the cookie
	// Cookie[] cookies = request.getCookies();
	// Cookie cookieFound = null;
	// if (cookies != null) {
	// for (Cookie c : cookies) {
	// if (COOKIE_NAME.equals(c.getName()) && StringUtils.hasText(c.getValue())) {
	// cookieFound = c;
	// break;
	// }
	// }
	// }
	// if (cookieFound == null) {
	// handleSessionTimedOut(request, response);
	// }
	//
	// // throw a timed out exception if the cookie value is invalid (different)
	// if (sessionCookieValue.equals(cookieFound.getValue()) == false) {
	// logger.debug("Timed out! sessionCookieValue: {} vs the cookie found {}", sessionCookieValue, cookieFound.getValue());
	//
	// handleSessionTimedOut(request, response);
	// }
	//
	// cookieFound.setPath("/");
	// cookieFound.setMaxAge(0);
	// cookieFound.setValue(null);
	// cookieFound.setSecure(false);
	// response.addCookie(cookieFound);
	// }
	//
	// protected Cookie generateAndSetCookie(final HttpServletResponse response) throws SessionTimedOutException {
	// Cookie cookie = generateCookie();
	//
	// // set the cookie into the response
	// response.addCookie(cookie);
	//
	// return cookie;
	// }
	//
	// protected Cookie generateCookie() throws SessionTimedOutException {
	// String newCookieValue;
	// newCookieValue = RandomStringUtils.randomNumeric(15);
	// Cookie userCookie = new Cookie(COOKIE_NAME, newCookieValue);
	// userCookie.setPath("/");
	// userCookie.setMaxAge(SESSION_TIME_OUT);
	// userCookie.setSecure(false);
	//
	// String maskedCookieValue = "***************" + newCookieValue.substring(newCookieValue.length() - 2, newCookieValue.length());
	// logger.debug("New cookie value for {}: {}", COOKIE_NAME, maskedCookieValue);
	//
	// return userCookie;
	// }
	//
	// protected void setCookieToSession(final HttpSession session, final Cookie cookie) {
	// session.setAttribute(COOKIE_NAME, cookie.getValue());
	// }

	protected void handleSessionTimedOut(final HttpServletRequest request, final HttpServletResponse response) throws SessionTimedOutException {
		response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);

		if (request.getSession() != null) {
			request.getSession().invalidate();
		}
		throw new SessionTimedOutException(ErrorStatus.SESSION_TIMED_OUT.getCode(), ErrorStatus.SESSION_TIMED_OUT.getDefaultMessage());
	}

	// set current user into the enum into the session so that in the future we can validate
	// protected void persistToSession(final JSONServiceDTO dto, final HttpServletRequest request, final String userEmail) {
	// PosUser staff = posUserDAO.getUserByEmail(userEmail);
	// request.getSession(true).setAttribute(SESSION_KEYS.CURRENT_LOGIN_STAFF.name(), staff);
	// }

	// protected void verifySessionData(final PosUser staff, final HttpServletRequest request, final JSONServiceDTO dto) throws SSPosMobileRequestException {
	//
	// final PosUser sStaff = (PosUser) request.getSession(true).getAttribute(SESSION_KEYS.CURRENT_LOGIN_STAFF.name());
	// // validate merchant ID and reader serial number before hand
	// // check that against the value in session
	// // invalidate the session, throw exception if invalid
	//
	// if (!StringUtils.hasText(dto.getEmail()) || !dto.getEmail().equals(staff.getEmail()) || !sStaff.getEmail().equalsIgnoreCase(staff.getEmail())) {
	//
	// request.getSession(true).invalidate();
	//
	// logger.info("session timed out due to invalid session information!");
	// if (logger.isDebugEnabled()) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Value in REQUEST vs value in SESSION /DB");
	// sb.append(String.format("\nStaff - %s - %s", sStaff.getEmail(), staff.getEmail()));
	// logger.debug(sb.toString());
	// }
	// throw new SSPosMobileRequestException(ErrorStatus.SESSION_TIMED_OUT, "session is timed out due to invalid session information or timedout");
	// }
	// }
	//
	// protected void setSessionKeyToSession(final HttpSession session, final String sessionKey, JSONServiceDTO dto) {
	// SessionService.getInstance().setNewKey(session, sessionKey, dto);
	//
	// }
	//
	// protected void checkSessionKey(final JSONServiceDTO dto, final HttpServletRequest request, final HttpServletResponse response) throws SessionTimedOutException {
	// // System.out.println("checkSessionKey::" + request.getSession(true).getAttributeNames());
	// // request.getSession(true).setAttribute(SESSION_KEYS.CURRENT_LOGIN_STAFF.name(), posUserDAO.getUserByEmail(dto.getEmail()));
	//
	// String serviceName = dto.getServiceName();
	// if (!serviceName.equals(ServiceNames.LOGIN.toString()) && !serviceName.equals(ServiceNames.SIGN_UP.toString())
	// && !serviceName.equals(ServiceNames.FORGOT_PASSWORD.toString())) {
	//
	// String sessionKey = sessionService.getSessionKey(request.getSession());
	// // by calling getsessionKey, validity is already check (session vs db)
	// if (!isSessionKeyValid(sessionKey, dto.getSessionKey())) {
	// logger.error("Session keys don't match: {}", dto.getSessionKey());
	// handleSessionTimedOut(request, response);
	// }
	// }
	//
	// }
	//
	// private boolean isSessionKeyValid(final String sessionKeyFromSession, final String sessionKeyFromJSON) {
	// boolean isSessionKeyValid = false;
	//
	// if (StringUtils.hasText(sessionKeyFromSession) && (sessionKeyFromSession.equalsIgnoreCase(sessionKeyFromJSON))) {
	// isSessionKeyValid = true;
	// } else {
	// logger.error("Session keys in session and JSON don't match");
	// }
	//
	// return isSessionKeyValid;
	// }

}
