package com.reyco.dasbx.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.vo.VideoProductionInfoVO;
import com.reyco.dasbx.portal.service.VideoProductionService;

@RestController
@RequestMapping("/portal/production")
public class VideoProductionController {

	@Autowired
	private VideoProductionService videoProductionService;
	
	@GetMapping("getByVideoId")
	public Object getByVideoId(Long videoId) {
		VideoProductionInfoVO videoProductionInfoVO = videoProductionService.getByVideoId(videoId);
		return R.success(videoProductionInfoVO);
	}
	
}
