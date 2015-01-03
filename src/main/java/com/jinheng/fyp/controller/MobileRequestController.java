package com.jinheng.fyp.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.DTO.JSONServiceError;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.enums.ServiceNames;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.exceptions.MyRecoverableException;
import com.jinheng.fyp.exceptions.SessionTimedOutException;
import com.jinheng.fyp.service.MapService;
import com.jinheng.fyp.service.ParkingUserService;
import com.jinheng.fyp.util.Encryptor;
import com.jinheng.fyp.util.JSONFactory;

/**
 * POS version of GenericMobileServiceController<br/>
 * Renamed to reflect more on this Class' role and avoid confusion
 * 
 * @author original author
 * @author azliabdullah
 */
@Controller
public class MobileRequestController extends AbstractController {

	private int SESSION_TIME_OUT = 60 * 60 * 24;

	private static final Logger logger = LoggerFactory.getLogger(MobileRequestController.class);
	// TODO later phase? perf logger
	// TODO later phase? smartdeviceversioning

	@Autowired
	private ParkingUserService parkingUserService;

	@Autowired
	private MapService mapService;

	// @Autowired
	// private SessionService sessionService;

	// TODO exception logger here in later phase?

	@Autowired
	private MessageSource messageSource;

	// TODO initResource and versioning later phase?

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	@ResponseBody
	public String defResp(final HttpServletResponse response) throws IOException {
		return "";
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.POST)
	public void doProcessMobileRequest(final HttpServletRequest request, @RequestBody final String jsonFromMobile, final HttpServletResponse response, final Locale locale)
			throws Exception {
		String serviceName = null;
		try {
			// TODO get rsa3, senderPublicKey from request's session. They are used for encryption/decryption. Not in Phase 1

			StringBuilder newJsonFromMobile = Encryptor.hidePasswordFromMobile(jsonFromMobile);
			StringBuilder maskedJSONFromMobile = Encryptor.hideSessionKeyFromMobile(newJsonFromMobile.toString());

			logger.debug("\nJSON sent from mobile app: {}", maskedJSONFromMobile);

			// checkCookie here
			// checkCookie(request, response);
			// logger.debug("Cookie validated");

			// setMaxInactiveInterval (when session will time out)
			request.getSession().setMaxInactiveInterval(SESSION_TIME_OUT);

			// do process request and get the response JSON
			// TODO later phase? decrypt the json before convert to JSONServiceDTO object then process
			// in Phase 1 directly convert to JSONServiceDTO object then process
			JSONServiceDTO requestObj = JSONFactory.create().fromJson(jsonFromMobile, JSONServiceDTO.class);

			// check whether the server session match with the dto session, then check with the db
			// checkSessionKey(requestObj, request, response);
			// logger.debug("Session validated");

			serviceName = requestObj.getServiceName();
			JSONServiceDTO respObj = processRequest(requestObj, locale, request, response);
			logger.debug("Request processed successfully");

			if (respObj.getError() != null) {
				String checker = respObj.getError().getCode();
				Integer errorCode = Integer.valueOf(checker);
				// error code between 5000 - 5999 && 8000 - 8999 are recoverable error, it's fine. user can retry
				if ((10001 == errorCode) || ((errorCode >= 5000) && (errorCode < 6000)) || ((errorCode >= 8000) && (errorCode < 9000))) {
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					// non recoverable - could be session prob, etc. forced user to re-login
					request.getSession().invalidate();
					response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);

				}
			}
			if (request.getSession() != null) {

				// if haven't login or signUp won't set session
				if (!(respObj.getError() != null && (serviceName.equals(ServiceNames.FORGOT_PASSWORD.toString()) || serviceName.equals(ServiceNames.LOGIN.toString()) || serviceName
						.equals(ServiceNames.SIGN_UP.toString())))) {
					// renew session key
					// String sessionKey = generateAndSetSessionKey(respObj);
					// logger.debug("New session key set in DTO");
					//
					// // renew session cookie
					// Cookie cookie = generateAndSetCookie(response);
					// logger.debug("New cookie added to response");
					//
					// // set the session key and cookie to the session
					// setCookieToSession(request.getSession(), cookie);
					// logger.debug("Cookie set in session");

					if (respObj.getEmail() == null) {
						respObj.setEmail(requestObj.getEmail());
					}
					respObj.setServiceName(requestObj.getServiceName());

					// this is the part where you pass current cookie data into the new session
					// this is the part you update db
					// setSessionKeyToSession(request.getSession(), sessionKey, respObj);
					// logger.debug("Session key set in database");
				}
			}

			// flushResponse will throw an IOException if client connection is dropped
			flushResponse(response, respObj);

		} catch (MyMobileRequestException e) {
			try {
				if (e.getErrorStatus() == ErrorStatus.SESSION_TIMED_OUT) {
					response.setStatus(HttpServletResponse.SC_FOUND);
				} else {
					response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
				}

				flushResponse(response, getExceptionMessage(locale, e.getErrorStatus(), serviceName));

				// TODO if want to save exception to incident table
			} catch (MyRecoverableException e1) {
				logger.error("Problem returning the response to client. Has the client disconnected to my app?");
				throw new RuntimeException(e1);
			}
			throw e;
		} catch (SessionTimedOutException e) {
			logger.error("Session Expired: ", e);
			response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);

			flushResponse(response, getExceptionMessage(locale, ErrorStatus.SESSION_TIMED_OUT, serviceName));
		} catch (Exception e) {
			try {
				logger.error("Error in method invocation: ", e);
				response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);

				flushResponse(response, getExceptionMessage(locale, ErrorStatus.ERROR_METHOD_INVOCATION_ERROR, serviceName));
			} catch (MyRecoverableException e1) {
				logger.error("Problem returning the response to client. Has the client disconnected to my app?");
				throw new RuntimeException(e1);
			}
			// TODO if want to save exception to incident table
			throw e;
		}
	}

	private JSONServiceDTO processRequest(JSONServiceDTO dto, final Locale locale, final HttpServletRequest request, final HttpServletResponse response)
			throws MyMobileRequestException {
		Assert.notNull(dto);

		ServiceNames serviceNameEnum = extractServiceName(dto);
		try {
			switch (serviceNameEnum) {
			case SIGN_UP:
				logger.debug("Entering {} service", ServiceNames.SIGN_UP);
				// dto = posUserService.doSignUp(dto.getStoreName(), dto.getEmail(), dto.getPassword());
				// persistToSession(dto, request, dto.getEmail());
				logger.debug("Current user set in session");
				break;
			case LOGIN:
				logger.debug("Entering {} service", ServiceNames.LOGIN);
				dto = parkingUserService.doLogIn(dto.getEmail(), dto.getPassword());
				// persistToSession(dto, request, dto.getEmail());
				logger.debug("Current user set in session");
				break;
			case LOGOUT:
				logger.debug("Entering {} service", ServiceNames.LOGOUT);
				request.getSession().invalidate();
				break;
			case FORGOT_PASSWORD:
				logger.debug("Entering {} service", ServiceNames.FORGOT_PASSWORD);
				// dto = posUserService.doForgotPassword(dto.getEmail());
				break;
			case CHANGE_PASSWORD:
				logger.debug("Entering {} service", ServiceNames.CHANGE_PASSWORD);
				// dto = posUserService.doChangePassword(dto.getEmail(), dto.getOldPassword(), dto.getNewPassword(), dto.isForceChangePassword());
				break;
			case FB_LOGIN:
				logger.debug("Entering {} service", ServiceNames.FB_LOGIN);
				dto = parkingUserService.doFBLogin(dto.getFacebookUID(), dto.getName());
				break;
			case GET_PARKING_LOTS:
				logger.debug("Entering {} service", ServiceNames.GET_PARKING_LOTS);
				dto = mapService.getParkingLots(dto.getLatitude(), dto.getLongitude(), dto.getGroupType(), dto.getCriteria());
				break;

			default:
				logger.error("Service name not found: " + serviceNameEnum);
				throw new MyMobileRequestException(ErrorStatus.ERROR_SERVICE_NOT_FOUND, "Service name not found: " + serviceNameEnum);
			}

		} catch (MyMobileRequestException e) {
			return getExceptionMessage(locale, e.getErrorStatus(), serviceNameEnum.name());
		} catch (Exception e) {
			logger.error("unhandled exception", e);
			return getExceptionMessage(locale, ErrorStatus.UNHANDLED_ERROR, serviceNameEnum.name());
		}
		return dto;
	}

	// TODO check platform version, checkPlatformVersion

	// fill all ErrorStatus inside messages_error
	private JSONServiceDTO getExceptionMessage(final Locale locale, final ErrorStatus errorStatus, final String serviceName) {
		String message = messageSource.getMessage(errorStatus.getKey(), errorStatus.getParam(), errorStatus.getDefaultMessage(), locale);
		JSONServiceDTO jsonToReturn = new JSONServiceDTO();
		jsonToReturn.setError(new JSONServiceError(errorStatus.getCode(), message));
		return jsonToReturn;
	}
}
