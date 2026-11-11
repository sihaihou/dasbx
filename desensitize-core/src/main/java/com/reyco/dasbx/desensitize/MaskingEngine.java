package com.reyco.dasbx.desensitize;

import java.util.Objects;

import com.reyco.dasbx.desensitize.annotations.Desensitize;
import com.reyco.dasbx.desensitize.handler.DesensitizeHandler;
import com.reyco.dasbx.desensitize.resolver.DesensitizeTypeResolver;

/**
 * 与输出协议无关的脱敏执行入口。
 */
public class MaskingEngine {

	private final DesensitizeHandler handler;
	private final DesensitizeTypeResolver typeResolver;

	public MaskingEngine(DesensitizeHandler handler) {
		this(handler, new DesensitizeTypeResolver());
	}

	public MaskingEngine(DesensitizeHandler handler, DesensitizeTypeResolver typeResolver) {
		this.handler = Objects.requireNonNull(handler, "handler must not be null");
		this.typeResolver = Objects.requireNonNull(typeResolver, "typeResolver must not be null");
	}

	public String mask(Desensitize desensitize, String value) {
		return mask(typeResolver.resolve(desensitize), value);
	}

	public String mask(DesensitizeType type, String value) {
		if (value == null) {
			return null;
		}
		return handler.handler(type, value);
	}

}
