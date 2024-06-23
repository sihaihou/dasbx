package com.reyco.dasbx.open.core.dao;

import com.reyco.dasbx.model.dao.BaseDao;
import com.reyco.dasbx.open.core.model.domain.Application;
import com.reyco.dasbx.open.core.model.po.ApplicationDeletePO;
import com.reyco.dasbx.open.core.model.po.ApplicationImprovePo;
import com.reyco.dasbx.open.core.model.po.ApplicationInsertPO;
import com.reyco.dasbx.open.core.model.po.ApplicationReviewPO;
import com.reyco.dasbx.open.core.model.po.ApplicationSelectPO;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdateMainPo;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdatePO;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdateSimplePO;

public interface ApplicationDao extends BaseDao<Application, ApplicationInsertPO, ApplicationDeletePO, ApplicationUpdatePO,ApplicationSelectPO> {
	/**
	 * 查询
	 * @param clientId
	 * @return
	 */
	Application getByClientId(String clientId);
	/**
	 * 完善资料
	 * @param applicationImproveDto
	 */
	void improveApplication(ApplicationImprovePo applicationImprovePo );
	
	/**
	 * 更新基本信息
	 * @param applicationUpdateSimpleDto
	 */
	void updateSimpleInfo(ApplicationUpdateSimplePO applicationUpdateSimplePO);
	
	/**
	 * 更新主要信息
	 * @param applicationUpdateMainDto
	 */
	void updateMainInfo(ApplicationUpdateMainPo applicationUpdateMainPo);
	
	/**
	 * 审核
	 * @param applicationReviewPO
	 */
	void reviewApplication(ApplicationReviewPO applicationReviewPO);
	
	/**
	 * 删除
	 * @param applicationDeletePO
	 */
	void deleteApplication(ApplicationDeletePO applicationDeletePO);
	
	
	
}
