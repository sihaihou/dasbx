package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.model.domain.Area;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.model.po.InsertPO;
import com.reyco.dasbx.model.po.SelectPO;
import com.reyco.dasbx.model.po.UpdatePO;

public interface AreaDao extends BaseDao<Area, InsertPO, DeletePO, UpdatePO, SelectPO> {
	
	Area getByName(String name);
	
	List<Area> getChildsByParentId(Long id);
	
	List<Area> getAll();
	
	Long getMaxId();
	
	List<Area> getListByLimit(@Param("startId")Long startId,@Param("endId")Long endId);
}
