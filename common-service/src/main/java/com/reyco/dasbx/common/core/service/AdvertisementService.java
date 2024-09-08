package com.reyco.dasbx.common.core.service;

import java.util.List;

import com.reyco.dasbx.common.core.model.domain.sys.Advertisement;

public interface AdvertisementService {
	/**
	 * 获取顶部广告
	 * @return
	 */
	List<Advertisement> getAdvertisementByTop();
	
	/**
	 * 获取左侧广告
	 * @return
	 */
	List<Advertisement> getAdvertisementByLeft();
	/**
	 * 获取右侧广告
	 * @return
	 */
	List<Advertisement> getAdvertisementByRight();
	
	/**
	 * 获取头部广告
	 * @return
	 */
	List<Advertisement> getAdvertisementByHeader();
	
}
