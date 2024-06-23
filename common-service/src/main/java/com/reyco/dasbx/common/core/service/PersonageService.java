package com.reyco.dasbx.common.core.service;

import java.io.IOException;
import java.util.List;

import com.reyco.dasbx.common.core.model.dto.personage.PersonageDeleteDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageInsertDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageSearchDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageUpdateDto;
import com.reyco.dasbx.common.core.model.vo.personage.PersonageInfoVO;
import com.reyco.dasbx.config.service.BaseService;
import com.reyco.dasbx.es.core.search.ElasticsearchVO;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.vo.ListVO;

public interface PersonageService extends BaseService<PersonageInfoVO, ListVO, ListDto, PersonageUpdateDto, PersonageInsertDto, PersonageDeleteDto> {
	/**
	 * 随机生成角色人物
	 * @return
	 * @throws IOException
	 */
	int randomPersonage() throws IOException ;
	/**
	 * 初始化角色人物
	 * @return
	 * @throws IOException
	 */
	int initPersonage() throws IOException ;
	/**
	 * 初始化es
	 * @return
	 * @throws IOException
	 */
	void initElasticsearchPersonage() throws IOException ;
	/**
	 * 获取自动提示
	 * @param keyword
	 * @return
	 */
	List<String> getSuggestion(String keyword) throws Exception;
	/**
	 * 搜索
	 * @param keyword
	 * @return
	 */
	ElasticsearchVO<PersonageInfoVO> search(PersonageSearchDto personageSearchDto) throws Exception;
}
