package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.es.core.search.SimpleSortSearchDto;

public class VideoSearchDto extends SimpleSortSearchDto{
	private Long categoryId;
	private Long countryId;
	private Long typeId;
	private Long yearId;
	private Long vipId;
	private Byte sort;
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public Long getVipId() {
		return vipId;
	}
	public void setVipId(Long vipId) {
		this.vipId = vipId;
	}
	public Byte getSort() {
		return sort;
	}
	public void setSort(Byte sort) {
		this.sort = sort;
	}
}
