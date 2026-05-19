FROM openjdk:17-jdk-slim
WORKDIR /app
COPY app/tripsketch.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]