# 构建阶段：使用阿里云 ACR 公共 Maven 镜像（国内访问稳定）
FROM docker.1ms.run/maven:3.9.12-eclipse-temurin-21 AS builder
WORKDIR /app

# 关键：配置阿里云 Maven 镜像源（解决依赖下载慢/失败问题）
COPY settings.xml /root/.m2/settings.xml

# 先复制父 POM 和 RAG 模块 POM（利用 Maven 缓存，优先下载依赖）
COPY ../../pom.xml ./pom.xml
COPY ../pom.xml ./KnowGo-business/pom.xml
COPY ./pom.xml ./KnowGo-business/KnowGo-rag/pom.xml

# 移除冗余的 mvn dependency:go-offline，直接打包（依赖全工程已安装）
COPY ../../ ./

# 配置 Maven JVM 内存，避免 OOM
ENV MAVEN_OPTS="-Xms512m -Xmx1024m"

# 仅打包 RAG 模块（依托全工程依赖已安装到本地仓库）
RUN mvn clean package -DskipTests -pl KnowGo-business/KnowGo-rag -am

# 运行阶段：使用阿里云 ACR 公共 JDK 镜像
FROM docker.1ms.run/eclipse-temurin:21-jdk
WORKDIR /app

# 从构建阶段复制 RAG 模块的 JAR 包
COPY --from=builder /app/KnowGo-business/KnowGo-rag/target/*.jar app.jar

# 配置运行环境
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"

# 暴露 RAG 模块端口
EXPOSE 8081

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]