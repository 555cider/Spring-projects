# STAGE 1. Build the project
FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN ["gradle", "clean", "build"]

# STAGE 2. Copy the project
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
