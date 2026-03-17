FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src src

RUN chmod +x mvnw && ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

ENV SERVER_PORT=8083
ENV SERVER_CONTEXT_PATH=/inventory
ENV SERVER_TOMCAT_THREADS_MAX=200
ENV SERVER_TOMCAT_THREADS_MIN_SPARE=20
ENV SERVER_TOMCAT_ACCEPT_COUNT=100
ENV SHUTDOWN_TIMEOUT=20s
ENV APP_ENVIRONMENT=container

COPY --from=build /workspace/target/inventory-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl --fail --silent http://127.0.0.1:${SERVER_PORT}${SERVER_CONTEXT_PATH}/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]