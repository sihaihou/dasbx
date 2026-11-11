package com.reyco.dasbx.desensitize.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.reyco.dasbx.desensitize.DesensitizeType;
import com.reyco.dasbx.desensitize.MaskingEngine;

public class DesensitizeJacksonSerializer extends JsonSerializer<Object> {

	private final MaskingEngine maskingEngine;
	private final DesensitizeType type;

	public DesensitizeJacksonSerializer(MaskingEngine maskingEngine, DesensitizeType type) {
		this.maskingEngine = maskingEngine;
		this.type = type;
	}

	@Override
	public void serialize(Object value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		if (value == null) {
			generator.writeNull();
			return;
		}
		generator.writeString(maskingEngine.mask(type, value.toString()));
	}

}
