package com.jinheng.fyp.bean;

import java.text.DecimalFormat;
import java.util.List;

public class EmailDetails {

	public String email;
	public String temporaryPassword;
	public String storeName;
	public String receiptNumber;
	public String paymentType;
	public String subTotalAmount;
	public String totalAmount;
	public String discountAmount;
	public String tenderAmount;
	public String changeAmount;
	public String discountName;
	public List<String> cartItemSubTotalList;
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

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		if (paymentType.equals("0")) {
			this.paymentType = "Cash";
		} else {
			this.paymentType = "Credit Card";
		}

		this.paymentType = paymentType;
	}

	public String getSubTotalAmount() {
		return subTotalAmount;
	}

	public void setSubTotalAmount(String subTotalAmount) {
		Double tempDouble = Double.valueOf(subTotalAmount);
		tempDouble /= 100;
		this.subTotalAmount = df.format(tempDouble);
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		Double tempDouble = Double.valueOf(totalAmount);
		tempDouble /= 100;
		this.totalAmount = df.format(tempDouble);
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		if (!discountAmount.equals("null")) {
			Double tempDouble = Double.valueOf(discountAmount);
			tempDouble /= 100;
			this.discountAmount = df.format(tempDouble);
		} else {
			this.discountAmount = df.format(0);
		}
	}

	public String getTenderAmount() {
		return tenderAmount;
	}

	public void setTenderAmount(String tenderAmount) {
		if (!tenderAmount.equals("null")) {
			Double tempDouble = Double.valueOf(tenderAmount);
			tempDouble /= 100;
			this.tenderAmount = df.format(tempDouble);
		} else {
			this.tenderAmount = df.format(0);
		}
	}

	public String getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(String changeAmount) {
		if (!changeAmount.equals("null")) {
			Double tempDouble = Double.valueOf(changeAmount);
			tempDouble /= 100;
			this.changeAmount = df.format(tempDouble);
		} else {
			this.changeAmount = df.format(0);
		}
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<String> getCartItemSubTotalList() {
		return cartItemSubTotalList;
	}

	public void setCartItemSubTotalList(List<String> cartItemSubTotalList) {
		this.cartItemSubTotalList = cartItemSubTotalList;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.setTime(transactionDate.substring(11));
		this.transactionDate = transactionDate.substring(0, 10);
	}

}
