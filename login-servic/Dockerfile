# 基于官方OpenJDK镜像
FROM openjdk:8u102-jdk
 
# 指定维护者信息
LABEL maintainer="18307200213@163.com"

# 暴露端口
EXPOSE 8001

#拷贝到工作目录
COPY ./target/login-service-0.0.1-SNAPSHOT.jar app.jar
 
# 执行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]
