package com.reyco.dasbx.commons.utils.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.expression.ParseException;

public class DateUtils {

	public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 字符串转时间
	 * @param dateTime
	 * @return
	 */
	public static Date parseDateTime(String dateTime) {
		if (dateTime.trim().length() == 10) {
			return parse(dateTime, DATE_FORMAT);
		}
		return parse(dateTime.trim(), DATE_TIME_FORMAT);
	}
	/**
	 * 字符串转时间
	 * @param dateTime
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String dateTime, String format) throws ParseException {
		if (dateTime == null || dateTime.length() <= 0)
			return null;
		try {
			String sDateTime = ((dateTime.indexOf('.') > 0)) ? dateTime.substring(0, dateTime.indexOf('.')) : dateTime;
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(sDateTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将日期类解析成"yyyy-MM-dd HH:mm:ss"格式的日期字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return format(date, DATE_TIME_FORMAT);
	}

	/**
	 * 将日期类解析成指定格式的日期字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format) {
		if (date == null)
			return null;
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
}
