package com.reyco.dasbx.oss.core;

public class DefaultImageOssResult extends DefaultOssResult implements ImageOssResult{
	
	@Override
	public String getImageUrl() {
		return this.url;
	}

}
