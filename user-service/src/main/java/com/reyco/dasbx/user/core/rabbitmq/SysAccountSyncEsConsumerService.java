package com.reyco.dasbx.user.core.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.model.constants.OperationType;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.msg.SysAccountSyncEsMessage;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.model.es.po.SysAccountElasticsearchDocument;

@Service
public class SysAccountSyncEsConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountRabbitConsumerService.class);
	@Autowired
	private SysAccountDao accountDao;
	
	@Autowired
	private ElasticsearchClient<SysAccountElasticsearchDocument> elasticsearchClient;
	
	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = RabbitConstants.ACCOUNT_SYNC_ES_QUEUE, durable = "true", exclusive = "false", autoDelete = "false"), 
			exchange = @Exchange(value = RabbitConstants.ACCOUNT_EXCHANGE, type = ExchangeTypes.DIRECT, durable = "true", autoDelete = "false"), 
			key = RabbitConstants.ACCOUNT_SYNC_ES_ROUTE_KEY))
	public void log(SysAccountSyncEsMessage sysAccountSyncEsMessage, Channel channel, Message message) {
		try {
			OperationType type = sysAccountSyncEsMessage.getType();
			if(OperationType.CREATE==type || OperationType.UPDATE==type) {
				syncCreateOrUpdate(sysAccountSyncEsMessage.getAccountId());
			}else if(OperationType.DELETE==type) {
				syncDelete(sysAccountSyncEsMessage.getAccountId());
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费:用户ES同步】消息消费成功, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysAccountSyncEsMessage);
		} catch (IOException e) {
			logger.error("【消息消费:用户ES同步】消息消费失败, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysAccountSyncEsMessage);
			//根据业务重试...
		}
	}
	private void syncDelete(Long accountId) throws IOException {
		if(elasticsearchClient.existsDocument(Constants.ACCOUNT_INDEX_NAME, accountId.toString())) {
			elasticsearchClient.deleteDocument(Constants.ACCOUNT_INDEX_NAME, accountId.toString());
		}
	}
	private void syncCreateOrUpdate(Long accountId) throws IOException {
		SysAccountElasticsearchDocument sysAccountElasticsearchDocument = getSysAccountElasticsearchDocument(accountId);
		if(elasticsearchClient.existsDocument(Constants.ACCOUNT_INDEX_NAME, accountId.toString())) {
			elasticsearchClient.updateDocument(Constants.ACCOUNT_INDEX_NAME, sysAccountElasticsearchDocument);
		}else {
			elasticsearchClient.addDocument(Constants.ACCOUNT_INDEX_NAME, sysAccountElasticsearchDocument);
		}
	}
	/**
	 * 根据Id获取对象
	 * @param sysAccountId
	 * @return
	 */
	private SysAccountElasticsearchDocument getSysAccountElasticsearchDocument(Long sysAccountId){
		SysAccount sysAccount = accountDao.getById(sysAccountId);
		return sysAccountToSysAccountElasticsearchDocument(sysAccount);
	}
	/**
	 * 
	 * @param sysAccount
	 * @return
	 */
	private SysAccountElasticsearchDocument sysAccountToSysAccountElasticsearchDocument(SysAccount sysAccount) {
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
