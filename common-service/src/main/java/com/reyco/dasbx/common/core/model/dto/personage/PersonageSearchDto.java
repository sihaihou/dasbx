package com.reyco.dasbx.common.core.model.dto.personage;

import com.reyco.dasbx.es.dto.SearchPageDto;
import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;
import com.reyco.dasbx.es.support.annotation.sort.EsSort;
import com.reyco.dasbx.es.support.annotation.sort.EsSortParam;
import com.reyco.dasbx.es.support.result.facet.GenderLabelProvider;

@EsIndex("personage")
public class PersonageSearchDto extends SearchPageDto {
	
	@EsQuery(type=EsQueryType.MATCH,field="all")
	private String keyword;
	
	@EsQuery
	@EsAgg(name="性别",provider=GenderLabelProvider.class)
	private Byte gender;
	
	@EsQuery
	@EsHighlightField
	@EsAgg(name="代表作")
	private String masterpiece;
	
	@EsHighlightField
	private String name;
	@EsHighlightField
	private String code;
	
	@EsSort(field="location")
	private EsSortParam sortParam = new EsSortParam();
	
	private Double longitude; 
	private Double latitude; 
	
	
	private Integer startAge;
	private Integer endAge;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public EsSortParam getSortParam() {
		return sortParam;
	}
	public void setSortParam(EsSortParam sortParam) {
		this.sortParam = sortParam;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
		this.sortParam.setLongitude(longitude);
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
		this.sortParam.setLatitude(latitude);
	}
	public Integer getStartAge() {
		return startAge;
	}
	public void setStartAge(Integer startAge) {
		this.startAge = startAge;
	}
	public Integer getEndAge() {
		return endAge;
	}
	public void setEndAge(Integer endAge) {
		this.endAge = endAge;
	}
}
