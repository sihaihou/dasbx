package com.reyco.dasbx.common.core.rabbitmq;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.reyco.dasbx.common.core.dao.sys.SysLoginLogDao;
import com.reyco.dasbx.common.core.model.domain.sys.SysLoginLog;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLoginUpdateDto;
import com.reyco.dasbx.common.core.model.dto.sys.SysLoginLogLogoutUpdateDto;
import com.reyco.dasbx.common.core.service.sys.SysLogService;
import com.reyco.dasbx.common.core.service.sys.SysLoginLogService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.SysLoginLogLoginMessage;
import com.reyco.dasbx.model.msg.SysLoginLogLogoutMessage;

@Service
public class SysLoginLogRabbitConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(SysLoginLogRabbitConsumerService.class);
	
	@Autowired
	public SysLoginLogDao  sysLoginLogDao;
	@Autowired
	public SysLoginLogService  sysLoginLogService;
	@Autowired
	private SysLogService sysLogService;
	
	@RabbitHandler
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(value=RabbitConstants.LOG_LOGIN_QUEUE,durable="true",exclusive="false",autoDelete="false"),
			exchange=@Exchange(value=RabbitConstants.LOG_EXCHANGE,type=ExchangeTypes.DIRECT,durable="true",autoDelete="false"),
			key=RabbitConstants.LOG_LOGIN_ROUTE_KEY
			))
	public void login(SysLoginLogLoginMessage sysLoginLogLoginMessage, Channel channel, Message message) {
		try {
			SysLoginLog sysLoginLog = sysLoginLogDao.getByCode(sysLoginLogLoginMessage.getCode());
			if(sysLoginLog!=null) {
				SysLoginLogLoginUpdateDto sysLoginLogLoginUpdateDto = Convert.sourceToTarget(sysLoginLog, SysLoginLogLoginUpdateDto.class);
				sysLoginLogService.updateLogin(sysLoginLogLoginUpdateDto);
			}else {
				SysLoginLogInsertDto sysLoginLogInsertDto = Convert.sourceToTarget(sysLoginLogLoginMessage, SysLoginLogInsertDto.class);
				sysLoginLogService.insert(sysLoginLogInsertDto);
			}
			SysLogUpdateDto sysLogUpdateDto = new SysLogUpdateDto();
			sysLogUpdateDto.setCode(sysLoginLogLoginMessage.getCode());
			sysLogUpdateDto.setUserId(sysLoginLogLoginMessage.getUserId());
			sysLogService.updateByCode(sysLogUpdateDto);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费:登录】消息消费成功, {},{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),sysLoginLogLoginMessage);
		} catch (IOException e) {
			logger.error("【消息消费:登录】消息消费失败, {},{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),sysLoginLogLoginMessage);
		}
	}

	@RabbitHandler
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(value=RabbitConstants.LOG_LOGOUT_QUEUE,durable="true",exclusive="false",autoDelete="false"),
			exchange=@Exchange(value=RabbitConstants.LOG_EXCHANGE,type=ExchangeTypes.DIRECT,durable="true",autoDelete="false"),
			key=RabbitConstants.LOG_LOGOUT_ROUTE_KEY
			))
	public void logout(SysLoginLogLogoutMessage sysLoginLogLogoutMessage, Channel channel, Message message) {
		try {
			SysLoginLogLogoutUpdateDto sysLoginLogLogoutUpdateDto = Convert.sourceToTarget(sysLoginLogLogoutMessage, SysLoginLogLogoutUpdateDto.class);
			sysLoginLogService.updateLogout(sysLoginLogLogoutUpdateDto);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费:登出】消息消费成功, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysLoginLogLogoutMessage);
		} catch (IOException e) {
			logger.error("【消息消费:登出】消息消费失败, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysLoginLogLogoutMessage);
		}
	}
	
}
