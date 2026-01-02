# 构建阶段：替换为有效标签 maven:3.9.6-openjdk-21（支持Java 21）
FROM maven:3.9.6-openjdk-21 AS builder
WORKDIR /app
# 复制pom.xml和源码（利用Maven缓存，优先复制pom.xml）
COPY pom.xml .
# 下载依赖（仅当pom.xml变更时重新下载）
RUN mvn dependency:go-offline -B
COPY src ./src
# 打包（跳过测试，生成可执行jar）
RUN mvn clean package -DskipTests

# 运行阶段：保持不变（已为Java 21）
FROM openjdk:21-jdk-slim
WORKDIR /app
# 开启UTF-8编码，优化JVM运行参数（适配微服务）
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"
# 从构建阶段复制jar包（匹配Spring Boot打包后的产物名称）
COPY --from=builder /app/target/*.jar app.jar
# 暴露端口（与微服务配置端口对应，仅作标识）
EXPOSE 8080
# 启动命令（加载JVM参数，执行jar包）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]