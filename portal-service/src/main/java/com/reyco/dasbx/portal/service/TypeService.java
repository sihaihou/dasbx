package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;

public interface TypeService {
	
	TypeListVO get(Long id);
	
	List<TypeListVO> list();
	
	List<TypeListVO> listByCategoryId(Long categoryId);
}
