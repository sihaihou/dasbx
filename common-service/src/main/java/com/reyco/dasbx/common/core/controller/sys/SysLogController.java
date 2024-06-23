package com.reyco.dasbx.common.core.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogPageDto;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLogListVO;
import com.reyco.dasbx.common.core.service.sys.SysLogService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {
	
	@Autowired
	private SysLogService sysLogService;
	
	@GetMapping("{id}")
	public Object info(@PathVariable("id")Long id) {
		SysLogInfoVO sysLogInfoVO = sysLogService.get(id);
		return R.success(sysLogInfoVO);
	}
	
	@GetMapping("search")
	public Object info(SysLogPageDto sysLogPageDto) {
		PageInfo<SysLogListVO> sysLogListVOPageInfo = sysLogService.search(sysLogPageDto);
		return R.success(sysLogListVOPageInfo);
	}
}
