package com.reyco.dasbx.desensitize.core;

import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.reyco.dasbx.desensitize.DesensitizeType;
import com.reyco.dasbx.desensitize.MaskingEngine;
import com.reyco.dasbx.desensitize.annotations.Desensitize;
import com.reyco.dasbx.desensitize.resolver.DesensitizeTypeResolver;

public class DesensitizeJacksonModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public DesensitizeJacksonModule(MaskingEngine maskingEngine, DesensitizeTypeResolver typeResolver) {

		super("desensitize-jackson-module");

		setSerializerModifier(new BeanSerializerModifier() {
			@Override
			public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
					List<BeanPropertyWriter> beanProperties) {
				for (BeanPropertyWriter writer : beanProperties) {
					Desensitize desensitize = writer.getMember().getAnnotation(Desensitize.class);

					if (desensitize == null || writer.getType().getRawClass() != String.class) {
						continue;
					}

					DesensitizeType type = typeResolver.resolve(desensitize);

					writer.assignSerializer(new DesensitizeJacksonSerializer(maskingEngine, type));
				}

				return beanProperties;
			}
		});
	}
}
