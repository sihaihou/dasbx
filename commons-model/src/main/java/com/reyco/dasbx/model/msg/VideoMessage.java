package com.reyco.dasbx.model.msg;

import com.reyco.dasbx.commons.utils.ToString;

public class VideoMessage extends ToString  implements RabbitMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4942047515669019604L;
	private Long videoId;
	
	@Override
	public String getCorrelationDataId() {
		return videoId.toString();
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public Long getVideoId() {
		return videoId;
	}
}
