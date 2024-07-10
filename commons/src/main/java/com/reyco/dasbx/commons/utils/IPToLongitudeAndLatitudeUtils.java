package com.reyco.dasbx.commons.utils;

import com.reyco.dasbx.commons.utils.HttpClient.HttpResult;

/**
 * 根据IP获取经纬度
 * @author reyco
 *
 */
public class IPToLongitudeAndLatitudeUtils {
	
	private final static String API = "http://ip-api.com/json/";
	
	public static LongitudeLatitude getLongitudeAndLatitude(String ip) {
		HttpResult result = HttpClient.httpGet(API+ip, null);
		if(result.getCode()==200) {
			String json = result.getContent();
			LongitudeLatitude longitudeLatitude = JsonUtils.jsonToObj(json, LongitudeLatitude.class);
			return longitudeLatitude;
		}
		return new LongitudeLatitude();
	}
	public static class LongitudeLatitude{
		private Double lon = 120.114491;
		private Double lat = 30.316726;
		public Double getLon() {
			return lon;
		}
		public void setLon(Double lon) {
			this.lon = lon;
		}
		public Double getLat() {
			return lat;
		}
		public void setLat(Double lat) {
			this.lat = lat;
		}
	}
	public static void main(String[] args) {
		IPToLongitudeAndLatitudeUtils.getLongitudeAndLatitude("112.10.226.11");
	}
}
