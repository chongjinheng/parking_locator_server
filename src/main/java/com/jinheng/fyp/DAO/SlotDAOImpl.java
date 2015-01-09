package com.jinheng.fyp.DAO;

import java.util.Date;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.bean.Lot;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.bean.Slot;

@Repository
@Transactional
public class SlotDAOImpl extends GenericDAO implements SlotDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CrudDAO crudDao;

	@Override
	public Long createSlot(String userEmail, String status, Lot lot, ParkingUser parkingUser, Date parkTime) {
		Slot slot = new Slot();
		slot.setCreatedBy(userEmail);
		slot.setCreatedDate(new Date());
		slot.setStatus(status);
		slot.setLot(lot);
		slot.setUser(parkingUser);
		slot.setParkTime(parkTime);
		return crudDao.create(slot);
	}

	@Override
	public Slot getSlotByUserEmail(String userEmail) {
		return (Slot) getSessionFactory().createCriteria(Slot.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("createdBy", userEmail)).uniqueResult();
	}

	@Override
	public void removeSlotByByUserEmail(String userEmail) {
		Slot slot = getSlotByUserEmail(userEmail);
		crudDao.delete(slot);
	}

}
