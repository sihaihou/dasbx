package com.reyco.dasbx.user.core.service.es.sysAccount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.sync.es.AbstractElasticsearchSync;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.model.es.po.SysAccountElasticsearchDocument;

@Service(SysAccountSyncElasticsearchServiceImpl.SYNC_NAME)
public class SysAccountSyncElasticsearchServiceImpl extends AbstractElasticsearchSync<Long,SysAccountElasticsearchDocument>{
	
	public final static String SYNC_NAME= "sysAccountSyncElasticsearchService";
	
	private SysAccountDao sysAccountDao;
	
	@Autowired
	public SysAccountSyncElasticsearchServiceImpl(ElasticsearchClient<ElasticsearchDocument> elasticsearchClient,SysAccountDao sysAccountDao) {
		super(elasticsearchClient);
		this.sysAccountDao = sysAccountDao;
	}

	@Override
	protected SysAccountElasticsearchDocument getElasticsearchDocumentByPrimaryKeyId(Supplier<Long> supplier) {
		Long primaryKeyId = supplier.get();
		SysAccount sysAccount = sysAccountDao.getById(primaryKeyId);
		SysAccountElasticsearchDocument elasticsearchDocument = convert(sysAccount);
		return elasticsearchDocument;
	}

	@Override
	protected String getIndexName() {
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
	protected List<SysAccountElasticsearchDocument> getListByLimit(Long startId, Long endId) {
		List<SysAccount> sysAccounts = sysAccountDao.getListByLimit(startId, endId);
		List<SysAccountElasticsearchDocument> elasticsearchDocuments = convert(sysAccounts);
		return elasticsearchDocuments;
	}
	/**
	 * @param video
	 * @return
	 */
	protected List<SysAccountElasticsearchDocument> convert(List<SysAccount> sysAccounts) {
		if(sysAccounts==null || sysAccounts.size()==0) {
			return new ArrayList<>();
		}
		List<SysAccountElasticsearchDocument> elasticsearchDocuments = new ArrayList<>();
		SysAccountElasticsearchDocument elasticsearchDocument = null;
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
	protected SysAccountElasticsearchDocument convert(SysAccount sysAccount) {
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
