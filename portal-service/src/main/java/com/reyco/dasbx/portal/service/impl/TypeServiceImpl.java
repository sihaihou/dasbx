package com.reyco.dasbx.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.portal.dao.TypeDao;
import com.reyco.dasbx.portal.model.domain.Type;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.service.TypeService;

@Service
public class TypeServiceImpl implements TypeService {
	@Autowired
	private TypeDao typeDao;
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.PORTAL_TYPE_INFO_PREFIX,key="#id")
	public TypeListVO get(Long id) {
		Type type = typeDao.getById(id);
		TypeListVO typeListVO = Convert.sourceToTarget(type, TypeListVO.class);
		return typeListVO;
	}
	@Override
	public List<TypeListVO> list() {
		List<Type> types = typeDao.list();
		List<TypeListVO> typeListVO = Convert.sourceListToTargetList(types, TypeListVO.class);
		return typeListVO;
	}
	@Override
	public List<TypeListVO> listByCategoryId(Long categoryId) {
		if(categoryId==null || categoryId.intValue()<1) {
			return new ArrayList<>();
		}
		List<Type> types = typeDao.listByCategoryId(categoryId);
		List<TypeListVO> typeListVOs = Convert.sourceListToTargetList(types, TypeListVO.class);
		return typeListVOs;
	}
}
