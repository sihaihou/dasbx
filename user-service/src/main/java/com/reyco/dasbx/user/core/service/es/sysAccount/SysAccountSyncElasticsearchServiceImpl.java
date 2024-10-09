package com.reyco.dasbx.user.core.service.es.sysAccount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.sync.AbstractSyncElasticsearchService;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.model.es.po.SysAccountElasticsearchDocument;


@Service("sysAccountSyncElasticsearchService")
public class SysAccountSyncElasticsearchServiceImpl extends AbstractSyncElasticsearchService{
	
	@Autowired
	private SysAccountDao sysAccountDao;
	
	@Override
	public String getIndexName() {
		return Constants.ACCOUNT_INDEX_NAME;
	}
	@Override
	protected String getIndexDSL() {
		return "";
	}
	@Override
	protected Long getDocumentMaxId() {
		return sysAccountDao.getMaxId();
	}
	@Override
	protected List<ElasticsearchDocument> getListByLimit(Long startId, Long endId) {
		List<SysAccount> sysAccounts = sysAccountDao.getListByLimit(startId, endId);
		List<ElasticsearchDocument> elasticsearchDocuments = convert(sysAccounts);
		return elasticsearchDocuments;
	}
	@Override
	public ElasticsearchDocument getElasticsearchDocumentByPrimaryKeyId(Object primaryKeyId) {
		Long pkId = (Long)primaryKeyId;
		SysAccount sysAccount = sysAccountDao.getById(pkId);
		ElasticsearchDocument elasticsearchDocument = convert(sysAccount);
		return elasticsearchDocument;
	}
	/**
	 * @param video
	 * @return
	 */
	protected List<ElasticsearchDocument> convert(List<SysAccount> sysAccounts) {
		if(sysAccounts==null || sysAccounts.size()==0) {
			return new ArrayList<>();
		}
		List<ElasticsearchDocument> elasticsearchDocuments = new ArrayList<>();
		ElasticsearchDocument elasticsearchDocument = null;
		for (SysAccount sysAccount : sysAccounts) {
			elasticsearchDocument = convert(sysAccount);
			elasticsearchDocuments.add(elasticsearchDocument);
		}
		return elasticsearchDocuments;
	}
	/**
	 * @param video
	 * @return
	 */
	protected ElasticsearchDocument convert(SysAccount sysAccount) {
		if(sysAccount==null) {
			return null;
		}
		SysAccountElasticsearchDocument sysAccountElasticsearchDocument = Convert.sourceToTarget(sysAccount, SysAccountElasticsearchDocument.class);
		Set<String> suggestionSet = new HashSet<>();
		suggestionSet.add(sysAccountElasticsearchDocument.getUsername());
		suggestionSet.add(sysAccountElasticsearchDocument.getNickname());
		suggestionSet.add(sysAccountElasticsearchDocument.getPhone());
		suggestionSet.add(sysAccountElasticsearchDocument.getEmail());
		sysAccountElasticsearchDocument.setSuggestion(new ArrayList<>(suggestionSet));
		return sysAccountElasticsearchDocument;
	}
}
