package com.jinheng.fyp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinheng.fyp.DAO.MapDAO;
import com.jinheng.fyp.DTO.JSONServiceDTO;
import com.jinheng.fyp.bean.Lot;
import com.jinheng.fyp.enums.ErrorStatus;
import com.jinheng.fyp.exceptions.MyMobileRequestException;
import com.jinheng.fyp.util.Validators;

@Service
public class MapServiceImpl implements MapService {

	private static final Logger logger = LoggerFactory.getLogger(MapServiceImpl.class);

	@Autowired
	private MapDAO mapDao;

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

}
