package com.reyco.dasbx.open.core.dao;

import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.open.core.model.domain.Developer;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperInsertPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperReviewPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperSelectPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperUpdatePO;

public interface DeveloperDao extends BaseDao<Developer, DeveloperInsertPO, DeletePO, DeveloperUpdatePO, DeveloperSelectPO> {
	/**
	 * 审批
	 */
	void review(DeveloperReviewPO developerReviewPO);
	
	
}
