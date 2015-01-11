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

			// user is parked somewhere else
			if (checker != null) {
				if (checker.getLot().getLotID() == lot.getLotID()) {
					dtoToReturn.setAlreadyParkedThere(true);
				} else {
					if (forceRepark) {
						// repark in current parking lot
						// check lot type first, paid and basement parking do not delete slot
						if (lot.getLotType().equals("P") || lot.getLotType().equals("B")) {
							checker.setCreatedBy(null);
							slotDao.updateSlot(checker);
						} else {
							slotDao.removeSlotByByUserEmail(userEmail);
							slotDao.createSlot(userEmail, slot.getStatus(), lot, parkingUser, slot.getParkTime());
						}
						lotDao.updateAvailability(lot);
					} else {
						throw new MyMobileRequestException(ErrorStatus.USER_PARKED, ErrorStatus.USER_PARKED.getDefaultMessage());
					}
				}
			} else {
				// parked successfully
				slotDao.createSlot(userEmail, slot.getStatus(), lot, parkingUser, slot.getParkTime());
				lotDao.updateAvailability(lot);
				dtoToReturn.setAlreadyParkedThere(false);
			}
			return dtoToReturn;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public JSONServiceDTO checkVehicle(String email) {
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
			Slot slot = slotDao.getSlotByUserEmail(email);
			Lot lot = slot.getLot();

			slotDao.removeSlotByByUserEmail(email);
			lotDao.updateAvailability(lot);
			dtoToReturn.setForceRepark(true);

		} catch (Exception e) {
			throw e;
		}
		return dtoToReturn;
	}

	@Override
	public void updateAvailabilityOnSensorChanged(Long slotID, Boolean parked) {
		try {
			if (parked) {
				Slot slot = slotDao.getSlotByID(slotID);
				slot.setStatus("F");
				slotDao.updateSlot(slot);
			} else {
				Slot slot = slotDao.getSlotByID(slotID);
				slot.setStatus("E");
				slotDao.updateSlot(slot);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public JSONServiceDTO saveParkedSlotDetails(String email, Slot slot) {
		JSONServiceDTO dtoToReturn = new JSONServiceDTO();

		// remove previously saved slot first and proceed to updating current slot status
		// TODO null pointer chance
		slotDao.removeSlotByByUserEmail(email);
		// just update straight, slot from NFC/QR contain slotID
		slot.setCreatedBy(email);
		slotDao.updateSlot(slot);
		dtoToReturn.setSlot(slot);

		return dtoToReturn;
	}

}
