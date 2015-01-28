package com.jinheng.fyp.service;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.DAO.ForgotPasswordSessionDAO;
import com.jinheng.fyp.DAO.ParkingUserDAO;
import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.EmailDetails;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.util.Encryptor;
import com.jinheng.fyp.util.Formatters;
import com.jinheng.fyp.util.Validators;

@Service
public class ParkingUserServiceImpl implements ParkingUserService {

	private static final Logger logger = LoggerFactory.getLogger(ParkingUserServiceImpl.class);

	@Autowired
	private ForgotPasswordSessionDAO forgotPasswordSessionDAO;

	@Autowired
	private ParkingUserDAO parkingUserDAO;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO doRegister(String email, String password) throws MyMobileRequestException, Exception {
		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			email = Validators.sanityCheck(email);
			password = Validators.sanityCheck(password);

			String encryptedPassword = Encryptor.hashPassword(password, email);

			// check for format
			if (!Validators.validateEmail(email)) {
				throw new MyMobileRequestException(ErrorStatus.EMAIL_STYLE_ERROR, ErrorStatus.EMAIL_STYLE_ERROR.getDefaultMessage() + " - doSignup");
			}

			if (parkingUserDAO.getUserByEmail(email) != null) {
				throw new MyMobileRequestException(ErrorStatus.EMAIL_EXISTS, ErrorStatus.EMAIL_EXISTS.getDefaultMessage());
			}

			if (!(Validators.checkPasswordStyle(password) && password.length() >= 8 && password.length() <= 15)) {
				throw new MyMobileRequestException(ErrorStatus.PASSWORD_STYLE_ERROR, ErrorStatus.PASSWORD_STYLE_ERROR.getDefaultMessage() + " - doSignup");
			}
			// salesRunningNumberDAO.createSalesRunningNumber(storeID);
			// logger.info("A Running Number that is dedicated to the store is created successfully");
			else {
				parkingUserDAO.createParkingUser(email, encryptedPassword);
				logger.info("User created sucessfully");

				dtoToReturn.setEmail(email);
				dtoToReturn.setLoginMode(0);

				EmailDetails details = new EmailDetails();
				details.setEmail(email);
				logger.info("Email sending....");
				emailService.sendEmail(email, 0, details);
				return dtoToReturn;
			}

		} catch (MyMobileRequestException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception found - doSignUp " + e);
			throw e;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO doLogIn(String email, String password) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			email = Validators.sanityCheck(email);
			password = Validators.sanityCheck(password);

			ParkingUser posUser = parkingUserDAO.getUserByEmail(email);
			if (posUser == null) {
				throw new MyMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doLogin");
			}
			logger.debug(email + " found in database");
			logger.debug(Encryptor.hashPassword(password, email));

			// if password entered is not same with the main password
			if (!(Validators.validatePassword(password, posUser.getPassword(), email))) {
				// check if there is an temporary password
				// if temporary password does not exists
				if (posUser.getForgotPasswordSession() == null) {
					throw new MyMobileRequestException(ErrorStatus.VALIDATION_ERROR, ErrorStatus.VALIDATION_ERROR.getDefaultMessage()
							+ " : Temporary password does exist - doLogin");
				}
				// if temporary password exists
				else if (!(posUser.getForgotPasswordSession() == null)) {
					// temporary password does not match
					if (!(Validators.validatePassword(password, posUser.getForgotPasswordSession().getTempPassword(), email))) {
						throw new MyMobileRequestException(ErrorStatus.VALIDATION_ERROR, ErrorStatus.VALIDATION_ERROR.getDefaultMessage()
								+ " : Temporary password does not match - doLogin");
					}
					// temporary password matches //then login
					else {
						// check validity of the date
						Date currentDate = new Date();
						Date tempPasswordCreatedDate = posUser.getForgotPasswordSession().getValidTillDate();
						// if temporary password expired
						if (currentDate.after(tempPasswordCreatedDate)) {
							throw new MyMobileRequestException(ErrorStatus.VALIDATION_ERROR, ErrorStatus.VALIDATION_ERROR.getDefaultMessage()
									+ " : Temporary password expired - doLogin");
						} // if temporary password is used
						else if (posUser.getForgotPasswordSession().getDeleteFlag() == true) {
							throw new MyMobileRequestException(ErrorStatus.VALIDATION_ERROR, ErrorStatus.VALIDATION_ERROR.getDefaultMessage()
									+ " : Temporary password already used - doLogin");
						} else {
							logger.info("Logged in using temporary password");
							dtoToReturn.setLoginMode(1);
							return dtoToReturn;
						}

					}
				}
			} else if ((Validators.validatePassword(password, posUser.getPassword(), email))) {
				// if input password is same as registered password
				dtoToReturn.setLoginMode(0);
				if (posUser.getForgotPasswordSession() != null) {
					forgotPasswordSessionDAO.deleteForgetPasswordSession(email);
				}
				logger.info("Logged in using normal password");

				return dtoToReturn;
			}

		} catch (MyMobileRequestException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception - doLogIn " + e);
			throw e;
		}
		return dtoToReturn;
	}

	@Override
	public JSONServiceDTO doFBLogin(String facebookUID, String userName) throws Exception {
		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			facebookUID = Validators.sanityCheck(facebookUID);

			ParkingUser posUser = parkingUserDAO.getUserByFacebookUID(facebookUID);
			if (posUser == null) {
				parkingUserDAO.createFacebookUser(facebookUID, userName);
				logger.debug("New facebook user created");
			}
			EmailDetails details = new EmailDetails();
			details.setEmail(userName);
			logger.info("Email sending....");
			emailService.sendEmail(userName, 0, details);

			logger.debug("Logged in with facebook");
			dtoToReturn.setLoginMode(2);
			return dtoToReturn;

		} catch (MyMobileRequestException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception - doLogIn " + e);
			throw e;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO doForgotPassword(String email) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			email = Validators.sanityCheck(email);

			ParkingUser parkingUser = parkingUserDAO.getUserByEmail(email);
			if (parkingUser == null) {
				throw new MyMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doForgotPassword");
			}
			logger.debug(email + " pulled from database");

			String tempPassword = RandomStringUtils.randomAlphanumeric(8);
			while (!(Validators.checkPasswordStyle(tempPassword))) {
				tempPassword = RandomStringUtils.randomAlphanumeric(8);
			}
			String encryptedTempPassword = Encryptor.hashPassword(tempPassword, email);
			logger.info("Temporary password created successfully");

			// if there is no previous record of forgot_session
			if (parkingUser.getForgotPasswordSession() == null) {
				Long forgotPasswordSessionID = forgotPasswordSessionDAO.createForgetPasswordSession(email, encryptedTempPassword);
				logger.info("New forgot password session created successfully");

				parkingUserDAO.updatePosUserForgetPasswordSession(email, forgotPasswordSessionID);
				logger.info("posUser forgot password session changed");

			} // if there is previous record
			else if (parkingUser.getForgotPasswordSession() != null) {
				forgotPasswordSessionDAO.updateForgetPasswordSession(encryptedTempPassword, email);
				logger.info("Temporary password found and overwritten");

			} else {
				throw new MyMobileRequestException(ErrorStatus.UNHANDLED_ERROR, ErrorStatus.UNHANDLED_ERROR.getDefaultMessage()
						+ " : force password true but password session does not exist - doForgotPassword");

			}
			EmailDetails details = new EmailDetails();
			details.setTemporaryPassword(tempPassword);
			details.setEmail(email);
			details.setTime(Formatters.formatOutputDate(new Date()));
			emailService.sendEmail(email, 1, details);

		} catch (MyMobileRequestException e) {
			throw e;
		} catch (Exception e) {
			logger.info("Exception - doForgotPass " + e);
			throw e;
		}
		return dtoToReturn;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO doChangePassword(String email, String oldPassword, String newPassword, Boolean isForceChangePassword) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			email = Validators.sanityCheck(email);
			oldPassword = Validators.sanityCheck(oldPassword);
			newPassword = Validators.sanityCheck(newPassword);

			ParkingUser parkingUser = parkingUserDAO.getUserByEmail(email);
			if (parkingUser == null) {
				throw new MyMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doChangePassword");
			}
			logger.debug(email + " found in database");

			if (!Validators.checkPasswordStyle(newPassword) && newPassword.length() >= 8 && newPassword.length() <= 15) {
				throw new MyMobileRequestException(ErrorStatus.PASSWORD_STYLE_ERROR, ErrorStatus.PASSWORD_STYLE_ERROR.getDefaultMessage());
			} // if it is called from entering a temporary password
			else if (isForceChangePassword == true) {
				if (parkingUser.getForgotPasswordSession() == null) {
					throw new Exception(" force changing password without password session - doForgotPassword");
				} else if (parkingUserDAO.getUserByEmail(email).getPassword().equals(Encryptor.hashPassword(newPassword, email))) {
					throw new MyMobileRequestException(ErrorStatus.PASSWORD_REQ_ERROR, ErrorStatus.PASSWORD_REQ_ERROR.getDefaultMessage()
							+ " : old and new password is same - doChangePassword");
				}

				String newHashedPassword = Encryptor.hashPassword(newPassword, email);
				parkingUserDAO.updatePosUserPassword(email, newHashedPassword);
				logger.info("Force password = true, password updated");

				forgotPasswordSessionDAO.deleteForgetPasswordSession(email);
				logger.info("Temporary password deleted");

				EmailDetails details = new EmailDetails();
				details.setEmail(email);
				details.setTransactionDate(Formatters.formatOutputDate(new Date()));
				emailService.sendEmail(email, 2, details);

			} else if (oldPassword.equals(newPassword)) {
				throw new MyMobileRequestException(ErrorStatus.PASSWORD_REQ_ERROR, ErrorStatus.PASSWORD_REQ_ERROR.getDefaultMessage()
						+ " : old and new password is same - doChangePassword");
			} else if (Validators.validatePassword(oldPassword, parkingUser.getPassword(), email)) {
				String newHashedPassword = Encryptor.hashPassword(newPassword, email);
				parkingUserDAO.updatePosUserPassword(email, newHashedPassword);
				logger.info("Password Changed");

				EmailDetails details = new EmailDetails();
				details.setEmail(email);
				details.setTransactionDate(Formatters.formatOutputDate(new Date()));
				emailService.sendEmail(email, 2, details);

			} else {
				throw new MyMobileRequestException(ErrorStatus.PASSWORD_INVALID, ErrorStatus.PASSWORD_INVALID.getDefaultMessage());
			}

		} catch (MyMobileRequestException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception - doChangePass " + e);
			throw e;
		}

		return dtoToReturn;

	}

	@Override
	public void sendFeedback(String feedback) throws Exception {
		EmailDetails details = new EmailDetails();
		details.setEmail("parking.locator.app@gmail.com");
		details.setFeedback(feedback);
		emailService.sendEmail("parking.locator.app@gmail.com", details);
	}
}
