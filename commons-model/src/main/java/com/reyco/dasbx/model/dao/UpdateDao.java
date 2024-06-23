package com.reyco.dasbx.model.dao;

import java.util.List;

import com.reyco.dasbx.model.po.UpdatePO;

/**
 * 更新Dao
 * @author reyco
 * @param <T extends BasePO>
 */
public interface UpdateDao<T extends UpdatePO> {

	int update(T t);
	
	int updateBatch(List<T> list);
}
