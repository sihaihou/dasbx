package com.reyco.dasbx.common.core.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogPageDto;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.SysLoginLogListVO;
import com.reyco.dasbx.common.core.service.sys.SysLoginLogService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("/sys/loginLog")
public class SysLoginLogController {
	@Autowired
	private SysLoginLogService sysLoginLogService;
	
	@GetMapping("{id}")
	public Object info(@PathVariable("id")Long id) {
		SysLoginLogInfoVO sysLoginLogInfoVO = sysLoginLogService.get(id);
		return R.success(sysLoginLogInfoVO);
	}
	
	@GetMapping("search")
	public Object info(SysLoginLogPageDto sysLoginLogPageDto) {
		PageInfo<SysLoginLogListVO> sysLoginLogListVOPageInfo = sysLoginLogService.search(sysLoginLogPageDto);
		return R.success(sysLoginLogListVOPageInfo);
	}
}
