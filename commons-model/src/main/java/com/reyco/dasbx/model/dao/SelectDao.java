package com.reyco.dasbx.model.dao;

import java.util.List;

import com.reyco.dasbx.model.Base;
import com.reyco.dasbx.model.po.SelectPO;

/**
 * 查询Dao
 * @author reyco
 * @param <V>
 * @param <T>
 */
public interface SelectDao<V extends Base,T extends SelectPO> {
	
	V getById(Long id);
	
	V getByUid(String uid);
	
	List<V> getByExample(T t);
	
	List<V> list(T t);
	
}
