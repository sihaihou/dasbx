package com.reyco.dasbx.portal.service.websocket;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.portal.model.domain.dto.VideoDanmakuListDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoDanmakuListVO;
import com.reyco.dasbx.portal.service.VideoDanmakuService;

@Service
public class DanmakuWebSocketChannelHandker implements WebSocketChannelHandler {
	
	@Autowired
	private VideoDanmakuService videoDanmakuService;
	
	@Override
	public WebSocketMessage HandleWebSocketChannel(ChannelHandlerParameter channelHandlerParameter) {
		if(StringUtils.isBlank(channelHandlerParameter.getWebSocketChannelhandlerMessage())) {
			return WebSocketMessage.success("大量弹幕即将来袭！！！");
		}
		VideoDanmakuListDto videoDanmakuListDto = new VideoDanmakuListDto();
		videoDanmakuListDto.setVideoId(channelHandlerParameter.getChannelId());
		videoDanmakuListDto.setStartDanmakuTime(new BigDecimal("0"));
		videoDanmakuListDto.setEndDanmakuTime(new BigDecimal("5"));
		List<VideoDanmakuListVO> videoDanmakuListVO = videoDanmakuService.listByVideoIdAndDanmakuTime(videoDanmakuListDto);
		return WebSocketMessage.success(videoDanmakuListVO);
	}
	
	
}
