package com.reyco.dasbx.oss.core;

public interface VideoOssResult extends OssResult{
	
	default Type getType() {
		return Type.VIDEO;
	}
	
	public String getVideoUrl();
	
	public String getPortraitCoverUrl();
	
	public String getLandscapeCoverUrl();
	
}
