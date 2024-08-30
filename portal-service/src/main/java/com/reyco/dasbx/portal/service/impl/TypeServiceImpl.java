package com.reyco.dasbx.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.portal.dao.TypeDao;
import com.reyco.dasbx.portal.model.domain.Type;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.service.TypeService;

@Service
public class TypeServiceImpl implements TypeService {
	@Autowired
	private TypeDao typeDao;
	@Override
	public List<TypeListVO> list() {
		List<Type> types = typeDao.list();
		List<TypeListVO> typeListVO = Convert.sourceListToTargetList(types, TypeListVO.class);
		return typeListVO;
	}

}
