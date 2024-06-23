package com.reyco.dasbx.model.dao;

import java.util.List;

import com.reyco.dasbx.model.po.DeletePO;

/**
 * 删除Dao
 * @author reyco
 * @param <T extends DeletePO>
 */
public interface DeleteDao<T extends DeletePO> {
	
	int deleteById(T t);
	
	int deleteByUid(T t);
	
	int deleteByExample(T t);
	
	int deleteBatch(List<T> list);
}
