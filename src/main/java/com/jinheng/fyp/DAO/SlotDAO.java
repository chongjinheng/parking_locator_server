package com.jinheng.fyp.DAO;

import java.util.Date;

import com.jinheng.fyp.bean.Lot;
import com.jinheng.fyp.bean.ParkingUser;
import com.jinheng.fyp.bean.Slot;

public interface SlotDAO {

	public Long createSlot(String userEmail, String status, Lot lot, ParkingUser parkingUser, Date parkTIme);

	public Slot getSlotByUserEmail(String userEmail);

	public void removeSlotByByUserEmail(String userEmail);
}
