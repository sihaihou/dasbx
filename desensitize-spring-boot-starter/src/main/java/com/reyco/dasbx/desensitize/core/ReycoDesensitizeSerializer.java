package com.reyco.dasbx.desensitize.core;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.reyco.dasbx.desensitize.annotations.Desensitize;
import com.reyco.dasbx.desensitize.autoConfiguration.DesensitizeConfiguration;
import com.reyco.dasbx.desensitize.core.handler.DesensitizeHandler;
import com.reyco.dasbx.desensitize.core.manager.DefaultDesensitizeHandlerManager;
import com.reyco.dasbx.desensitize.core.manager.DesensitizeHandlerManager;
import com.reyco.dasbx.desensitize.utils.SpringContextUtils;

public class ReycoDesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean enabled;
	/**
	 * 脱敏类型，默认为DEFAULT
	 */
	private DesensitizeType type;
	
	private DesensitizeHandlerManager desensitizeHandlerManager;
	
	public ReycoDesensitizeSerializer() {
		this(new DefaultDesensitizeHandlerManager());
	}
	public ReycoDesensitizeSerializer(DesensitizeHandlerManager desensitizeHandlerManager) {
		super();
		init(desensitizeHandlerManager);
	}
	private void init(DesensitizeHandlerManager desensitizeHandlerManager){
		this.desensitizeHandlerManager = desensitizeHandlerManager;
		this.enabled = SpringContextUtils.getPropertyToBoolean(DesensitizeConfiguration.ENABLED);
	}
	public DesensitizeHandlerManager getDesensitizeHandlerManager() {
		return desensitizeHandlerManager;
	}
	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (enabled) {
			DesensitizeHandlerManager desensitizeHandlerManager = getDesensitizeHandlerManager();
			DesensitizeHandler desensiteHandler = desensitizeHandlerManager.getDesensitizeHandler();
			logger.debug("desensiteHandler:{}",desensiteHandler);
			gen.writeString(desensiteHandler.handler(type, value));
		}else {
			gen.writeString(value);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		if (enabled) {
			Desensitize desensitize = property.getAnnotation(Desensitize.class);
			if (desensitize != null) {
				try {
					final int start = desensitize.start();
					final int end = desensitize.end();
					Class<?> desensitizeType;
					if(start==Integer.MIN_VALUE && end==Integer.MAX_VALUE && (desensitizeType=desensitize.type())!=null) {
						Constructor<?> constructorToUse = desensitizeType.getDeclaredConstructor();
						this.type = (DesensitizeType) BeanUtils.instantiateClass(constructorToUse);
					}else if(start!=Integer.MIN_VALUE || end!=Integer.MAX_VALUE) {
						this.type = new DesensitizeType.DefaultIndexDesensitizeType(start,end);
					}else {
						this.type = new DesensitizeType.DefaultAllExpressionDesensitizeType();
					}
				}catch (Exception e) {
					e.printStackTrace();
					this.type = new DesensitizeType.DefaultAllExpressionDesensitizeType();
				}
			}
		}
		return this;
	}

}
