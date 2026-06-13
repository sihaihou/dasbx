package com.reyco.dasbx.portal.provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.service.TypeService;

public class VideoTypeProvider implements FacetLabelProvider {

	private TypeService typeService;

	@Autowired
	public VideoTypeProvider(TypeService typeService) {
		this.typeService = typeService;
	}

	@Override
	public String getLabel(Object key) {
		Long categoryId = Long.parseLong(key.toString());
		TypeListVO typeListVO = typeService.get(categoryId);
		return typeListVO.getName();
	}

}
