FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY app/tripsketch.jar app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]