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

	private Long flatRate;

	private Long firstHour;

	private Long subsHour;

	private Long lostTicPenalty;

	private String priceType;

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public Long getFlatRate() {
		return flatRate;
	}

	public void setFlatRate(Long flatRate) {
		this.flatRate = flatRate;
	}

	public Long getFirstHour() {
		return firstHour;
	}

	public void setFirstHour(Long firstHour) {
		this.firstHour = firstHour;
	}

	public Long getSubsHour() {
		return subsHour;
	}

	public void setSubsHour(Long subsHour) {
		this.subsHour = subsHour;
	}

	public Long getLostTicPenalty() {
		return lostTicPenalty;
	}

	public void setLostTicPenalty(Long lostTicPenalty) {
		this.lostTicPenalty = lostTicPenalty;
	}

	public Integer getPriceID() {
		return priceID;
	}

}
