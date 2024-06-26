package com.reyco.dasbx.config.resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.commons.utils.DictionaryOrderUtils;
import com.reyco.dasbx.config.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.log.core.MethodType;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.SysLogRabbitmqMessage;
import com.reyco.dasbx.resource.core.handler.ResourceHandler;
import com.reyco.dasbx.resource.core.model.ResourceDefinition;

@Component
public class DasbxResourceHandler implements ResourceHandler{
	
	@Autowired
	private RabbitProducrService rabbitProducrService;
	
	@Autowired 
	private IdGenerator<Long> idGenerator;
	
	@Override
	public void handler(ResourceDefinition reycoResource) {
		String token = reycoResource.getToken();
		if(StringUtils.isBlank(token)) {
			token = idGenerator.getGeneratorId().toString();
		}
		SysLogRabbitmqMessage rabbitMessage = new SysLogRabbitmqMessage();
		rabbitMessage.setCorrelationDataId(token);
		rabbitMessage.setCode(Long.parseLong(token));
		rabbitMessage.setService(reycoResource.getApplication());
		rabbitMessage.setType(getType(reycoResource.getMethod()));
		rabbitMessage.setName(reycoResource.getDescription());
		rabbitMessage.setPath(reycoResource.getResource());
		rabbitMessage.setMethod(reycoResource.getMethod());
		rabbitMessage.setParams(DictionaryOrderUtils.lowestDictionaryOrderString(reycoResource.getParameters()));
		rabbitMessage.setTimes(reycoResource.getEndTime()-reycoResource.getStartTime());
		rabbitMessage.setGmtRequest(reycoResource.getStartTime());
		rabbitMessage.setGmtResponse(reycoResource.getEndTime());
		rabbitMessage.setSuccess(reycoResource.isSuccess()?(byte)1:(byte)0);
		rabbitProducrService.send(RabbitConstants.LOG_EXCHANGE, RabbitConstants.LOG_SYS_ROUTE_KEY,rabbitMessage);
	}
	/**
	 * 0未知 1查看 2创建 3编辑 4删除
	 * 
	 * @param method
	 * @return
	 */
	private Byte getType(String method) {
		Byte type = 0;
		if (MethodType.GET.getMethod().equals(method) || MethodType.HEAD.getMethod().equals(method)
				|| MethodType.OPTIONS.getMethod().equals(method)) {
			type = 1;
		} else if (MethodType.POST.getMethod().equals(method)) {
			type = 2;
		} else if (MethodType.PUT.getMethod().equals(method) || MethodType.PATCH.getMethod().equals(method)) {
			type = 3;
		} else if (MethodType.DELETE.getMethod().equals(method)) {
			type = 4;
		}
		return type;
	}
}
