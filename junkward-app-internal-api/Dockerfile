FROM openjdk:17-jdk-slim
WORKDIR /build
ARG JAR_FILE=build/libs/junkward-app-internal-api.jar
ARG REST_DOCS_FILE=build/docs/asciidoc/index.html
COPY ${JAR_FILE} app.jar
RUN mkdir -p docs
COPY ${REST_DOCS_FILE} docs
ENTRYPOINT ["java", "-jar","app.jar"]