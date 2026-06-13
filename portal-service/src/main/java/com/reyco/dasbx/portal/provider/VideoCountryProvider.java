package com.reyco.dasbx.portal.provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.service.CountryService;

public class VideoCountryProvider implements FacetLabelProvider{
	
	private CountryService countryService;
	
	@Autowired
	public VideoCountryProvider(CountryService countryService) {
		this.countryService = countryService;
	}
	
	@Override
	public String getLabel(Object key) {
		Long countryId = Long.parseLong(key.toString());
		CountryListVO cuntryListVO = countryService.get(countryId);
		return cuntryListVO.getName();
	}

}
