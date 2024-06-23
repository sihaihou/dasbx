package com.reyco.dasbx.commons.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Dasbx {
	
	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	
	public static final Integer DEFAULT_TIMEZONE_8 = 8;
	/**
	 * 不同时区获取时间
	 * @param date  当前时间
	 * @param format 格式
	 * @param timezone 时区
	 * @return
	 */
	public static String getDateByTimeZone(Date date) {
		return getDateByTimeZone(date, FORMAT, DEFAULT_TIMEZONE_8);
	}
	/**
	 * 不同时区获取时间
	 * @param date  当前时间
	 * @param format 格式
	 * @param timezone 时区
	 * @return
	 */
	public static String getDateByTimeZone(Date date,String format) {
		return getDateByTimeZone(date, format, DEFAULT_TIMEZONE_8);
	}
	/**
	 * 不同时区获取时间
	 * @param date  当前时间
	 * @param format 格式
	 * @param timezone 时区
	 * @return
	 */
	public static String getDateByTimeZone(Date date,int timezone) {
		return getDateByTimeZone(date, FORMAT, timezone);
	}
	/**
	 * 不同时区获取时间
	 * @param date  当前时间
	 * @param format 格式
	 * @param timezone 时区
	 * @return
	 */
	public static String getDateByTimeZone(Date date, String format, int timezone) {
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format, timezone);
		return simpleDateFormat.format(date);
	}

	/**
	 * 不同时区获取时间
	 * @param time 指定时间戳
	 * @param timezone  时区
	 * @return
	 */
	public static String getDateByTimeZone(Long time) {
		return getDateByTimeZone(time, FORMAT, DEFAULT_TIMEZONE_8);
	}
	/**
	 * 不同时区获取时间
	 * @param time 指定时间戳
	 * @param timezone  时区
	 * @return
	 */
	public static String getDateByTimeZone(Long time,String format) {
		return getDateByTimeZone(time, format, DEFAULT_TIMEZONE_8);
	}
	/**
	 * 不同时区获取时间
	 * @param time 指定时间戳
	 * @param timezone  时区
	 * @return
	 */
	public static String getDateByTimeZone(Long time,int timezone) {
		return getDateByTimeZone(time, FORMAT, timezone);
	}
	/**
	 * 不同时区获取时间
	 * @param time 指定时间戳
	 * @param format 格式
	 * @param timezone  时区
	 * @return
	 */
	public static String getDateByTimeZone(Long time, String format, int timezone) {
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format, timezone);
		return simpleDateFormat.format(new Date(time));
	}
	/**
	 * 获取时间戳
	 * @return
	 */
	public static Long getCurrentTime() {
		return new Date().getTime();
	}
	/**
	 * 获取时间戳
	 * @param date
	 * @return
	 */
	public static Long getTimeByDate(Date date) {
		return date.getTime();
	}
	/**
	 * 获取时间戳
	 * @param date
	 * @return
	 */
	public static Long getTimeByDateFormat(String dateStr,String format) {
		if (dateStr == null || dateStr.length() <= 0)
			return null;
		try {
			String dateTime = ((dateStr.indexOf('.') > 0)) ? dateStr.substring(0, dateStr.indexOf('.')) : dateStr;
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date date = dateFormat.parse(dateTime);
			return date.getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据时区获取指定的Format
	 * @param format
	 * @param timezone
	 * @return
	 */
	private static SimpleDateFormat getSimpleDateFormat(String format, int timezone) {
		TimeZone timeZone = getTimeZone(timezone);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(timeZone);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat;
	}

	/**
	 * 获取指定的时区
	 * 
	 * @param timezone
	 * @return
	 */
	private static TimeZone getTimeZone(int timezone) {
		if (timezone > 13 || timezone < -12) {
			timezone = 0;
		}
		TimeZone timeZone = TimeZone.getTimeZone("GMT+" + timezone);
		return timeZone;
	}

	public static void main(String[] args) {
		String dateByTimeZone = getDateByTimeZone(1688453540000L, "yyyy-MM-dd HH:mm:ss", 0);
		System.out.println(dateByTimeZone);
	}
}
