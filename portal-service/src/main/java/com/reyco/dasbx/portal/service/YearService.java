package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.vo.YearListVO;

public interface YearService {
	
	YearListVO get(Long id);
	
	List<YearListVO> list();
	
	List<YearListVO> listByCategoryId(Long categoryId);
}
