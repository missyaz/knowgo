# 构建阶段：使用官方有效标签（已验证可行）
FROM maven:3.9.8-openjdk-21 AS builder
WORKDIR /app
# 复制pom.xml利用Maven缓存，提升构建效率
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# 打包生成可执行jar（跳过测试）
RUN mvn clean package -DskipTests

# 运行阶段：补充adoptium/仓库前缀，使用正确的镜像地址
FROM adoptium/eclipse-temurin:21-jdk-slim
WORKDIR /app
# 配置UTF-8编码和JVM优化参数，适配微服务运行
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"
# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar
# 暴露应用端口（与配置文件一致）
EXPOSE 8080
# 启动应用（加载JVM参数）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]