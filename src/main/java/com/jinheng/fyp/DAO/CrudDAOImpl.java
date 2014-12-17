package com.jinheng.fyp.DAO;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CrudDAO Class
 * 
 * @author original author
 * @author waikit chong
 */
@Service
@Transactional(readOnly = false)
public class CrudDAOImpl<T> extends GenericDAO implements CrudDAO<T> {

	@Override
	@Transactional(readOnly = false)
	public Long create(Object object) {
		Integer i = (Integer) getSessionFactory().save(object);
		return i.longValue();
	}

	@Override
	@Transactional(readOnly = false)
	public void update(Object object) {
		getSessionFactory().update(object);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Object object) {
		getSessionFactory().delete(object);
	}

	@Override
	@Transactional(readOnly = true)
	public T getObjectById(Class<T> clazz, Serializable id) {
		return clazz.cast(getSessionFactory().get(clazz, id));
	}
}
