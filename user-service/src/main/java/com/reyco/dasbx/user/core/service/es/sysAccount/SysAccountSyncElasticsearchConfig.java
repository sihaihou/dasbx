package com.reyco.dasbx.user.core.service.es.sysAccount;

import org.springframework.context.annotation.Bean;

import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.sync.es.ElasticsearchSync;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;

//@Configuration
public class SysAccountSyncElasticsearchConfig {
	
	@Bean(SysAccountSyncElasticsearchServiceImpl.SYNC_NAME)
	public ElasticsearchSync<Long,Integer> abstractElasticsearchSync(ElasticsearchClient<ElasticsearchDocument> elasticsearchClient,
			SysAccountDao sysAccountDao){
		return new SysAccountSyncElasticsearchServiceImpl(elasticsearchClient,sysAccountDao);
	}
}
