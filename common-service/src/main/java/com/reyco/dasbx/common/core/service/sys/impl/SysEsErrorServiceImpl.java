package com.reyco.dasbx.common.core.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.reyco.dasbx.common.core.dao.sys.SysEsErrorDao;
import com.reyco.dasbx.common.core.model.po.sys.SysEsErrorInsertPO;
import com.reyco.dasbx.common.core.service.sys.SysEsErrorService;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.es.core.event.ElasticSearcchErrorEvent;
import com.reyco.dasbx.es.core.event.ElasticSearcchErrorEvent.ProcessFailureMessage;
import com.reyco.dasbx.model.domain.SysEsError;

@Service
public class SysEsErrorServiceImpl implements SysEsErrorService,ApplicationListener<ElasticSearcchErrorEvent> {
	
	@Autowired
	private SysEsErrorDao sysEsErrorDao;
	
	@Override
	public SysEsError get(Long id) {
		return null;
	}

	@Override
	public PageInfo<SysEsError> search(SysEsError SysEsError) {
		return null;
	}

	@Override
	public void onApplicationEvent(ElasticSearcchErrorEvent event) {
		List<ProcessFailureMessage> processFailureMessages = event.getProcessFailureMessages();
		processFailureMessages.stream().forEach(processFailureMessage->{
			SysEsErrorInsertPO sysEsErrorInsertPO = new SysEsErrorInsertPO();
			sysEsErrorInsertPO.setIndex(processFailureMessage.getIndex());
			sysEsErrorInsertPO.setType(processFailureMessage.getType());
			sysEsErrorInsertPO.setPrimaryKey(processFailureMessage.getPrimarykey());
			sysEsErrorInsertPO.setFailureMessage(processFailureMessage.getFailureMessage());
			sysEsErrorInsertPO.setCreateBy(1L);
			sysEsErrorInsertPO.setGmtCreate(Dasbx.getCurrentTime());
			sysEsErrorInsertPO.setModifiedBy(1L);
			sysEsErrorInsertPO.setGmtModified(Dasbx.getCurrentTime());
			sysEsErrorDao.batchInsert(sysEsErrorInsertPO);
		});
		//sysEsErrorDao.batchInsert(sysEsErrorInsertPOs);
	}

}
