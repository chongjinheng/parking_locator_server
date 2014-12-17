package com.jinheng.fyp.DAO;

import java.io.Serializable;

/**
 * CrudDAO Class
 * 
 * @author original author
 * @author waikit chong
 */
public interface CrudDAO<T> {

	public Long create(Object object);

	public void update(Object object);

	public void delete(Object object);

	public T getObjectById(final Class<T> clazz, final Serializable id);
}
