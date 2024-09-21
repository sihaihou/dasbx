package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;

public interface CountryService {
	
	CountryListVO get(Long id);
	
	List<CountryListVO> list();
	
	List<CountryListVO> listByCategoryId(Long categoryId);
}
