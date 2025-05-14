package com.reyco.dasbx.portal.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.portal.model.domain.dto.VideoInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoPlayEventDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoReviewDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListDetailVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.service.VideoService;
import com.reyco.dasbx.sync.exception.SyncException;

@RestController
@RequestMapping("/portal/video")
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	@GetMapping("{id}")
	public Object info(@PathVariable("id")Long id) {
		VideoInfoVO videoInfoVO = videoService.get(id);
		return R.success(videoInfoVO);
	}
	@GetMapping("suggestion")
	public Object getSuggestion(String keyword) throws Exception {
		List<String> suggestions = videoService.getSuggestion(keyword);
		return R.success(suggestions);
	}
	@GetMapping("search")
	public Object search(VideoSearchDto videoSearchDto) throws IOException {
		SearchVO<VideoListVO> searchVO = videoService.search(videoSearchDto);
		return R.success(searchVO);
	}
	@GetMapping("searchBack")
	public Object searchBack(VideoSearchDto videoSearchDto) throws IOException {
		SearchVO<VideoListDetailVO> searchVO = videoService.searchBack(videoSearchDto);
		return R.success(searchVO);
	}
	@PostMapping("init")
	public Object init() throws SyncException {
		int count = videoService.initElasticsearchVideo();
		return R.success(count);
	}
	/**
	 * 发布视频
	 * @param videoInsertDto
	 * @return
	 * @throws AuthenticationException
	 */
	@PostMapping
	public Object add(@RequestBody VideoInsertDto videoInsertDto) throws AuthenticationException {
		videoService.add(videoInsertDto);
		return R.success("发布成功，等待解码后可见...");
	}
	/**
	 * 审核
	 * @param videoReviewDto
	 * @return
	 * @throws AuthenticationException
	 */
	@PatchMapping("review")
	public Object review(@RequestBody VideoReviewDto videoReviewDto) throws AuthenticationException {
		videoService.review(videoReviewDto);
		return R.success();
	}
	/**
	 * 播放
	 * @param videoPlayEventDto
	 * @return
	 */
	@PatchMapping("play")
	public Callable<R<?>> play(@RequestBody VideoPlayEventDto videoPlayEventDto) {
		return new Callable<R<?>>() {
			@Override
			public R<?> call() throws Exception {
				videoService.addPlayEvent(videoPlayEventDto);
				return R.success("播放成功！");
			}
		};
	}
	
}
