package com.reyco.dasbx.open.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.id.core.UUIdGenerator;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.vo.ListVO;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.open.core.dao.ApplicationCategoryDao;
import com.reyco.dasbx.open.core.dao.ApplicationDao;
import com.reyco.dasbx.open.core.dao.DeveloperApplicationDao;
import com.reyco.dasbx.open.core.model.domain.Application;
import com.reyco.dasbx.open.core.model.dto.ApplicationDeleteDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationImproveDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationInsertDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationPageDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationReviewDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateMainDto;
import com.reyco.dasbx.open.core.model.dto.ApplicationUpdateSimpleDto;
import com.reyco.dasbx.open.core.model.po.ApplicationDeletePO;
import com.reyco.dasbx.open.core.model.po.ApplicationImprovePo;
import com.reyco.dasbx.open.core.model.po.ApplicationInsertPO;
import com.reyco.dasbx.open.core.model.po.ApplicationReviewPO;
import com.reyco.dasbx.open.core.model.po.ApplicationSelectPO;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdateMainPo;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdatePO;
import com.reyco.dasbx.open.core.model.po.ApplicationUpdateSimplePO;
import com.reyco.dasbx.open.core.model.po.developerApplication.DeveloperApplicationInsertPO;
import com.reyco.dasbx.open.core.model.vo.ApplicationInfoVO;
import com.reyco.dasbx.open.core.model.vo.ApplicationListVO;
import com.reyco.dasbx.open.core.service.ApplicationService;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {
	private static Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);
	@Autowired
	private ApplicationDao applicationDao;
	@Autowired
	private ApplicationCategoryDao applicationCategoryDao;
	@Autowired
	private DeveloperApplicationDao developerApplicationDao;
	
	@Override
	public ApplicationInfoVO get(Long id) {
		Application application = applicationDao.getById(id);
		ApplicationInfoVO applicationInfoVO = Convert.sourceToTarget(application, ApplicationInfoVO.class);
		applicationInfoVO.setFirstCategoryDesc(applicationCategoryDao.getById(applicationInfoVO.getFirstCategoryId()).getName());
		applicationInfoVO.setSecondCategoryDesc(applicationCategoryDao.getById(applicationInfoVO.getSecondCategoryId()).getName());
		return applicationInfoVO;
	}
	@Override
	public Application getByClientId(String clientId) {
		return applicationDao.getByClientId(clientId);
	}
	@Override
	public List<ListVO> list(ListDto t1) {
		return null;
	}
	@Override
	public PageInfo<ApplicationListVO> searchPage(final ApplicationPageDto applicationPageDto) throws AuthenticationException {
		PageHelper.startPage(applicationPageDto.getPageNum(), applicationPageDto.getPageSize());
		SysAccountToken token = TokenUtils.getToken();
		Long developerId = token.getDeveloperId();
		if(applicationPageDto.getApplicationSelectPO()==null) {
			applicationPageDto.setApplicationSelectPO(new ApplicationSelectPO());
		}
		applicationPageDto.getApplicationSelectPO().setDeveloperId(developerId);
		List<Application> applications = applicationDao.list(applicationPageDto.getApplicationSelectPO());
	
		PageInfo<Application> applicationPageInfo = new PageInfo<>(applications);
		if (CollectionUtils.isEmpty(applicationPageInfo.getList())) {
			logger.info("###################暂无数据");
			PageInfo<ApplicationListVO> applicationListVOPageInfo = new PageInfo<>();
			return applicationListVOPageInfo;
		}
		
		logger.info("###################构建返回结果");
		List<ApplicationListVO> applicationListVOs = Convert.sourceListToTargetList(applications, ApplicationListVO.class);
		// 封装分页
		PageInfo<ApplicationListVO> applicationListVOPageInfo = new PageInfo<>(applicationListVOs);
		applicationListVOPageInfo.setTotal(applicationPageInfo.getTotal());
		applicationListVOPageInfo.setPages(applicationPageInfo.getPages());
		applicationListVOPageInfo.getList().stream().forEach(applicationInfoVO->{
			String format = DateUtils.format(applicationInfoVO.getGmtCreate(), DateUtils.DATE_FORMAT);
			applicationInfoVO.setCreateDesc(format);
		});
		return applicationListVOPageInfo;
	}
	@Override
	public ApplicationInfoVO insert(ApplicationInsertDto applicationInsertDto) throws AuthenticationException {
		String clientId = RandomStringUtils.randomAlphabetic(16);
		String clientSecret = UUIdGenerator.getUUId();
		ApplicationInsertPO applicationInsertPO = Convert.sourceToTarget(applicationInsertDto, ApplicationInsertPO.class);
		applicationInsertPO.setClientId(clientId);
		applicationInsertPO.setClientSecret(clientSecret);
		applicationDao.insert(applicationInsertPO);
		//
		DeveloperApplicationInsertPO developerApplicationInsertPO = new DeveloperApplicationInsertPO();
		SysAccountToken token = TokenUtils.getToken();
		Long developerId = token.getDeveloperId();
		developerApplicationInsertPO.setDeveloperId(developerId);
		developerApplicationInsertPO.setApplicationId(applicationInsertPO.getId());
		developerApplicationDao.insert(developerApplicationInsertPO);
		
		ApplicationInfoVO applicationInfoVO = Convert.sourceToTarget(applicationInsertPO, ApplicationInfoVO.class);
		return applicationInfoVO;
	}
	@Override
	public void improveApplication(ApplicationImproveDto applicationImproveDto) {
		ApplicationImprovePo applicationImprovePo = Convert.sourceToTarget(applicationImproveDto, ApplicationImprovePo.class);
		applicationDao.improveApplication(applicationImprovePo);
	}
	@Override
	public void update(ApplicationUpdateDto applicationUpdateDto) {
		ApplicationUpdatePO applicationUpdatePO = Convert.sourceToTarget(applicationUpdateDto, ApplicationUpdatePO.class);
		applicationDao.update(applicationUpdatePO);
	}
	@Override
	public void updateSimpleInfo(ApplicationUpdateSimpleDto applicationUpdateSimpleDto) {
		ApplicationUpdateSimplePO applicationUpdateSimplePO = Convert.sourceToTarget(applicationUpdateSimpleDto, ApplicationUpdateSimplePO.class);
		applicationDao.updateSimpleInfo(applicationUpdateSimplePO);
	}
	@Override
	public void updateMainInfo(ApplicationUpdateMainDto applicationUpdateMainDto) {
		ApplicationUpdateMainPo applicationUpdateMainPo = Convert.sourceToTarget(applicationUpdateMainDto, ApplicationUpdateMainPo.class);
		applicationDao.updateMainInfo(applicationUpdateMainPo);
	}
	@Override
	public void reviewApplication(ApplicationReviewDto applicationReviewDto) {
		ApplicationReviewPO applicationReviewPO = Convert.sourceToTarget(applicationReviewDto, ApplicationReviewPO.class);
		applicationDao.reviewApplication(applicationReviewPO);
	}
	@Override
	public void delete(ApplicationDeleteDto applicationDeleteDto) {
		ApplicationDeletePO applicationDeletePO = Convert.sourceToTarget(applicationDeleteDto, ApplicationDeletePO.class);
		applicationDao.deleteApplication(applicationDeletePO);
	}
}
