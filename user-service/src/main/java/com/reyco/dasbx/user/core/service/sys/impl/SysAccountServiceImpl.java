package com.reyco.dasbx.user.core.service.sys.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.PasswordUtils;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.encrypt.SimpleHash;
import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.lock.annotation.Lock;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.constants.OperationType;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.sync.es.ElasticsearchSync;
import com.reyco.dasbx.sync.exception.SyncException;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.model.dto.AccountBindDeveloperDto;
import com.reyco.dasbx.user.core.model.dto.AccountListDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountInsertDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountRegisterDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountUpdateDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDisableOrEnableDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountSearchDto;
import com.reyco.dasbx.user.core.model.es.po.SysAccountElasticsearchDocument;
import com.reyco.dasbx.user.core.model.msg.SysAccountSyncEsMessage;
import com.reyco.dasbx.user.core.model.po.AccountBindDeveloperPO;
import com.reyco.dasbx.user.core.model.po.AccountInsertPO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountDisableOrEnablePO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountSelectPO;
import com.reyco.dasbx.user.core.model.po.sys.SysAccountUpdatePO;
import com.reyco.dasbx.user.core.model.vo.AccountListVO;
import com.reyco.dasbx.user.core.model.vo.SysAccountInfoVO;
import com.reyco.dasbx.user.core.service.FullnameService;
import com.reyco.dasbx.user.core.service.es.sysAccount.SysAccountSearch;
import com.reyco.dasbx.user.core.service.es.sysAccount.SysAccountSyncElasticsearchServiceImpl;
import com.reyco.dasbx.user.core.service.sys.SysAccountService;
import com.reyco.dasbx.user.core.service.sys.SysUserRoleService;

@Service
public class SysAccountServiceImpl implements SysAccountService {

	@Autowired
	private SysAccountDao accountDao;
	@Autowired
	private IdGenerator<Long> idGenerator;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private ElasticsearchClient<SysAccountElasticsearchDocument> elasticsearchClient;
	@Autowired
	private SysAccountSearch sysAccountSearch;
	@Autowired
	private RabbitProducrService rabbitProducrService;
	@Autowired
	private FullnameService fullnameService;
	
	@Resource(name=SysAccountSyncElasticsearchServiceImpl.SYNC_NAME)
	private ElasticsearchSync<Long,Integer> elasticsearchSync;
	
	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_ACCOUNT_COMMENT_INFO_PREFIX,key="#id")
	public SysAccountInfoVO get(Long id) {
		SysAccount account = accountDao.getById(id);
		List<Long> roleIdList = sysUserRoleService.queryRoleIdsByUserId(id);
		SysAccountInfoVO accountInfoVO = Convert.sourceToTarget(account, SysAccountInfoVO.class);
		accountInfoVO.setRoleIdList(roleIdList);
		return accountInfoVO;
	}
	@Override
	public SysAccountInfoVO getByUid(String uid) {
		 SysAccount account = accountDao.getByUid(uid);
		 return Convert.sourceToTarget(account, SysAccountInfoVO.class);
	}
	@Override
	public SysAccountInfoVO getByEmail(String email) {
		SysAccount account = accountDao.getByEmail(email);
		 return Convert.sourceToTarget(account, SysAccountInfoVO.class);
	}
	@Override
	public SysAccount getByUsername(String username) {
		return accountDao.getByUsername(username);
	}
	@Override
	public List<String> getSuggestion(String keyword) throws Exception {
		List<String> suggestion = elasticsearchClient.getSuggestion(Constants.ACCOUNT_INDEX_NAME, keyword);
		if (suggestion == null) {
			suggestion = new ArrayList<>();
		}
		return suggestion;
	}
	@Override
	public SearchVO<SysAccountInfoVO> search(SysAccountSearchDto sysAccountSearchDto) throws IOException {
		return sysAccountSearch.search(sysAccountSearchDto);
	}
	@Override
	public int initElasticsearchSysAccount() throws SyncException {
		return elasticsearchSync.fullSync();
	}
	@Override
	public void updateState(SysAccountDisableOrEnableDto sysAccountDisableOrEnableDto){
		SysAccountDisableOrEnablePO sysAccountDisableOrEnablePO = Convert.sourceToTarget(sysAccountDisableOrEnableDto, SysAccountDisableOrEnablePO.class);
		accountDao.updateState(sysAccountDisableOrEnablePO);
		sendSyncEsMessage(sysAccountDisableOrEnablePO.getId(),OperationType.UPDATE);
	}
	@Override
	@Lock(value="account:register",key="#sysAccountRegisterDto.username")
	public void register(SysAccountRegisterDto sysAccountRegisterDto) {
		if(accountDao.getByUsername(sysAccountRegisterDto.getUsername())!=null) {
			throw new RuntimeException("用户已存在.");
		}
		AccountInsertPO accountInsertPO = Convert.sourceToTarget(sysAccountRegisterDto, AccountInsertPO.class);
		if(StringUtils.isBlank(accountInsertPO.getUid())) {
			accountInsertPO.setUid(idGenerator.getGeneratorId()+"");
		}
		SysAccount account = accountDao.getByUid(accountInsertPO.getUid());
		if(account!=null) {
			return;
		}
		if(StringUtils.isBlank(accountInsertPO.getUsername())) {
			accountInsertPO.setUsername(fullnameService.randomFullName().getFullname());
		}
		if(StringUtils.isBlank(accountInsertPO.getNickname())) {
			accountInsertPO.setNickname(accountInsertPO.getUsername());
		}
		accountInsertPO.setSalt(RandomStringUtils.randomAlphabetic(20));
		if(StringUtils.isBlank(accountInsertPO.getPassword())) {
			accountInsertPO.setPassword(PasswordUtils.getEncryptionPassword(new SimpleHash(SimpleHash.MD5,"123456").toHex(), accountInsertPO.getSalt()));
		}else {
			accountInsertPO.setPassword(PasswordUtils.getEncryptionPassword(accountInsertPO.getPassword(), accountInsertPO.getSalt()));
		}
		if(StringUtils.isNotBlank(sysAccountRegisterDto.getBirthdayDesc())) {
			accountInsertPO.setBirthday(Dasbx.getTimeByDateFormat(sysAccountRegisterDto.getBirthdayDesc(), Dasbx.FORMAT_YYYY_MM_DD));
		}
		accountInsertPO.setCreateBy(1L);
		accountInsertPO.setModifiedBy(1L);
		accountDao.insert(accountInsertPO);
		sendSyncEsMessage(accountInsertPO.getId(),OperationType.CREATE);
	}
	@Override
	public int getCountByTypeAndNameOrById(String username, Long userId) {
		return accountDao.getCountByNameAndById(username, userId);
	}
	
	@Override
	public SysAccountInfoVO currentUser() throws Exception {
		SysAccountToken token = TokenUtils.getToken();
		SysAccountInfoVO accountInfoVO = Convert.sourceToTarget(token, SysAccountInfoVO.class);
		return accountInfoVO;
	}
	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_ACCOUNT_COMMENT_INFO_PREFIX,key="#sysAccountUpdateDto.id")
	public void update(SysAccountUpdateDto sysAccountUpdateDto){
		SysAccount accountParameter = Convert.sourceToTarget(sysAccountUpdateDto, SysAccount.class);
		SysAccountUpdatePO sysAccountUpdatePO = Convert.sourceToTarget(accountParameter, SysAccountUpdatePO.class);
		if(StringUtils.isNotBlank(sysAccountUpdatePO.getPassword())) {
			SysAccount selectAccount = accountDao.getByUid(sysAccountUpdateDto.getUid());
			sysAccountUpdatePO.setPassword(PasswordUtils.getEncryptionPassword(sysAccountUpdatePO.getPassword(), selectAccount.getSalt()));
		}
		if(StringUtils.isNotBlank(sysAccountUpdateDto.getBirthdayDesc())) {
			sysAccountUpdatePO.setBirthday(Dasbx.getTimeByDateFormat(sysAccountUpdateDto.getBirthdayDesc(), Dasbx.FORMAT_YYYY_MM_DD));
		}
		accountDao.update(sysAccountUpdatePO);
		sysUserRoleService.saveOrUpdate(sysAccountUpdatePO.getId(), sysAccountUpdateDto.getRoleIdList());
		sendSyncEsMessage(sysAccountUpdatePO.getId(),OperationType.UPDATE);
	}
	@Override
	public SysAccountInfoVO insert(SysAccountInsertDto sysAccountInsertDto){
		AccountInsertPO accountInsertPO = Convert.sourceToTarget(sysAccountInsertDto, AccountInsertPO.class);
		if(StringUtils.isBlank(accountInsertPO.getUid())) {
			accountInsertPO.setUid(idGenerator.getGeneratorId()+"");
		}
		if(StringUtils.isBlank(accountInsertPO.getUsername())) {
			accountInsertPO.setUsername(accountInsertPO.getUid());
		}
		accountInsertPO.setSalt(RandomStringUtils.randomAlphabetic(20));
		if(StringUtils.isBlank(accountInsertPO.getPassword())) {
			accountInsertPO.setPassword(PasswordUtils.getEncryptionPassword(new SimpleHash(SimpleHash.MD5,"123456").toHex(), accountInsertPO.getSalt()));
		}else {
			accountInsertPO.setPassword(PasswordUtils.getEncryptionPassword(accountInsertPO.getPassword(), accountInsertPO.getSalt()));
		}
		if(StringUtils.isNotBlank(sysAccountInsertDto.getBirthdayDesc())) {
			accountInsertPO.setBirthday(Dasbx.getTimeByDateFormat(sysAccountInsertDto.getBirthdayDesc(), Dasbx.FORMAT_YYYY_MM_DD));
		}
		accountDao.insert(accountInsertPO);
		sysUserRoleService.saveOrUpdate(accountInsertPO.getId(), sysAccountInsertDto.getRoleIdList());
		sendSyncEsMessage(accountInsertPO.getId(),OperationType.CREATE);
		return Convert.sourceToTarget(accountInsertPO, SysAccountInfoVO.class);
	}


	@Override
	public List<AccountListVO> list(AccountListDto accountListDto) {
		SysAccountSelectPO accountSelectPO = Convert.sourceToTarget(accountListDto, SysAccountSelectPO.class);
		List<SysAccount> accounts = accountDao.list(accountSelectPO);
		List<AccountListVO> accountListVOs = Convert.sourceListToTargetList(accounts, AccountListVO.class);
		return accountListVOs;
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_ACCOUNT_COMMENT_INFO_PREFIX,key="#sysAccountDeleteDto.id")
	public void delete(SysAccountDeleteDto sysAccountDeleteDto) {
		SysAccountDeletePO sysAccountDeletePO = Convert.sourceToTarget(sysAccountDeleteDto, SysAccountDeletePO.class);
		accountDao.deleteById(sysAccountDeletePO);
		sendSyncEsMessage(sysAccountDeletePO.getId(),OperationType.DELETE);
	}
	@Override
	public void bindDeveloper(AccountBindDeveloperDto accountBindDeveloperDto) throws BusinessException {
		SysAccount account = accountDao.getByUid(accountBindDeveloperDto.getUid());
		if(account.getDeveloperId()!=null) {
			throw new BusinessException("开发者信息已经存在!");
		}
		AccountBindDeveloperPO accountBindDeveloperPO = Convert.sourceToTarget(accountBindDeveloperDto, AccountBindDeveloperPO.class);
		accountDao.bindDeveloper(accountBindDeveloperPO);
		sendSyncEsMessage(accountBindDeveloperPO.getId(),OperationType.UPDATE);
	}
	private void sendSyncEsMessage(Long sysAccountId,OperationType type) {
		SysAccountSyncEsMessage rabbitMessage = new SysAccountSyncEsMessage();
		rabbitMessage.setAccountId(sysAccountId);
		rabbitMessage.setType(type);
		rabbitProducrService.send(RabbitConstants.ACCOUNT_EXCHANGE,RabbitConstants.ACCOUNT_SYNC_ES_ROUTE_KEY,rabbitMessage);
	}
}
