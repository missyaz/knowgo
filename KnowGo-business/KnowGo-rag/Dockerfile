#FROM docker.1ms.run/maven:3.9.12-eclipse-temurin-21 AS builder
#WORKDIR /app
#
## 1. 复制Maven配置
#COPY KnowGo-business/KnowGo-rag/settings.xml /root/.m2/settings.xml
#
## 2. 复制所有POM文件（先复制POM，利用Maven缓存）
#COPY pom.xml ./pom.xml
#COPY KnowGo-business/pom.xml ./KnowGo-business/pom.xml
#COPY KnowGo-business/KnowGo-rag/pom.xml ./KnowGo-business/KnowGo-rag/pom.xml
#
## 3. 复制整个项目源代码
#COPY . ./
#
## 4. 执行Maven打包，生成target目录
#ENV MAVEN_OPTS="-Xms512m -Xmx1024m"
#RUN mvn clean package -Dmaven.test.skip=true -pl KnowGo-business/KnowGo-rag -am

# 运行阶段
FROM docker.1ms.run/eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
ENV LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]