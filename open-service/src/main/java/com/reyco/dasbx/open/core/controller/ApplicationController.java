package com.reyco.dasbx.open.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
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
import com.reyco.dasbx.open.core.service.ApplicationService;

@RestController
@RequestMapping("application")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;
	
	/**
	 * 根据 id查询
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Object info(@PathVariable("id") Long id) {
		ApplicationInfoVO applicationInfoVO = applicationService.get(id);
		return R.success(applicationInfoVO);
	}
	
	@GetMapping("getByClientId")
	public Object getByClientId(String clientId) {
		if(StringUtils.isBlank(clientId)) {
			return R.fail(R.PARAM_ERROR,"clientId 不能为空");
		}
		Application application = applicationService.getByClientId(clientId);
		return R.success(application);
	}
	@GetMapping("searchPage")
	public Object searchPage(ApplicationPageDto applicationPageDto) throws AuthenticationException{
		PageInfo<ApplicationListVO> applicationListVOs = applicationService.searchPage(applicationPageDto);
		return R.success(applicationListVOs);
	}
	/**
	 * 添加
	 * @param name
	 * @param desc
	 * @return
	 * @throws Exception 
	 */
	@PostMapping
	public Object insert(@RequestBody ApplicationInsertDto applicationInsertDto) throws Exception {
		ApplicationInfoVO applicationInfoVO = applicationService.insert(applicationInsertDto);
		return R.success(applicationInfoVO);
	}
	@PatchMapping
	public Object improveApplication(@RequestBody ApplicationImproveDto applicationImproveDto) {
		applicationService.improveApplication(applicationImproveDto);
		return R.success();
	}
	@PutMapping
	public Object update(@RequestBody ApplicationUpdateDto applicationUpdateDto) {
		applicationService.update(applicationUpdateDto);
		return R.success("修改成功");
	}
	@PatchMapping("updateSimpleInfo")
	public Object updateSimpleInfo(@RequestBody ApplicationUpdateSimpleDto applicationUpdateSimpleDto) {
		applicationService.updateSimpleInfo(applicationUpdateSimpleDto);
		return R.success();
	}
	@PatchMapping("updateMainInfo")
	public Object updateMainInfo(@RequestBody ApplicationUpdateMainDto applicationUpdateMainDto) {
		applicationService.updateMainInfo(applicationUpdateMainDto);
		return R.success();
	}
	@PatchMapping("review")
	public Object review(@RequestBody ApplicationReviewDto applicationReviewDto) {
		applicationService.reviewApplication(applicationReviewDto);
		return R.success();
	}
	@PatchMapping("delete")
	public Object delete(@RequestBody ApplicationDeleteDto applicationDeleteDto) {
		applicationService.delete(applicationDeleteDto);
		return R.success();
	}	
	
}
