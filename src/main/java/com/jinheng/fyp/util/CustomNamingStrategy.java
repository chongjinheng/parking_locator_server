package com.jinheng.fyp.util;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Override Hibernate settings to generate table and column name according to what we want
 * 
 * @see ImprovedNamingStrategy
 * @author chengyang
 */
public class CustomNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = 1L;

	@Override
	public String classToTableName(String className) {
		String tableNameInSingularForm = super.classToTableName(className);
		return tableNameInSingularForm.toUpperCase();
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		String colName = super.propertyToColumnName(propertyName);
		return colName.toUpperCase();
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		String fkColName = super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName);
		return (fkColName += "_FK").toUpperCase();

	}
}
