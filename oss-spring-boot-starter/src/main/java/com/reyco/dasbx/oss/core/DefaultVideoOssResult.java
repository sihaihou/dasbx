package com.reyco.dasbx.oss.core;

public class DefaultVideoOssResult extends DefaultOssResult implements VideoOssResult{
	
	private String portraitCoverUrl;
	
	private String landscapeCoverUrl;
	
	public String getVideoUrl() {
		return this.url;
	}
	public String getPortraitCoverUrl() {
		return this.portraitCoverUrl;
	}
	public void setPortraitCoverUrl(String portraitCoverUrl) {
		this.portraitCoverUrl = portraitCoverUrl;
	}
	public String getLandscapeCoverUrl() {
		return this.landscapeCoverUrl;
	}
	public void setLandscapeCoverUrl(String landscapeCoverUrl) {
		this.landscapeCoverUrl = landscapeCoverUrl;
	}
}
