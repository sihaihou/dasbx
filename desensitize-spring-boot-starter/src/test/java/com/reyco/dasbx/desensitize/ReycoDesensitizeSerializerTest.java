package com.reyco.dasbx.desensitize;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reyco.dasbx.desensitize.annotations.Desensitize;
import com.reyco.dasbx.desensitize.autoConfiguration.DesensitizeConfiguration;
import com.reyco.dasbx.desensitize.handler.DesensitizeHandler;

public class ReycoDesensitizeSerializerTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    DesensitizeConfiguration.class));

    @Test
    public void autoConfigurationsAreRegistered() {
        List<String> autoConfigurations = SpringFactoriesLoader.loadFactoryNames(
                EnableAutoConfiguration.class,
                getClass().getClassLoader());

        assertThat(autoConfigurations)
                .contains(DesensitizeConfiguration.class.getName());
    }

    @Test
    public void desensitizesByDefault() {
        assertJson(json -> assertThat(json)
                .contains("\"phone\":\"138****8000\"")
                .contains("\"name\":\"张*\"")
                .contains("\"account\":\"123***7890\""));
    }

    @Test
    public void passesThroughWhenDisabled() {
        assertJson(json -> assertThat(json)
                        .contains("\"phone\":\"13800138000\"")
                        .contains("\"name\":\"张三\"")
                        .contains("\"account\":\"1234567890\""),
                "reyco.dasbx.desensitize.enabled=false");
    }

    @Test
    public void usesCustomHandler() {
        contextRunner
                .withUserConfiguration(CustomHandlerConfiguration.class)
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    String json = objectMapper(context).writeValueAsString(new SecretUser());
                    assertThat(json).contains("\"secret\":\"CUSTOM\"");
                });
    }

    private void assertJson(JsonAssertion assertion, String... properties) {
        contextRunner
                .withPropertyValues(properties)
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertion.accept(objectMapper(context).writeValueAsString(new User()));
                });
    }

    private ObjectMapper objectMapper(ApplicationContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (Module module : context.getBeansOfType(Module.class).values()) {
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

    @FunctionalInterface
    private interface JsonAssertion {
        void accept(String json) throws Exception;
    }

    static class User {
        @Desensitize(type = DesensitizeType.PhoneDesensitizeType.class)
        private final String phone = "13800138000";

        @Desensitize(type = DesensitizeType.NameDesensitizeType.class)
        private final String name = "张三";

        @Desensitize(type = DesensitizeType.DefaultIndexDesensitizeType.class, start = 3, end = 4)
        private final String account = "1234567890";

        public String getPhone() {
            return phone;
        }

        public String getName() {
            return name;
        }

        public String getAccount() {
            return account;
        }
    }

    static class SecretType implements DesensitizeType {
    }

    static class SecretUser {
        @Desensitize(type = SecretType.class)
        private final String secret = "original";

        public String getSecret() {
            return secret;
        }
    }

    @Configuration
    static class CustomHandlerConfiguration {
        @Bean
        DesensitizeHandler customHandler() {
            return new DesensitizeHandler() {
                @Override
                public Boolean support(DesensitizeType type) {
                    return type instanceof SecretType;
                }

                @Override
                public String handler(DesensitizeType type, String value) {
                    return "CUSTOM";
                }
            };
        }
    }
}
