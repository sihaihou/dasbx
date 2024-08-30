package com.reyco.dasbx.oss.core;

public interface ImageOssResult extends OssResult{
	
	default Type getType() {
		return Type.IMAGE;
	}
	
	String getImageUrl();
}
