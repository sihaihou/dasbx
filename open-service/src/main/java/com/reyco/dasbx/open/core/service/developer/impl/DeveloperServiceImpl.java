package com.reyco.dasbx.open.core.service.developer.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.constants.ReviewStateType;
import com.reyco.dasbx.model.domain.Area;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.dto.DeleteDto;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.open.core.dao.DeveloperDao;
import com.reyco.dasbx.open.core.feign.AccountFeignClientService;
import com.reyco.dasbx.open.core.feign.AreaFeignClientService;
import com.reyco.dasbx.open.core.model.domain.Developer;
import com.reyco.dasbx.open.core.model.dto.developer.AccountBindDeveloperDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperInsertDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperPageDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperReviewDto;
import com.reyco.dasbx.open.core.model.dto.developer.DeveloperUpdateDto;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperInsertPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperReviewPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperSelectPO;
import com.reyco.dasbx.open.core.model.po.developer.DeveloperUpdatePO;
import com.reyco.dasbx.open.core.model.vo.developer.DeveloperInfoVO;
import com.reyco.dasbx.open.core.model.vo.developer.DeveloperListVO;
import com.reyco.dasbx.open.core.service.developer.DeveloperService;

@Service
public class DeveloperServiceImpl implements DeveloperService{
	private static Logger logger = LoggerFactory.getLogger(DeveloperServiceImpl.class);
	@Autowired
	private DeveloperDao developerDao;
	@Autowired
	private AreaFeignClientService areaFeignClientService;
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.OPEN_DEVELOPER_INFO_PREFIX,key="#id")
	public DeveloperInfoVO get(Long id) {
		Developer developer = developerDao.getById(id);
		DeveloperInfoVO developerInfoVO = Convert.sourceToTarget(developer, DeveloperInfoVO.class);
		buildIfNecessary(developerInfoVO);
		return developerInfoVO;
	}
	@Override
	public DeveloperInfoVO currentDeveloper() throws Exception {
		SysAccountToken token = TokenUtils.getToken();
		SysAccount account = accountFeignClientService.getByUid(token.getUid());
		Developer developer = developerDao.getById(account.getDeveloperId());
		DeveloperInfoVO developerInfoVO = Convert.sourceToTarget(developer, DeveloperInfoVO.class);
		buildIfNecessary(developerInfoVO);
		return developerInfoVO;
	}
	@Override
	public List<DeveloperListVO> list(ListDto listDto) {
		return null;
	}
	@Override
	public PageInfo<DeveloperInfoVO> searchPage(DeveloperPageDto developerPageDto) throws AuthenticationException {
		DeveloperSelectPO developerSelectPO = Convert.sourceToTarget(developerPageDto, DeveloperSelectPO.class);
		developerSelectPO.setName(developerPageDto.getKeyword());
		PageHelper.startPage(developerPageDto.getPageNum(), developerPageDto.getPageSize());
		List<Developer> developers = developerDao.list(developerSelectPO);
		PageInfo<Developer> developerPageInfo = new PageInfo<>(developers);
		if (CollectionUtils.isEmpty(developerPageInfo.getList())) {
			logger.debug("###################暂无数据");
			PageInfo<DeveloperInfoVO> developerInfoVOPageInfo = new PageInfo<>();
			return developerInfoVOPageInfo;
		}
		
		logger.debug("###################构建返回结果");
		List<DeveloperInfoVO> developerInfoVOs = Convert.sourceListToTargetList(developers, DeveloperInfoVO.class);
		// 封装分页
		PageInfo<DeveloperInfoVO> developerInfoVOPageInfo = new PageInfo<>(developerInfoVOs);
		developerInfoVOPageInfo.setTotal(developerPageInfo.getTotal());
		developerInfoVOPageInfo.setPages(developerPageInfo.getPages());
		developerInfoVOPageInfo.setPageNum(developerPageInfo.getPageNum());
		return developerInfoVOPageInfo;
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.OPEN_DEVELOPER_INFO_PREFIX,key="#developerUpdateDto.id")
	public void update(DeveloperUpdateDto developerUpdateDto) {
		DeveloperUpdatePO developerUpdatePO = Convert.sourceToTarget(developerUpdateDto, DeveloperUpdatePO.class);
		developerDao.update(developerUpdatePO);
	}

	@Override
	public DeveloperInfoVO insert(DeveloperInsertDto developerInsertDto) throws Exception {
		DeveloperInsertPO developerInsertPO = Convert.sourceToTarget(developerInsertDto, DeveloperInsertPO.class);
		developerDao.insert(developerInsertPO);
		SysAccountToken token = TokenUtils.getToken();
		AccountBindDeveloperDto accountBindDeveloperDto = new AccountBindDeveloperDto();
		accountBindDeveloperDto.setUid(token.getUid());
		accountBindDeveloperDto.setDeveloperId(developerInsertPO.getId());
		accountFeignClientService.bindDeveloper(accountBindDeveloperDto);
		token.setDeveloperId(developerInsertPO.getId());
		TokenUtils.createToken(token);
		return Convert.sourceToTarget(developerInsertPO, DeveloperInfoVO.class);
	}

	@Override
	public void delete(DeleteDto t4) {
		
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.OPEN_DEVELOPER_INFO_PREFIX,key="#developerReviewDto.id")
	public void review(DeveloperReviewDto developerReviewDto) {
		DeveloperReviewPO developerReviewPO = Convert.sourceToTarget(developerReviewDto, DeveloperReviewPO.class);
		developerDao.review(developerReviewPO);
	}
	private void buildIfNecessary(DeveloperInfoVO developerInfoVO){
		Long provinceId = developerInfoVO.getProvinceId();
		Area areaProvince = areaFeignClientService.getById(provinceId);
		if(areaProvince!=null) {
			developerInfoVO.setProvinceDesc(areaProvince.getName());
		}
		Long cityId = developerInfoVO.getCityId();
		Area areaCity = areaFeignClientService.getById(cityId);
		if(areaCity!=null) {
			developerInfoVO.setCityDesc(areaCity.getName());
		}
		developerInfoVO.setStateDesc(ReviewStateType.getReviewStateType(developerInfoVO.getState()).getDesc());
	}
}
