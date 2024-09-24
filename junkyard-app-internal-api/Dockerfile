FROM openjdk:17-jdk-slim
WORKDIR /build
ARG JAR_FILE=build/libs/junkyard-app-internal-api.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","app.jar"]