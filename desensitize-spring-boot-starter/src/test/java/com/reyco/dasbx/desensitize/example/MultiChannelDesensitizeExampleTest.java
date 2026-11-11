package com.reyco.dasbx.desensitize.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reyco.dasbx.desensitize.DesensitizeType;
import com.reyco.dasbx.desensitize.annotations.Desensitize;
import com.reyco.dasbx.desensitize.autoConfiguration.DesensitizeConfiguration;

/**
 * 多输出通道脱敏示例。
 *
 * <p>规则只声明一次，Jackson、日志、RPC 和数据库读模型共用同一个执行引擎。</p>
 */
public class MultiChannelDesensitizeExampleTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    DesensitizeConfiguration.class));

    @Test
    public void demonstratesMultipleOutputChannels() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            UserEntity entity = new UserEntity(
                    "10001",
                    "张三",
                    "13800138000",
                    "110101199001011234",
                    "6222021234560001",
                    "北京市朝阳区建国路88号");
            UserView userView = UserView.from(entity);
            ObjectMapper objectMapper = objectMapper(context);

            String httpJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(userView);

            ObjectNode maskedUser = objectMapper.valueToTree(userView);
            ObjectNode logRecord = objectMapper.createObjectNode();
            logRecord.put("event", "user_query");
            logRecord.put("channel", "LOG");
            logRecord.setAll(maskedUser);

            ObjectNode rpcResponse = objectMapper.createObjectNode();
            rpcResponse.put("code", 0);
            rpcResponse.put("channel", "RPC");
            rpcResponse.set("data", maskedUser);

            ObjectNode databaseView = objectMapper.valueToTree(userView);
            databaseView.put("channel", "DATABASE_READ");

            assertThat(maskedUser.path("phone").asText()).isEqualTo("138****8000");
            assertThat(maskedUser.path("idCard").asText()).isEqualTo("110***********1234");
            assertThat(maskedUser.path("bankCard").asText()).isEqualTo("622*********0001");
            assertThat(maskedUser.path("name").asText()).isEqualTo("张*");
            assertThat(databaseView.path("channel").asText()).isEqualTo("DATABASE_READ");

            // 原实体没有被修改，数据库内部业务逻辑仍然可以读取明文。
            assertThat(entity.getPhone()).isEqualTo("13800138000");
            assertThat(entity.getIdCard()).isEqualTo("110101199001011234");

            System.out.println("========== HTTP JSON ==========");
            System.out.println(httpJson);
            System.out.println("========== STRUCTURED LOG ==========");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logRecord));
            System.out.println("========== RPC RESPONSE ==========");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rpcResponse));
            System.out.println("========== DATABASE READ VIEW ==========");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(databaseView));
        });
    }

    private ObjectMapper objectMapper(ApplicationContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (Module module : context.getBeansOfType(Module.class).values()) {
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

    /**
     * 数据库实体保持明文，不直接挂输出脱敏规则。
     */
    private static final class UserEntity {
        private final String id;
        private final String name;
        private final String phone;
        private final String idCard;
        private final String bankCard;
        private final String address;

        private UserEntity(String id, String name, String phone, String idCard,
                String bankCard, String address) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.idCard = idCard;
            this.bankCard = bankCard;
            this.address = address;
        }

        private String getId() {
            return id;
        }

        private String getName() {
            return name;
        }

        private String getPhone() {
            return phone;
        }

        private String getIdCard() {
            return idCard;
        }

        private String getBankCard() {
            return bankCard;
        }

        private String getAddress() {
            return address;
        }
    }

    /**
     * 对外读模型：规则只声明一次，各出口按需复用。
     */
    private static final class UserView {
        @Desensitize(type = DesensitizeType.NameDesensitizeType.class)
        private final String name;

        @Desensitize(type = DesensitizeType.PhoneDesensitizeType.class)
        private final String phone;

        @Desensitize(type = DesensitizeType.IdCardDesensitizeType.class)
        private final String idCard;

        @Desensitize(type = DesensitizeType.DefaultIndexDesensitizeType.class, start = 3, end = 4)
        private final String bankCard;

        @Desensitize(type = DesensitizeType.DefaultIndexDesensitizeType.class, start = 3, end = 4)
        private final String address;

        private UserView(String name, String phone, String idCard, String bankCard, String address) {
            this.name = name;
            this.phone = phone;
            this.idCard = idCard;
            this.bankCard = bankCard;
            this.address = address;
        }

        private static UserView from(UserEntity entity) {
            return new UserView(
                    entity.getName(),
                    entity.getPhone(),
                    entity.getIdCard(),
                    entity.getBankCard(),
                    entity.getAddress());
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getIdCard() {
            return idCard;
        }

        public String getBankCard() {
            return bankCard;
        }

        public String getAddress() {
            return address;
        }
    }
}
