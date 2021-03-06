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

@Controller
public class MobileRequestController extends AbstractController {

	private int SESSION_TIME_OUT = 60 * 60 * 24;

	private static final Logger logger = LoggerFactory.getLogger(MobileRequestController.class);

	@Autowired
	private ParkingUserService parkingUserService;

	@Autowired
	private MapService mapService;

	@Autowired
	private MessageSource messageSource;

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
			StringBuilder newJsonFromMobile = Encryptor.hidePasswordFromMobile(jsonFromMobile);
			StringBuilder maskedJSONFromMobile = Encryptor.hideSessionKeyFromMobile(newJsonFromMobile.toString());

			logger.debug("\nJSON sent from mobile app: {}", maskedJSONFromMobile);

			request.getSession().setMaxInactiveInterval(SESSION_TIME_OUT);

			// do process request and get the response JSON
			JSONServiceDTO requestObj = JSONFactory.create().fromJson(jsonFromMobile, JSONServiceDTO.class);

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
				respObj.setServiceName(requestObj.getServiceName());
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
			throw e;
		}
	}

	private JSONServiceDTO processRequest(JSONServiceDTO dto, final Locale locale, final HttpServletRequest request, final HttpServletResponse response)
			throws MyMobileRequestException {
		Assert.notNull(dto);

		ServiceNames serviceNameEnum = extractServiceName(dto);
		try {
			switch (serviceNameEnum) {
			case REGISTER:
				logger.debug("Entering {} service", ServiceNames.REGISTER);
				dto = parkingUserService.doRegister(dto.getEmail(), dto.getPassword());
				logger.debug("Current user set in session");
				break;
			case LOGIN:
				logger.debug("Entering {} service", ServiceNames.LOGIN);
				dto = parkingUserService.doLogIn(dto.getEmail(), dto.getPassword());
				logger.debug("Current user set in session");
				break;
			case LOGOUT:
				logger.debug("Entering {} service", ServiceNames.LOGOUT);
				request.getSession().invalidate();
				break;
			case FORGOT_PASSWORD:
				logger.debug("Entering {} service", ServiceNames.FORGOT_PASSWORD);
				dto = parkingUserService.doForgotPassword(dto.getEmail());
				break;
			case CHANGE_PASSWORD:
				logger.debug("Entering {} service", ServiceNames.CHANGE_PASSWORD);
				dto = parkingUserService.doChangePassword(dto.getEmail(), dto.getOldPassword(), dto.getPassword(), dto.isForceChangePassword());
				break;
			case FB_LOGIN:
				logger.debug("Entering {} service", ServiceNames.FB_LOGIN);
				dto = parkingUserService.doFBLogin(dto.getFacebookUID(), dto.getName());
				break;
			case GET_PARKING_LOTS:
				logger.debug("Entering {} service", ServiceNames.GET_PARKING_LOTS);
				dto = mapService.getParkingLots(dto.getLatitude(), dto.getLongitude(), dto.getGroupType(), dto.getCriteria());
				break;
			case PARK_VEHICLE:
				logger.debug("Entering {} service", ServiceNames.PARK_VEHICLE);
				dto = mapService.parkVehicle(dto.getSlot(), dto.getEmail(), dto.isForceRepark());
				break;
			case CHECK_VEHICLE:
				logger.debug("Entering {} service", ServiceNames.CHECK_VEHICLE);
				dto = mapService.checkVehicle(dto.getEmail());
				break;
			case REMOVE_VEHICLE:
				logger.debug("Entering {} service", ServiceNames.REMOVE_VEHICLE);
				dto = mapService.removeVehicle(dto.getEmail());
				break;
			case QR_SCANNED:
				logger.debug("Entering {} service", ServiceNames.QR_SCANNED);
				dto = mapService.updateOnQRScanned(dto.getSlot(), dto.getEmail());
				break;
			case SEND_FEEDBACK:
				logger.debug("Entering {} service", ServiceNames.SEND_FEEDBACK);
				parkingUserService.sendFeedback(dto.getFeedback());
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

	// fill all ErrorStatus inside messages_error
	private JSONServiceDTO getExceptionMessage(final Locale locale, final ErrorStatus errorStatus, final String serviceName) {
		String message = messageSource.getMessage(errorStatus.getKey(), errorStatus.getParam(), errorStatus.getDefaultMessage(), locale);
		JSONServiceDTO jsonToReturn = new JSONServiceDTO();
		jsonToReturn.setError(new JSONServiceError(errorStatus.getCode(), message));
		return jsonToReturn;
	}
}
