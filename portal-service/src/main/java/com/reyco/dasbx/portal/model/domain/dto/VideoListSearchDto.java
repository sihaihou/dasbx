package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.es.dto.SearchPageDto;
import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;
import com.reyco.dasbx.es.support.annotation.sort.EsSort;
import com.reyco.dasbx.es.support.annotation.sort.EsSortParam;
import com.reyco.dasbx.portal.provider.VideoCategoryProvider;
import com.reyco.dasbx.portal.provider.VideoCountryProvider;
import com.reyco.dasbx.portal.provider.VideoTypeProvider;
import com.reyco.dasbx.portal.provider.VideoYearProvider;

@EsIndex("video")
public class VideoListSearchDto extends SearchPageDto{
	
	@EsQuery(type=EsQueryType.MATCH,field="all")
	private String keyword;
	
	@EsQuery
	@EsAgg(name="category",provider=VideoCategoryProvider.class)
	private Long categoryId;
	@EsQuery
	@EsAgg(name="country",provider=VideoCountryProvider.class)
	private Long countryId;
	@EsQuery
	@EsAgg(name="type",provider=VideoTypeProvider.class)
	private Long typeId;
	@EsQuery
	@EsAgg(name="year",provider=VideoYearProvider.class)
	private Long yearId;
	
	@EsQuery
	private Long vipId;
	
	@EsHighlightField(preTags="",postTags="")
	private String name;
	@EsHighlightField(preTags="",postTags="")
	private String description;
	
	@EsSort
	private EsSortParam playQuantity;
	@EsSort
	private EsSortParam heatQuantity;
	
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
	public EsSortParam getPlayQuantity() {
		return playQuantity;
	}
	public void setPlayQuantity(EsSortParam playQuantity) {
		this.playQuantity = playQuantity;
	}
	public EsSortParam getHeatQuantity() {
		return heatQuantity;
	}
	public void setHeatQuantity(EsSortParam heatQuantity) {
		this.heatQuantity = heatQuantity;
	}
	public Byte getSort() {
		return sort;
	}
	public void setSort(Byte sort) {
		this.sort = sort;
	}
}
