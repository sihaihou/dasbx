package com.reyco.dasbx.model.es;

import java.io.Serializable;
import java.math.BigDecimal;

public class GeoPoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6192473763814635272L;
	private BigDecimal lat;
	private BigDecimal lon;
	public GeoPoint() {
	}
	public GeoPoint(BigDecimal lat, BigDecimal lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	public BigDecimal getLon() {
		return lon;
	}
	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}
}
