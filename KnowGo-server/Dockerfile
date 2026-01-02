# 构建阶段
FROM maven:3.8.8-openjdk-21 AS builder
WORKDIR /app
# 复制pom.xml和源码
COPY pom.xml .
COPY src ./src
# 打包（跳过测试）
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:21-jdk-slim
WORKDIR /app
# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar
# 暴露端口（实际端口由应用配置文件决定，此处仅为标识）
EXPOSE 8080
# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]