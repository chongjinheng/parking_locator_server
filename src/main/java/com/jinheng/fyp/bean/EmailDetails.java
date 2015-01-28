package com.jinheng.fyp.bean;

import java.text.DecimalFormat;

public class EmailDetails {

	public String email;
	public String temporaryPassword;
	public String feedback;
	public String transactionDate;
	public String time;

	DecimalFormat df = new DecimalFormat("###,###,##0.00");

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String storeName) {
		this.feedback = storeName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.setTime(transactionDate.substring(11));
		this.transactionDate = transactionDate.substring(0, 10);
	}

}
