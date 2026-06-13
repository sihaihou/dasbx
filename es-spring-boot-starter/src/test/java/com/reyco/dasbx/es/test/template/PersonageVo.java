package com.reyco.dasbx.es.test.template;

import com.reyco.dasbx.es.support.result.record.EsGeoDistanceResult;
import com.reyco.dasbx.es.support.result.record.EsHighlightResult;

public class PersonageVo {
	private Long id;
	
	@EsHighlightResult
	private String name;
	
	@EsHighlightResult
	private String code;
	
	private Byte gender;
	
	@EsHighlightResult
	private String masterpiece;
	
	@EsGeoDistanceResult
	private Double distance;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getMasterpiece() {
		return masterpiece;
	}

	public void setMasterpiece(String masterpiece) {
		this.masterpiece = masterpiece;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "PersonageVo [id=" + id + ", name=" + name + ", code=" + code + ", gender=" + gender + ", masterpiece="
				+ masterpiece + ", distance=" + distance + "]";
	}
}
