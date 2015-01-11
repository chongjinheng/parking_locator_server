package com.jinheng.fyp.DAO;

import com.jinheng.fyp.bean.Lot;

public interface LotDAO {

	public Lot getLotByID(Long id);

	public void updateAvailability(Lot currentLot);

}
