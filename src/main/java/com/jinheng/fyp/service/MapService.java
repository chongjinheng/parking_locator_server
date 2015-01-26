package com.jinheng.fyp.service;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.Slot;
import com.jinheng.fyp.exceptions.MyMobileRequestException;

public interface MapService {

	public JSONServiceDTO getParkingLots(Double latitute, Double longitute, String groupType, String searchCriteria) throws MyMobileRequestException, Exception;

	public JSONServiceDTO parkVehicle(Slot slot, String userEmail, Boolean forceRepark) throws MyMobileRequestException, Exception;

	public JSONServiceDTO checkVehicle(String email) throws MyMobileRequestException, Exception;

	public JSONServiceDTO removeVehicle(String email) throws MyMobileRequestException, Exception;

	// triggered by sensor
	public void updateAvailabilityOnSensorChanged(Long slotID, Boolean parked) throws MyMobileRequestException, Exception;

	// trigger when user tag QR
	public JSONServiceDTO updateOnQRScanned(Slot slot, String email) throws MyMobileRequestException, Exception;
}
