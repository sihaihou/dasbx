package com.reyco.dasbx.open.core.dao;

import java.util.List;

import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.model.po.InsertPO;
import com.reyco.dasbx.model.po.SelectPO;
import com.reyco.dasbx.model.po.UpdatePO;
import com.reyco.dasbx.open.core.model.domain.ApplicationCategory;

public interface ApplicationCategoryDao extends BaseDao<ApplicationCategory, InsertPO, DeletePO, UpdatePO, SelectPO>{
	
	List<ApplicationCategory> getChildsByParentId(Long id);
	
}
