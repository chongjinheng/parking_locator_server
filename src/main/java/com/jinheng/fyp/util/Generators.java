package com.jinheng.fyp.util;

import java.util.Calendar;

public class Generators {

	public static String generateRecieptNumber(Long storeID, Integer currentRunningNumber) {
		Calendar tempCal = Calendar.getInstance();
		int tempDate = tempCal.get(Calendar.DAY_OF_MONTH);
		int tempMonth = tempCal.get(Calendar.MONTH) + 1;
		int tempYear = tempCal.get(Calendar.YEAR) % 100;

		long yymmdd = tempYear * (long) Math.pow(10, 16) + tempMonth * (long) Math.pow(10, 14) + tempDate * (long) Math.pow(10, 12);
		long receiptNo = yymmdd + storeID * (long) Math.pow(10, 6) + (long) currentRunningNumber;

		return String.valueOf(receiptNo);
	}
}
