package com.jinheng.fyp.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * Formats Date into a custom format that the front end needs
 * 
 * @author chengyang
 */
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

	public static String resizeBase64Image(String base64ImgString) throws Exception {
		byte[] byteFormatedImage = ImageUtils.base64Decode(base64ImgString);
		ByteArrayInputStream tempByteImage = new ByteArrayInputStream(byteFormatedImage);
		String processedBase64Img = null;

		try {
			BufferedImage image = ImageIO.read(tempByteImage);
			BufferedImage processedImg = ImageUtils.resize(100, 100, 1, image);
			processedBase64Img = ImageUtils.BufferedImageToBase64(processedImg);
			return processedBase64Img;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processedBase64Img;
	}
}
