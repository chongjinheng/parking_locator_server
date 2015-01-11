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

	@Autowired
	private SlotDAO slotDao;

	@Override
	public Lot getLotByID(Long id) {
		return (Lot) getSessionFactory().createCriteria(Lot.class).add(Restrictions.eq("deleteFlag", false)).add(Restrictions.eq("lotID", id)).uniqueResult();
	}

	@Override
	public void updateAvailability(Lot currentLot) {
		if (currentLot.getCapacity() != null) {
			Long maxCapacity = Long.valueOf(currentLot.getCapacity());
			Double low = maxCapacity * 0.2;
			Double medium = maxCapacity * 0.7;
			// cast to int to avoid decimal points
			int lowAvailbility = low.intValue();
			int medAvailbility = medium.intValue();
			Long remainingSlot;
			Long occupiedSpace;

			// user is parked
			switch (currentLot.getLotType()) {
			case ("O"):
				occupiedSpace = slotDao.getOccupiedSlotCount(currentLot.getLotID());
				remainingSlot = maxCapacity - occupiedSpace;
				if (remainingSlot < lowAvailbility) {
					currentLot.setAvailability("L");
				} else if (remainingSlot < medAvailbility) {
					currentLot.setAvailability("M");
				} else {
					currentLot.setAvailability("H");
				}
				crudDao.update(currentLot);
				break;

			case ("P"):
			case ("B"):
				occupiedSpace = slotDao.getOccupiedSlotByStatus(currentLot.getLotID(), "F");
				remainingSlot = maxCapacity - occupiedSpace;
				if (remainingSlot < lowAvailbility) {
					currentLot.setAvailability("L");
				} else if (remainingSlot < medAvailbility) {
					currentLot.setAvailability("M");
				} else {
					currentLot.setAvailability("H");
				}
				crudDao.update(currentLot);
				break;
			}

		}
	}
}
