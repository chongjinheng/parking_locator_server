package com.jinheng.fyp.DAO;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.Lot;

@Repository
@Transactional
public class LotDAOImpl extends GenericDAO implements LotDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CrudDAO crudDao;

	@Override
	public Lot getLotByID(Integer id) {
		return (Lot) getSessionFactory().createCriteria(Lot.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("lotID", id)).uniqueResult();
	}

}
