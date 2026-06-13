package com.reyco.dasbx.es.test.template;

import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;
import com.reyco.dasbx.es.support.annotation.sort.EsSort;
import com.reyco.dasbx.es.support.annotation.sort.EsSortParam;
import com.reyco.dasbx.es.support.result.facet.GenderLabelProvider;

@EsIndex("personage")
public class PersonageDto extends BaseDto {
	
	@EsQuery(type=EsQueryType.MATCH,field="all")
	private String keyword;
	
	@EsHighlightField
	private String name;
	@EsHighlightField
	private String code;
	
	@EsHighlightField
	@EsAgg
	private String masterpiece;
	
	@EsAgg(provider=GenderLabelProvider.class)
	private Byte gender;
	
	@EsAgg
	private Long provinceId;
	
	@EsSort(field="location")
	private EsSortParam location;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getMasterpiece() {
		return masterpiece;
	}

	public void setMasterpiece(String masterpiece) {
		this.masterpiece = masterpiece;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public EsSortParam getLocation() {
		return location;
	}

	public void setLocation(EsSortParam location) {
		this.location = location;
	}
}
