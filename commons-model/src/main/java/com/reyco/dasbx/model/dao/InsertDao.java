package com.reyco.dasbx.model.dao;

import java.util.List;

import com.reyco.dasbx.model.po.InsertPO;

/**
 * 新增Dao
 * @author reyco
 * @param <T extends BasePO>
 */
public interface InsertDao<T extends InsertPO> {
	
	int insert(T t);
	
	int insertBatch(List<T> list);
}
