package com.jinheng.fyp.DAO;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.Lot;

@Repository
@Transactional
public class MapDAOImpl extends GenericDAO implements MapDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Lot> getNearbyLot(Double latitude, Double longitude, String groupType, String criteria) {
		if (groupType.equals("city")) {
			return (List<Lot>) getSessionFactory().createCriteria(Lot.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("city", criteria)).list();
		} else if (groupType.equals("state")) {
			return (List<Lot>) getSessionFactory().createCriteria(Lot.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("state", criteria)).list();
		} else {
			return null;
		}
	}
}
