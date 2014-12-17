package com.jinheng.fyp.DAO;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.ParkingUserSession;

/**
 * Session DAO Class
 * 
 * @author original author
 * @author chongjinheng
 */
@Component
@Repository
@Transactional(readOnly = false)
public class SessionDAOImpl extends GenericDAO implements SessionDAO {

	public void updateSession(String sessionKey, String username) {
		ParkingUserSession session = getSessionBean(username);
		session.setSessionValue(sessionKey);
		getSessionFactory().saveOrUpdate(session);
	}

	public ParkingUserSession checkSessionValidity(String sessionKey, String username) {
		return (ParkingUserSession) getSessionFactory().createCriteria(ParkingUserSession.class).add(Restrictions.eq("username", username))
				.add(Restrictions.eq("sessionValue", sessionKey)).uniqueResult();
	}

	private ParkingUserSession getSessionBean(String username) {
		Query query = getSessionFactory().createQuery("from c in class " + ParkingUserSession.class.getName() + " where c.username=:username");
		query.setString("username", username);
		ParkingUserSession session = (ParkingUserSession) query.setMaxResults(1).uniqueResult();
		if (session == null) {
			session = new ParkingUserSession();
			session.setUsername(username);
			session.setLastAccessTime(new Date());
			getSessionFactory().save(session);
		}
		return session;

	}

}
