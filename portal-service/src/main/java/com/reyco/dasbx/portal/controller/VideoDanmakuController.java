package com.reyco.dasbx.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuListDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuListVO;
import com.reyco.dasbx.portal.service.VideoDanmakuService;

@RestController
@RequestMapping("/portal/danmaku")
public class VideoDanmakuController {

	@Autowired
	private VideoDanmakuService videoDanmakuService;
	
	@GetMapping("list")
	public Object list(VideoDanmakuListDto videoDanmakuListDto) {
		List<VideoDanmakuListVO> videoDanmakuListVOs = videoDanmakuService.listByVideoIdAndDanmakuTime(videoDanmakuListDto);
		return R.success(videoDanmakuListVOs);
	}
	@PostMapping
	public Object save(@RequestBody VideoDanmakuInsertDto videoDanmakuInsertDto) {
		VideoDanmakuInfoVO videoDanmakuInfoVO = videoDanmakuService.save(videoDanmakuInsertDto);
		return R.success(videoDanmakuInfoVO);
	}
	
}
