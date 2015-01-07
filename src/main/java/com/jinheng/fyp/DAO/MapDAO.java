package com.jinheng.fyp.DAO;

import java.util.List;

import com.jinheng.fyp.bean.Lot;

public interface MapDAO {

	public List<Lot> getNearbyLot(Double latitute, Double longitute, String groupType, String criteria);

}
