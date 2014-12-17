package com.jinheng.fyp.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Slot extends BasicTable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer slotID;

	private Integer floorLevel;

	private String status;

	@OneToOne
	private ParkingUser user;

	@ManyToOne
	private Lot lot;

	public Integer getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(Integer floorLevel) {
		this.floorLevel = floorLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Lot getLot() {
		return lot;
	}

	public void setLot(Lot lot) {
		this.lot = lot;
	}

	public Integer getSlotID() {
		return slotID;
	}

	public ParkingUser getUser() {
		return user;
	}

	public void setUser(ParkingUser user) {
		this.user = user;
	}

	public void setSlotID(Integer slotID) {
		this.slotID = slotID;
	}

}
