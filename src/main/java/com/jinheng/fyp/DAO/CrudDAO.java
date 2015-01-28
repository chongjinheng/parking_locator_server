package com.jinheng.fyp.DAO;

import java.io.Serializable;

public interface CrudDAO<T> {

	public Long create(Object object);

	public void update(Object object);

	public void delete(Object object);

	public T getObjectById(final Class<T> clazz, final Serializable id);
}
