package com.jinheng.fyp.service;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.Slot;
import com.jinheng.fyp.exceptions.MyMobileRequestException;

public interface MapService {

	public JSONServiceDTO getParkingLots(Double latitute, Double longitute, String groupType, String searchCriteria) throws MyMobileRequestException, Exception;

	public JSONServiceDTO parkVehicle(Slot slot, String userEmail, Boolean forceRepark) throws MyMobileRequestException, Exception;

	public JSONServiceDTO checkVehicle(String email);

	public JSONServiceDTO removeVehicle(String email);

	// triggered by sensor
	public void updateAvailabilityOnSensorChanged(Long slotID, Boolean parked);

	// trigger when user tag nfc or QR
	public JSONServiceDTO saveParkedSlotDetails(String email, Slot slot);
}
