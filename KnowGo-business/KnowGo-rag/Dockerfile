# 构建阶段：使用阿里云 ACR 同步的 maven 镜像（替代 Docker Hub）
FROM maven:3.9.12-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段：使用阿里云 ACR 同步的 eclipse-temurin 镜像（替代 Docker Hub）
FROM eclipse-temurin:21-jdk
WORKDIR /app
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]