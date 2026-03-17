# Desensitize Spring Boot Starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x%20%7C%203.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

**Desensitize Spring Boot Starter** 是一款高性能、非侵入式的 Java 数据脱敏工具。它基于 Jackson 序列化机制，通过简单的注解配置，即可实现对手机号、身份证、姓名等敏感数据的自动化掩码处理。

## ✨ 核心特性

- **代码即配置 (Code as Config)**：拒绝逻辑碎片化，脱敏规则与领域模型（POJO）紧密结合，支持重构。
- **高性能架构**：内置 **二级缓存机制**（`ConcurrentHashMap`），实现从 $O(n)$ 到 $O(1)$ 的处理器查找优化。
- **极致轻量**：单例化处理器设计，彻底消除序列化过程中的对象频繁创建压力。
- **灵活扩展**：基于委派模式，支持开发者轻松自定义脱敏策略（Type）与执行引擎（Handler）。

---

## 🚀 快速上手

### 1. 引入依赖
在你的 `pom.xml` 中引入本 Starter：

```xml
<dependency>
    <groupId>com.reyco.dasbx</groupId>
    <artifactId>desensitize-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 标注脱敏注解
在 Java Bean 的字段上使用 @Desensitize，指定内置的脱敏策略：

```java
public class UserVO {

    // 手机号脱敏 (内置正则)
    @Desensitize(type = DesensitizeType.PhoneDesensitizeType.class)
    private String phone;

    // 身份证脱敏 (内置正则)
    @Desensitize(type = DesensitizeType.IdCardDesensitizeType.class)
    private String idCard;

    // 自定义下标脱敏：保留前3位，后4位
    @Desensitize(type = DesensitizeType.DefaultIndexDesensitizeType.class, start = 3, end = 4)
    private String address;
    
    // Getter & Setter...
}
```

### 3. 返回结果展示
当 Controller 返回对象时，前端收到的 JSON 将自动脱敏：

```json
{
  "phone": "138****8888",
  "idCard": "110101********0001",
  "address": "北京***街道"
}
```

---

## 🛠️ 进阶扩展
#### 如果你有特殊的业务脱敏需求，可以通过以下两步轻松扩展：

### 1. 第一步：定义脱敏类型
继承 CustomExpressionDesensitizeType：并实现正则规则：

```java
public static class MySecretType extends DesensitizeType.CustomExpressionDesensitizeType {
    @Override
    public String getExpression() {
        return "(\\w{2})\\w+(\\w{2})"; // 前2后2正则
    }
    @Override
    public String getReplaceExpression() {
        return "$1####$2"; 
    }
}
```

### 2. 定义执行器 (Handler) 并在 Spring 注册

```
@Component
public class MySecretHandler implements DesensitizeHandler {
    @Override
    public boolean support(DesensitizeType type) {
        // 关键：这里要判断 type 是不是你定义的那个标识类
        return type instanceof MySecretType;
    }

    @Override
    public String handler(DesensitizeType type, String value) {
        // 真正的脱敏逻辑写在这里
        return "******"; 
    }
}
```

### 3. 使用自定义类型

```java
@Desensitize(type = MySecretType.class)
private String secretCode;
```

---

## 🧠 设计思想与性能优化

本框架在设计上拒绝了“暴力反射”和“臃肿配置”，采用了业内领先的处理方案：

### 1. 委派执行模型 (Delegate Architecture)
通过 `DelegateDesensitizeHandler` 统一调度。它不直接处理脱敏，而是根据的 `DesensitizeType`类型，自动寻找最匹配的 `Handler` 执行.

### 2. 二级缓存机制 (Smart Cache)
为了应对高并发下的序列化压力，框架内部维护了一个 `ConcurrentHashMap`:

- **首次执行**：通过 $O(n)$ 遍历寻找支持的处理器。
- **后续执行**：直接从缓存中获取对应的 Handler 实例，实现近乎零损耗的脱敏处理。

## 3. 单例兜底策略
默认处理器采用单例 Bean 注入，避免了在高频接口返回时频繁创建垃圾对象，确保系统内存曲线平滑。

---

## ⚙️ 全局配置

在 application.yml 中，你可以轻松开启或关闭脱敏功能：

```yaml
reyco:
  dasbx:
    desensitize:
      enabled: true # 默认为 true, 设置为 false 即可在测试环境下关闭脱敏
```

---

## 📄 开源协议
本项目遵循 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 协议。

 1. **权利授予**：您可以自由地使用、修改和分发本作品及其衍生作品。

 2. **分发条件**：必须向接收者提供本许可副本，并保留原始版权和免责声明。

 3. **免责说明**：作者不提供任何明示或暗示的保证，不对任何损害承担责任。

---

 Copyright (c) 2024-2026 sihaihou. All rights reserved.
