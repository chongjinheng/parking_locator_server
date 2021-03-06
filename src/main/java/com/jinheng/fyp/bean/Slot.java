package com.jinheng.fyp.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Slot extends BasicTable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long slotID;

	private String floorLevel;

	private String status;

	private String position;

	private Date parkTime;

	@OneToOne
	private ParkingUser user;

	@ManyToOne
	private Lot lot;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getParkTime() {
		return parkTime;
	}

	public void setParkTime(Date parkTime) {
		this.parkTime = parkTime;
	}

	public String getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(String floorLevel) {
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

	public Long getSlotID() {
		return slotID;
	}

	public ParkingUser getUser() {
		return user;
	}

	public void setUser(ParkingUser user) {
		this.user = user;
	}

	public void setSlotID(Long slotID) {
		this.slotID = slotID;
	}

}
