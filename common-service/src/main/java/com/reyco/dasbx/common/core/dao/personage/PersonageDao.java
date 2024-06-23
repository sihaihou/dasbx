package com.reyco.dasbx.common.core.dao.personage;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.common.core.model.po.personage.PersonageInsertPO;
import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.model.domain.Personage;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.model.po.SelectPO;
import com.reyco.dasbx.model.po.UpdatePO;

public interface PersonageDao extends BaseDao<Personage, PersonageInsertPO, DeletePO, UpdatePO, SelectPO>{
	
	Personage getByName(String name);
	
	List<Personage> getAll();
	
	void batchInsert(List<Personage> list);
	
	List<Personage> getList(String name);
	
	Long getMaxId();
	
	List<Personage> getListByLimit(@Param("startId")Long startId,@Param("endId")Long endId);
	
}
