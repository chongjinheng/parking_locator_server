package com.jinheng.fyp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.DAO.LotDAO;
import com.jinheng.fyp.DAO.MapDAO;
import com.jinheng.fyp.DAO.ParkingUserDAO;
import com.jinheng.fyp.DAO.SlotDAO;
import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.Lot;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.bean.Slot;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.util.Validators;

@Service
public class MapServiceImpl implements MapService {

	private static final Logger logger = LoggerFactory.getLogger(MapServiceImpl.class);

	@Autowired
	private ParkingUserDAO parkingUserDao;

	@Autowired
	private MapDAO mapDao;

	@Autowired
	private SlotDAO slotDao;

	@Autowired
	private LotDAO lotDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONServiceDTO getParkingLots(Double latitude, Double longitude, String groupType, String searchCriteria) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();

		try {
			Validators.sanityCheck(groupType);
			Validators.sanityCheck(searchCriteria);
			if (!groupType.equals("city") && !groupType.equals("state")) {
				throw new MyMobileRequestException(ErrorStatus.ACCESS_DENIED, ErrorStatus.ACCESS_DENIED.getDefaultMessage());
			}

			List<Lot> lots = new ArrayList<Lot>();
			if (latitude != null || longitude != null) {
				lots = mapDao.getNearbyLot(latitude, longitude, groupType, searchCriteria);
				logger.debug("List of parking lot retrieved");
				dtoToReturn.setParkingLots(lots);
			}
			return dtoToReturn;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public JSONServiceDTO parkVehicle(Slot slot, String userEmail, Boolean forceRepark) throws MyMobileRequestException, Exception {

		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {

			// check whether user is parked
			Slot checker = slotDao.getSlotByUserEmail(userEmail);
			Lot lot = lotDao.getLotByID(slot.getLot().getLotID());
			ParkingUser parkingUser = parkingUserDao.getUserByEmail(userEmail);

			if (checker != null) {
				if (checker.getLot().getLotID() == lot.getLotID()) {
					dtoToReturn.setAlreadyParkedThere(true);
				} else {
					if (forceRepark) {
						slotDao.removeSlotByByUserEmail(userEmail);
						slotDao.createSlot(userEmail, slot.getStatus(), lot, parkingUser, slot.getParkTime());
					} else {
						throw new MyMobileRequestException(ErrorStatus.USER_PARKED, ErrorStatus.USER_PARKED.getDefaultMessage());
					}
				}
			} else {
				slotDao.createSlot(userEmail, slot.getStatus(), lot, parkingUser, slot.getParkTime());
				dtoToReturn.setAlreadyParkedThere(false);
			}
			return dtoToReturn;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public JSONServiceDTO checkUserParked(String email) {
		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			Slot slot = slotDao.getSlotByUserEmail(email);
			if (slot != null) {
				dtoToReturn.setSlot(slot);
				dtoToReturn.setAlreadyParkedThere(true);
			} else {
				dtoToReturn.setAlreadyParkedThere(false);
			}
		} catch (Exception e) {
			throw e;
		}
		return dtoToReturn;
	}

	@Override
	public JSONServiceDTO removeVehicle(String email) {
		JSONServiceDTO dtoToReturn = new JSONServiceDTO();
		try {
			slotDao.removeSlotByByUserEmail(email);
			dtoToReturn.setForceRepark(true);

		} catch (Exception e) {
			throw e;
		}
		return dtoToReturn;
	}
}
