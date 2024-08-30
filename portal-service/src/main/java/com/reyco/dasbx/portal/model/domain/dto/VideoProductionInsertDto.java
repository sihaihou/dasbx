package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class VideoProductionInsertDto extends SimpleInsertDto {
	private String director = "未知";
	private String star = "未知";
	private String introduction = "未知";
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
