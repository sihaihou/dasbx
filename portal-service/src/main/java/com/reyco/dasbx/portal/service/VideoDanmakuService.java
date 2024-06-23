package com.reyco.dasbx.portal.service;

import java.util.List;

import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuInsertDto;
import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuListDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuInfoVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuListVO;

public interface VideoDanmakuService {
	
	List<VideoDanmakuListVO> listByVideoIdAndDanmakuTime(VideoDanmakuListDto videoDanmakuListDto);
	
	
	VideoDanmakuInfoVO save(VideoDanmakuInsertDto videoDanmakuInsertDto);
}
