package com.reyco.dasbx.portal.service;

import java.util.List;
import java.util.Map;

public interface VideoPublishListService {
	/**
	 * 发布视频前，类别相关列表
	 * @return
	 */
	Map<String,List<?>> listForAdd() throws Exception;
}
