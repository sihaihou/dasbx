package com.reyco.dasbx.portal.provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.YearService;

public class VideoYearProvider implements FacetLabelProvider{
	
	private YearService yearService;
	
	@Autowired
	public VideoYearProvider(YearService yearService) {
		this.yearService = yearService;
	}
	
	@Override
	public String getLabel(Object key) {
		Long yearId = Long.parseLong(key.toString());
		YearListVO yearListVO = yearService.get(yearId);
		return yearListVO.getName();
	}

}
