package com.reyco.dasbx.resource.core.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

public class ResourceMaskingPropertiesTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(TestConfiguration.class);

	@Test
	public void bindsResourceMaskingProperties() {
		contextRunner
				.withPropertyValues(
						"reyco.dasbx.resource.masking.default-rules-enabled=false",
						"reyco.dasbx.resource.masking.token-mask=***",
						"reyco.dasbx.resource.masking.rules[0].name=custom",
						"reyco.dasbx.resource.masking.rules[0].regex=(.+)=(.+)",
						"reyco.dasbx.resource.masking.rules[0].replacement=$1=***",
						"reyco.dasbx.resource.masking.rules[0].enabled=true")
				.run(context -> {
					assertThat(context).hasNotFailed();

					ResourceMaskingProperties properties =
							context.getBean(ResourceMaskingProperties.class);

					assertThat(properties.isDefaultRulesEnabled()).isFalse();
					assertThat(properties.getTokenMask()).isEqualTo("***");
					assertThat(properties.getRules()).hasSize(1);
					assertThat(properties.getRules().get(0).getName()).isEqualTo("custom");
					assertThat(properties.getRules().get(0).isEnabled()).isTrue();
				});
	}

	@Configuration
	@EnableConfigurationProperties(ResourceMaskingProperties.class)
	static class TestConfiguration {
	}
}
