package com.reyco.dasbx.desensitize.core;

import java.io.IOException;

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
import com.reyco.dasbx.desensitize.utils.SpringContextUtils;

public class ReycoDesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean enabled;
	/**
	 * 脱敏类型，默认为DEFAULT
	 */
	private final DesensitizeType type;
	
	private final DesensitizeHandler handler;
	
	public ReycoDesensitizeSerializer() {
        // 关键：在这里直接拿 Spring 容器里的单例
        this.handler = SpringContextUtils.getBean(DesensitizeHandler.class);
        this.enabled = SpringContextUtils.getPropertyToBoolean(DesensitizeConfiguration.ENABLED);
        this.type = null;
    }
    public ReycoDesensitizeSerializer(DesensitizeHandler handler, boolean enabled, DesensitizeType type) {
        this.handler = handler;
        this.enabled = enabled;
        this.type = type;
    }
	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// 如果未开启或 type 为空，直接输出原值
        if (enabled && type != null && handler != null) {
            gen.writeString(handler.handler(type, value));
        } else {
            gen.writeString(value);
        }
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		if (property == null) {
            return prov.findValueSerializer(property.getType(), property);
        }
		Desensitize desensitize = property.getAnnotation(Desensitize.class);
		if (desensitize != null && enabled) {
			DesensitizeType resolvedType;
			try {
				final int start = desensitize.start();
				final int end = desensitize.end();
				
				if (start == Integer.MIN_VALUE && end == Integer.MAX_VALUE) {
                    Class<?> typeClass = desensitize.type();
                    resolvedType = (DesensitizeType) BeanUtils.instantiateClass(typeClass);
                } else if (start != Integer.MIN_VALUE || end != Integer.MAX_VALUE) {
                    resolvedType = new DesensitizeType.DefaultIndexDesensitizeType(start, end);
                } else {
                    resolvedType = new DesensitizeType.DefaultAllExpressionDesensitizeType();
                }
			}catch (Exception e) {
				logger.error("解析脱敏注解失败", e);
                resolvedType = new DesensitizeType.DefaultAllExpressionDesensitizeType();
			}
            return new ReycoDesensitizeSerializer(this.handler, this.enabled, resolvedType);
		}
		return this;
	}

}
