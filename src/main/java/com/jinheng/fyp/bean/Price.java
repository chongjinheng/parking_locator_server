package com.jinheng.fyp.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Price extends BasicTable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer priceID;

	private Double flatRate;

	private Double firstHour;

	private Double subsHour;

	private Double lostTicPenalty;

	public Double getFlatRate() {
		return flatRate;
	}

	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}

	public Double getFirstHour() {
		return firstHour;
	}

	public void setFirstHour(Double firstHour) {
		this.firstHour = firstHour;
	}

	public Double getSubsHour() {
		return subsHour;
	}

	public void setSubsHour(Double subsHour) {
		this.subsHour = subsHour;
	}

	public Double getLostTicPenalty() {
		return lostTicPenalty;
	}

	public void setLostTicPenalty(Double lostTicPenalty) {
		this.lostTicPenalty = lostTicPenalty;
	}

	public Integer getPriceID() {
		return priceID;
	}

}
