# 一.基于springcloudAlibaba架构的微服务项目
## 1.子项目介绍：

  - 公共包：
    - commons: 公共的工具类包
    - commons-model：公共的Model类
    - commons-config：公共的配置类
    - sync: 同步
      
  - 各种自定义starter包：
    - id-spring-boot-starter：全局唯一Id生成器starter
    - jwt-spring-boot-starter：Jwt生成token的starter
    - oss-spring-boot-starter：自定义实现的Oss文件上传的starter
    - constant-spring-boot-starter： 常量工具starter
    - es-spring-boot-starter： elasticsearch相关的starter
    - resource-spring-boot-starter：系统日志搜集的starter
    - trim-spring-boot-starter：请求参数去前后空格的starter
    - lock-spring-boot-starter：分布式锁的starter
    - actuator-spring-boot-starter: 监控
    - decrypt-spring-cloud-starter: 配置加密
    - desensitize-spring-boot-starter: 脱敏
    - rabbitmq-spring-boot-starter: rabbitmq
    - redis-spring-cloud-starter: redis
      
  - 相关服务：
    - common-service： 基础服务
    - portal-service： 门户服务
    - user-service： 用户服务
    - open-service：  开发平台服务
    - login-service：登录服务
    - gateway：网关服务
