package com.reyco.dasbx.common.core.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageSelectDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateHandleDto;
import com.reyco.dasbx.common.core.model.dto.sys.message.SysMessageUpdateReadDto;
import com.reyco.dasbx.common.core.model.vo.sys.message.SysMessageInfoVO;
import com.reyco.dasbx.common.core.service.sys.SysMessageService;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;

@RestController
@RequestMapping("/sys/message")
public class SysMessageController {
	@Autowired
	private SysMessageService sysMessageService;
	
	@GetMapping("{id}")
	public Object info(@PathVariable("id") Long id) throws AuthenticationException {
		SysMessageInfoVO sysMessageInfoVO = sysMessageService.getById(id);
		return R.success(sysMessageInfoVO);
	}
	@GetMapping("list")
	public Object list(SysMessageSelectDto sysMessageSelectDto) throws AuthenticationException {
		PageInfo<SysMessageInfoVO> list = sysMessageService.list(sysMessageSelectDto);
		return R.success(list);
	}
	@GetMapping("count")
	public Object count() throws AuthenticationException {
		Integer count = sysMessageService.getMessageCount();
		return R.success(count);
	}
	@PatchMapping("updateRead")
	public Object updateRead(@RequestBody SysMessageUpdateReadDto sysMessageUpdateReadDto) throws AuthenticationException {
		sysMessageService.updateRead(sysMessageUpdateReadDto);
		return R.success("ok");
	}
	@PatchMapping("updateHandle")
	public Object updateHandle(@RequestBody SysMessageUpdateHandleDto sysMessageUpdateHandleDto) throws AuthenticationException {
		sysMessageService.updateHandle(sysMessageUpdateHandleDto);
		return R.success("ok");
	}
	@DeleteMapping
	public Object delete(@RequestBody SysMessageDeleteDto sysMessageDeleteDto) throws AuthenticationException {
		sysMessageService.deleteById(sysMessageDeleteDto);
		return R.success("ok");
	}
}
