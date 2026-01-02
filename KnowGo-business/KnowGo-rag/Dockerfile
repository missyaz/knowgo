# 直接使用 JDK 运行镜像，无需构建阶段
FROM docker.1ms.run/eclipse-temurin:21-jdk
WORKDIR /app

# 复制流水线 1 打包生成的 RAG 模块 JAR 包
COPY KnowGo-business/KnowGo-rag/target/*.jar app.jar

# 配置运行环境
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]