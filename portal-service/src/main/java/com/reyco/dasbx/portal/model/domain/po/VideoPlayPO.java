package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class VideoPlayPO extends SimpleUpdatePO{
	private Integer version;
	private Integer playQuantity;
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getPlayQuantity() {
		return playQuantity;
	}
	public void setPlayQuantity(Integer playQuantity) {
		this.playQuantity = playQuantity;
	}
}
