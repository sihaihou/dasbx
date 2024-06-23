package com.reyco.dasbx.portal.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.model.domain.dto.VideoPageDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.service.VideoService;

@RestController
@RequestMapping("/portal/video")
public class VideoController {
	@Autowired
	private VideoService videoService;
	
	@GetMapping("{id}")
	public Object search(@PathVariable("id")Long id) {
		VideoInfoVO videoInfoVO = videoService.get(id);
		return R.success(videoInfoVO);
	}
	
	@GetMapping("search")
	public Object search(VideoPageDto videoPageDto) {
		PageInfo<VideoListVO> pageInfo = videoService.list(videoPageDto);
		return R.success(pageInfo);
	}
	
	@PatchMapping("play")
	public Callable<R> play(@RequestBody VideoPlayEventDto videoPlayEventDto) {
		return new Callable<R>() {
			@Override
			public R call() throws Exception {
				videoService.addPlayEvent(videoPlayEventDto);
				return R.success("播放成功！");
			}
		};
	}
}
