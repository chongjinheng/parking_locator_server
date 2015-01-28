package com.jinheng.fyp.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public abstract class GenericDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSessionFactory() {
		return sessionFactory.getCurrentSession();
	}

}
