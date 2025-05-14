package com.reyco.dasbx.portal.rabbitmq;

import javax.annotation.Resource;

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
import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;
import com.reyco.dasbx.portal.model.msg.VideoMessage;
import com.reyco.dasbx.portal.service.SysMessageService;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;
import com.reyco.dasbx.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;
import com.reyco.dasbx.sync.es.AbstractElasticsearchSync;

@Service
public class VideoSyncEsRabbitConsumerService extends AbstractRabbitConsumerService{

	private static final Logger logger = LoggerFactory.getLogger(VideoSyncEsRabbitConsumerService.class);

	@Autowired
	private SysMessageService sysMessageService;
	
	@Resource(name="videoSyncElasticsearchSerivce")
	private AbstractElasticsearchSync<Long,VideoElasticsearchDocument> syncElasticsearchService;
	
	@Autowired
	public VideoSyncEsRabbitConsumerService(RedisUtil redisUtil) {
		super(redisUtil);
	}
	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = RabbitConstants.VIDEO_FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT, durable = "true", autoDelete = "false"), 
			value = @Queue(value = RabbitConstants.VIDEO_FANOUT_SYNC_ES_QUEUE, durable = "true", exclusive = "false", autoDelete = "false")),
			containerFactory="rabbitListenerContainerFactory")
	public void syncElasticsearch(VideoMessage videoDecodeMessage, Channel channel, Message message) {
		handler(videoDecodeMessage, channel, message);
	}

	@Override
	protected void doHandler(RabbitMessage rabbitMessage) throws Exception {
		logger.debug("Video Sync Elasticsearch,rabbitMessage:{}",rabbitMessage);
		VideoMessage videoMessage = (VideoMessage)rabbitMessage;
		syncElasticsearchService.incrementUpdateSync(()->videoMessage.getVideoId());
	}

	@Override
	protected void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws BusinessException {
		SysMessageInsertDto sysMessageInsertDto = new SysMessageInsertDto();
		sysMessageInsertDto.setType(1L);
		sysMessageInsertDto.setUserId(1L);
		sysMessageInsertDto.setBuzId(rabbitMessage.getCorrelationDataId());
		sysMessageInsertDto.setBuzType((byte)3);
		sysMessageInsertDto.setBuzName("视频同步ES失败");
		sysMessageInsertDto.setContent("视频同步ES失败,需要人工介入处理...");
		sysMessageService.insert(sysMessageInsertDto);
	}
	@Override
	protected RabbitMessageType getRabbitMessageType() {
		return new RabbitMessageType() {
			@Override
			public byte getType() {
				return 3;
			}
			@Override
			public String getRetryKey() {
				return CachePrefixConstants.SYNC_ES_VIDEO_RETRY;
			}
		};
	}
}
