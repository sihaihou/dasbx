package com.reyco.dasbx.open.core.service;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.service.BaseService;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.vo.ListVO;
import com.reyco.dasbx.open.core.model.domain.Application;
import com.reyco.dasbx.open.core.model.dto.ApplicationDeleteDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationImproveDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationInsertDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationPageDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationReviewDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateMainDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateSimpleDto;
import com.reyco.dasbx.open.core.model.vo.ApplicationInfoVO;
import com.reyco.dasbx.open.core.model.vo.ApplicationListVO;

public interface ApplicationService extends BaseService<ApplicationInfoVO, ListVO, ListDto, ApplicationUpdateDto, ApplicationInsertDto, ApplicationDeleteDto> {
	
	Application getByClientId(String clientId);
	
	PageInfo<ApplicationListVO> searchPage(ApplicationPageDto applicationPageDto) throws AuthenticationException ;
	
	/**
	 * 完善资料
	 * @param applicationImproveDto
	 */
	void improveApplication(ApplicationImproveDto applicationImproveDto);
	
	/**
	 * 更新基本信息
	 * @param applicationUpdateSimpleDto
	 */
	void updateSimpleInfo(ApplicationUpdateSimpleDto applicationUpdateSimpleDto);
	
	/**
	 * 更新主要信息
	 * @param applicationUpdateMainDto
	 */
	void updateMainInfo(ApplicationUpdateMainDto applicationUpdateMainDto);
	
	/**
	 * 审核
	 * @param applicationReviewDto
	 */
	void reviewApplication(ApplicationReviewDto applicationReviewDto);
	
}
