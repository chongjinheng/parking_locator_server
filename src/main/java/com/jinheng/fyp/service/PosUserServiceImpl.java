package com.jinheng.fyp.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.DAO.CrudDAO;
import com.jinheng.fyp.DAO.ForgotPasswordSessionDAO;
import com.jinheng.fyp.DAO.PosUserDAO;
import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.util.Validators;

/**
 * Pos User Services, contains signup, login, forgotPassword, changePassword and update profile
 * 
 * @author chongjinheng
 */
@Service
public class PosUserServiceImpl implements PosUserService {

	private static final Logger logger = LoggerFactory.getLogger(PosUserServiceImpl.class);

	@SuppressWarnings("rawtypes")
	@Autowired
	private CrudDAO crudDAO;

	@Autowired
	private ForgotPasswordSessionDAO forgotPasswordSessionDAO;

	@Autowired
	private PosUserDAO posUserDAO;

	// @Autowired
	// private EmailService emailService;

	// @Override
	// @Transactional(rollbackFor = Exception.class)
	// public JSONServiceDTO doSignUp(String storeName, String email, String password) throws SSPosMobileRequestException, Exception {
	// JSONServiceDTO dtoToReturn = new JSONServiceDTO();
	// try {
	// /** Sanity Check **/
	// email = Validators.sanityCheck(email);
	// password = Validators.sanityCheck(password);
	// storeName = Validators.sanityCheck(storeName);
	//
	// String encryptedPassword = Encryptor.hashPassword(password, email);
	//
	// // check for format
	// if (!Validators.validateEmail(email)) {
	// throw new SSPosMobileRequestException(ErrorStatus.EMAIL_STYLE_ERROR, ErrorStatus.EMAIL_STYLE_ERROR.getDefaultMessage() + " - doSignup");
	// }
	//
	// if (posUserDAO.getUserByEmail(email) != null) {
	// throw new SSPosMobileRequestException(ErrorStatus.EMAIL_EXISTS, ErrorStatus.EMAIL_EXISTS.getDefaultMessage());
	// }
	//
	// if (!(Validators.checkPasswordStyle(password) && password.length() >= 8 && password.length() <= 15)) {
	// throw new SSPosMobileRequestException(ErrorStatus.PASSWORD_STYLE_ERROR, ErrorStatus.PASSWORD_STYLE_ERROR.getDefaultMessage() + " - doSignup");
	// }
	//
	// Long storeID = storeDAO.createStore(storeName, email);
	// dtoToReturn.setStoreId(storeID);
	// logger.info("Store created successfully");
	//
	// salesRunningNumberDAO.createSalesRunningNumber(storeID);
	// logger.info("A Running Number that is dedicated to the store is created successfully");
	//
	// posUserDAO.createPosUser(email, encryptedPassword, storeID);
	// logger.info("User created sucessfully");
	//
	// dtoToReturn.setEmail(email);
	//
	// EmailDetails details = new EmailDetails();
	// details.setEmail(email);
	// logger.info("Email sending....");
	// emailService.sendEmail(email, 0, details);
	//
	// } catch (SSPosMobileRequestException e) {
	// throw e;
	// } catch (Exception e) {
	// logger.error("Exception found - doSignUp " + e);
	// throw e;
	// }
	// return dtoToReturn;
	// }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO doLogIn(String email, String password) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			/** Sanity Check **/
			email = Validators.sanityCheck(email);
			password = Validators.sanityCheck(password);

			ParkingUser posUser = posUserDAO.getUserByEmail(email);
			if (posUser == null) {
				throw new MyMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doLogin");
			}
			logger.debug(email + " found in database");

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
							dtoToReturn.setLoginMode(10);
							return dtoToReturn;
						}

					}
				}
			} else if ((Validators.validatePassword(password, posUser.getPassword(), email))) {
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
}

// @Override
// @Transactional(rollbackFor = Exception.class)
// public JSONServiceDTO doForgotPassword(String email) throws SSPosMobileRequestException, Exception {
//
// JSONServiceDTO dtoToReturn = new JSONServiceDTO();
// try {
// /** Sanity Check **/
// email = Validators.sanityCheck(email);
//
// PosUser posUser = posUserDAO.getUserByEmail(email);
// if (posUser == null) {
// throw new SSPosMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doForgotPassword");
// }
// logger.debug(email + " pulled from database");
//
// String tempPassword = RandomStringUtils.randomAlphanumeric(8);
// while (!(Validators.checkPasswordStyle(tempPassword))) {
// tempPassword = RandomStringUtils.randomAlphanumeric(8);
// }
// String encryptedTempPassword = Encryptor.hashPassword(tempPassword, email);
// logger.info("Temporary password created successfully");
//
// // if there is no previous record of forgot_session
// if (posUser.getForgetPasswordSession() == null) {
// Long forgotPasswordSessionID = forgetPasswordSessionDAO.createForgetPasswordSession(email, encryptedTempPassword);
// logger.info("New forgot password session created successfully");
//
// posUserDAO.updatePosUserForgetPasswordSession(email, forgotPasswordSessionID);
// logger.info("posUser forgot password session changed");
//
// } // if there is previous record
// else if (posUser.getForgetPasswordSession() != null) {
// forgetPasswordSessionDAO.updateForgetPasswordSession(encryptedTempPassword, email);
// logger.info("Temporary password found and overwritten");
//
// } else {
// throw new SSPosMobileRequestException(ErrorStatus.UNHANDLED_ERROR, ErrorStatus.UNHANDLED_ERROR.getDefaultMessage()
// + " : force password true but password session does not exist - doForgotPassword");
//
// }
// EmailDetails details = new EmailDetails();
// details.setTemporaryPassword(tempPassword);
// details.setEmail(email);
// details.setTime(Formatters.formatOutputDate(new Date()));
// emailService.sendEmail(email, 1, details);
//
// } catch (SSPosMobileRequestException e) {
// throw e;
// } catch (Exception e) {
// logger.info("Exception - doForgotPass " + e);
// throw e;
// }
// return dtoToReturn;
//
// }
//
// @Override
// @Transactional(rollbackFor = Exception.class)
// public JSONServiceDTO doChangePassword(String email, String oldPassword, String newPassword, Boolean isForceChangePassword) throws SSPosMobileRequestException, Exception {
//
// JSONServiceDTO dtoToReturn = new JSONServiceDTO();
// try {
// /** Sanity Check **/
// email = Validators.sanityCheck(email);
// oldPassword = Validators.sanityCheck(oldPassword);
// newPassword = Validators.sanityCheck(newPassword);
//
// PosUser posUser = posUserDAO.getUserByEmail(email);
// if (posUser == null) {
// throw new SSPosMobileRequestException(ErrorStatus.USER_DOES_NOT_EXIST, ErrorStatus.USER_DOES_NOT_EXIST.getDefaultMessage() + " - doChangePassword");
// }
// logger.debug(email + " found in database");
//
// if (!Validators.checkPasswordStyle(newPassword) && newPassword.length() >= 8 && newPassword.length() <= 15) {
// throw new SSPosMobileRequestException(ErrorStatus.PASSWORD_STYLE_ERROR, ErrorStatus.PASSWORD_STYLE_ERROR.getDefaultMessage());
// } // if it is called from entering a temporary password
// else if (isForceChangePassword == true) {
// if (posUser.getForgetPasswordSession() == null) {
// throw new Exception(" force changing password without password session - doForgotPassword");
// } else if (posUserDAO.getUserByEmail(email).getPassword().equals(Encryptor.hashPassword(newPassword, email))) {
// throw new SSPosMobileRequestException(ErrorStatus.PASSWORD_REQ_ERROR, ErrorStatus.PASSWORD_REQ_ERROR.getDefaultMessage()
// + " : old and new password is same - doChangePassword");
// }
//
// String newHashedPassword = Encryptor.hashPassword(newPassword, email);
// posUserDAO.updatePosUserPassword(email, newHashedPassword);
// logger.info("Force password = true, password updated");
//
// forgetPasswordSessionDAO.deleteForgetPasswordSession(email);
// logger.info("Temporary password deleted");
// EmailDetails details = new EmailDetails();
// details.setEmail(email);
// details.setTransactionDate(Formatters.formatOutputDate(new Date()));
// emailService.sendEmail(email, 2, details);
// dtoToReturn.setUserProfile(posUser.getStore());
// logger.info("Store is returned");
//
// } else if (oldPassword.equals(newPassword)) {
// throw new SSPosMobileRequestException(ErrorStatus.PASSWORD_REQ_ERROR, ErrorStatus.PASSWORD_REQ_ERROR.getDefaultMessage()
// + " : old and new password is same - doChangePassword");
// } else if (Validators.validatePassword(oldPassword, posUser.getPassword(), email)) {
// String newHashedPassword = Encryptor.hashPassword(newPassword, email);
// posUserDAO.updatePosUserPassword(email, newHashedPassword);
// logger.info("Password Changed");
//
// EmailDetails details = new EmailDetails();
// details.setEmail(email);
// details.setTransactionDate(Formatters.formatOutputDate(new Date()));
// emailService.sendEmail(email, 2, details);
//
// } else {
// throw new SSPosMobileRequestException(ErrorStatus.PASSWORD_INVALID, ErrorStatus.PASSWORD_INVALID.getDefaultMessage());
// }
// dtoToReturn.setStoreId(posUser.getStore().getID());
// dtoToReturn.setUserProfile(posUser.getStore());
//
// } catch (SSPosMobileRequestException e) {
// throw e;
// } catch (Exception e) {
// logger.error("Exception - doChangePass " + e);
// throw e;
// }
//
// return dtoToReturn;
//
// }
//
// @Override
// @Transactional(rollbackFor = Exception.class)
// public JSONServiceDTO doUpdateProfile(String email, Long storeId, String storeName, String storeImage) throws SSPosMobileRequestException, Exception {
// JSONServiceDTO dtoToReturn = new JSONServiceDTO();
// try {
// /** Sanity Check **/
// email = Validators.sanityCheck(email);
// storeName = Validators.sanityCheck(storeName);
//
// PosUser posUser = posUserDAO.getUserByEmail(email);
// Store store = new Store();
//
// if (posUser != null) {
// logger.debug(email + " found in database");
// store = posUser.getStore();
// if (store == null) {
// throw new SSPosMobileRequestException(ErrorStatus.UNHANDLED_ERROR, ErrorStatus.UNHANDLED_ERROR.getDefaultMessage() + " : user does not have a store");
// } else if (store.getID() != storeId) {
// throw new SSPosMobileRequestException(ErrorStatus.ACCESS_DENIED, ErrorStatus.ACCESS_DENIED.getDefaultMessage() + " - doUpdateProfile");
// }
// } else {
// throw new SSPosMobileRequestException(ErrorStatus.UNHANDLED_ERROR, ErrorStatus.UNHANDLED_ERROR.getDefaultMessage() + " :email not registered - doUpdateProfile");
// }
//
// storeDAO.updateUserProfile(email, storeName, storeImage);
// logger.info("Profile updated successfully");
// } catch (SSPosMobileRequestException e) {
// throw e;
// } catch (Exception e) {
// logger.error("Exception - doUpdateProfile ");
// throw e;
// }
// return dtoToReturn;
// }
// }
