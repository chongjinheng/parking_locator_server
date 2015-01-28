package com.jinheng.fyp.DAO;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.ForgotPasswordSession;
import com.jinheng.fyp.bean.ParkingUser;

@Repository
@Transactional(readOnly = true)
public class ForgotPasswordSessionDAOImpl extends GenericDAO implements ForgotPasswordSessionDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CrudDAO crudDAO;

	@Autowired
	private ParkingUserDAO parkingUserDao;

	@Override
	public Long createForgetPasswordSession(String email, String hasedTempPassword) {
		ForgotPasswordSession fpsession = new ForgotPasswordSession();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date validDate = c.getTime();
		fpsession.setTempPassword(hasedTempPassword);
		fpsession.setCreatedBy(email);
		fpsession.setCreatedDate(new Date());
		fpsession.setValidTillDate(validDate);
		return crudDAO.create(fpsession);

	}

	@Override
	public ForgotPasswordSession getForgetPasswordSessionByID(Long ID) {
		return (ForgotPasswordSession) getSessionFactory().createCriteria(ForgotPasswordSession.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("id", ID))
				.uniqueResult();
	}

	@Override
	public void updateForgetPasswordSession(String hashedTempPassword, String userEmail) {
		ParkingUser posUser = parkingUserDao.getUserByEmail(userEmail);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date validDate = c.getTime();

		ForgotPasswordSession fpsession = posUser.getForgotPasswordSession();
		fpsession.setTempPassword(hashedTempPassword);
		fpsession.setModifiedBy(userEmail);
		fpsession.setModifiedDate(new Date());
		fpsession.setValidTillDate(validDate);
		fpsession.setDeleteFlag(false);
		crudDAO.update(fpsession);
	}

	@Override
	public void deleteForgetPasswordSession(String userEmail) {
		ParkingUser posUser = parkingUserDao.getUserByEmail(userEmail);
		ForgotPasswordSession forgotPassSession = posUser.getForgotPasswordSession();
		forgotPassSession.setDeleteFlag(true);
		crudDAO.update(forgotPassSession);

	}

}
