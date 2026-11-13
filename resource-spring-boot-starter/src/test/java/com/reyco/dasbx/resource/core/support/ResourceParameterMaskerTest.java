package com.reyco.dasbx.resource.core.support;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.reyco.dasbx.resource.core.support.ResourceMaskingProperties.Rule;

public class ResourceParameterMaskerTest {

	@Test
	public void masksDefaultSensitiveValues() {
		ResourceParameterMasker masker = new ResourceParameterMasker(
				new ResourceMaskingProperties());

		Map<String, Object> source = new LinkedHashMap<String, Object>();
		source.put("phone", "13800138000");
		source.put("email", "alice@example.com");
		source.put("idCard", "110101199001011234");
		source.put("password", "secret");
		source.put("tags", Arrays.asList("13800138000", "normal"));

		Map<String, Object> masked = masker.maskParameters(source);

		assertEquals("138****8000", masked.get("phone"));
		assertEquals("a***@example.com", masked.get("email"));
		assertEquals("110***********1234", masked.get("idCard"));
		assertEquals("******", masked.get("password"));

		@SuppressWarnings("unchecked")
		List<Object> tags = (List<Object>) masked.get("tags");

		assertEquals("138****8000", tags.get(0));
		assertEquals("normal", tags.get(1));
	}

	@Test
	public void masksNestedMap() {
		ResourceParameterMasker masker = new ResourceParameterMasker(
				new ResourceMaskingProperties());

		Map<String, Object> user = new LinkedHashMap<String, Object>();
		user.put("phone", "13900139000");
		user.put("email", "test@example.com");

		Map<String, Object> source = new LinkedHashMap<String, Object>();
		source.put("user", user);

		Map<String, Object> masked = masker.maskParameters(source);

		@SuppressWarnings("unchecked")
		Map<String, Object> maskedUser = (Map<String, Object>) masked.get("user");

		assertEquals("139****9000", maskedUser.get("phone"));
		assertEquals("t***@example.com", maskedUser.get("email"));
	}

	@Test
	public void masksTokenUsingConfiguredValue() {
		ResourceMaskingProperties properties = new ResourceMaskingProperties();
		properties.setTokenMask("***");

		ResourceParameterMasker masker = new ResourceParameterMasker(properties);

		assertEquals("***", masker.maskToken("123456789"));
	}

	@Test
	public void supportsCustomRule() {
		ResourceMaskingProperties properties = new ResourceMaskingProperties();
		properties.setDefaultRulesEnabled(false);

		Rule rule = new Rule();
		rule.setName("customer-code");
		rule.setRegex("(customerCode)=(\\d{4})\\d*(\\d{4})");
		rule.setReplacement("$1=$2****$3");
		properties.setRules(Arrays.asList(rule));

		ResourceParameterMasker masker = new ResourceParameterMasker(properties);

		Map<String, Object> source = new LinkedHashMap<String, Object>();
		source.put("customerCode", "1234567890");

		Map<String, Object> masked = masker.maskParameters(source);

		assertEquals("1234****7890", masked.get("customerCode"));
	}

	@Test
	public void masksPhoneInFreeText() {
		ResourceParameterMasker masker = new ResourceParameterMasker(
				new ResourceMaskingProperties());

		String masked = masker.maskText("phone=13800138000");

		assertEquals("phone=138****8000", masked);
	}
}
