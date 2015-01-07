package com.jinheng.fyp.service;

import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.exceptions.MyMobileRequestException;

public interface MapService {

	public JSONServiceDTO getParkingLots(Double latitute, Double longitute, String groupType, String searchCriteria) throws MyMobileRequestException, Exception;

}
