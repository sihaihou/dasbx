package com.reyco.dasbx.open.core.service.developer;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.service.BaseService;
import com.reyco.dasbx.model.dto.DeleteDto;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperInsertDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperPageDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperReviewDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperUpdateDto;
import com.reyco.dasbx.open.core.model.vo.developer.DeveloperInfoVO;
import com.reyco.dasbx.open.core.model.vo.developer.DeveloperListVO;

public interface DeveloperService extends BaseService<DeveloperInfoVO, DeveloperListVO, ListDto, DeveloperUpdateDto, DeveloperInsertDto, DeleteDto>{
	
	PageInfo<DeveloperInfoVO> searchPage(DeveloperPageDto developerPageDto) throws AuthenticationException;
	/**
	 * 审批
	 * @param developerReviewDto
	 */
	void review(DeveloperReviewDto developerReviewDto);
	
	DeveloperInfoVO currentDeveloper() throws Exception;
	
}
