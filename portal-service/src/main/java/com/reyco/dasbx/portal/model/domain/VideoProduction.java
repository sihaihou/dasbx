package com.reyco.dasbx.portal.model.domain;

import com.reyco.dasbx.model.Base;

public class VideoProduction extends Base {
	private Long videoId;
	private String director;
	private String star;
	private String introduction;
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}
