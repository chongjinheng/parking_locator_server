package com.jinheng.fyp.DAO;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.ForgotPasswordSession;
import com.jinheng.fyp.bean.ParkingUser;

/**
 * Pos User DAO Class
 * 
 * @author original author
 * @author Darren
 * @author chengyang
 */
@Repository
@Transactional(readOnly = true)
public class ParkingUserDAOImpl extends GenericDAO implements ParkingUserDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CrudDAO crudDAO;

	@Autowired
	private ForgotPasswordSessionDAO forgetPasswordSessionDAO;

	@Override
	public ParkingUser getUserByEmail(String email) {
		return (ParkingUser) getSessionFactory().createCriteria(ParkingUser.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("email", email)).uniqueResult();
	}

	@Override
	public ParkingUser getUserByFacebookUID(String UID) {
		return (ParkingUser) getSessionFactory().createCriteria(ParkingUser.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("facebookUID", UID))
				.uniqueResult();
	}

	@Override
	public Long createPosUser(String userEmail, String hashedPassword, Long storeID) {
		ParkingUser posUser = new ParkingUser();
		posUser.setCreatedBy(userEmail);
		posUser.setEmail(userEmail);
		posUser.setCreatedDate(new Date());
		posUser.setPassword(hashedPassword);

		return crudDAO.create(posUser);
	}

	@Override
	public void updatePosUserForgetPasswordSession(String userEmail, Long forgotPasswordSessionID) {
		ParkingUser posUser = getUserByEmail(userEmail);
		ForgotPasswordSession forgetPasswordSession = forgetPasswordSessionDAO.getForgetPasswordSessionByID(forgotPasswordSessionID);
		forgetPasswordSession.setModifiedDate(new Date());
		forgetPasswordSession.setModifiedBy(userEmail);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date validTillDate = c.getTime();
		forgetPasswordSession.setValidTillDate(validTillDate);
		posUser.setForgotPasswordSession(forgetPasswordSession);
		posUser.setModifiedBy(userEmail);
		posUser.setModifiedDate(new Date());
		crudDAO.update(posUser);
	}

	@Override
	public void updatePosUserPassword(String userEmail, String newHashedPassword) {
		ParkingUser posUser = getUserByEmail(userEmail);
		posUser.setModifiedDate(new Date());
		posUser.setModifiedBy(userEmail);
		posUser.setPassword(newHashedPassword);
		crudDAO.update(posUser);
	}

}
