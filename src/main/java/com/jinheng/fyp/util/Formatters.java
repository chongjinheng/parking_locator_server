package com.jinheng.fyp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatters {

	/**
	 * Format a date into dd/MM/yyyy HH:mm:ss
	 * 
	 * @param recordedDate
	 * @return String
	 */
	public static String formatOutputDate(Date recordedDate) {

		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return formatter.format(recordedDate);
	}
}
