# Stage 1: Build the application using Gradle
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy the Gradle wrapper and configuration files first (for caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Give permission to execute the wrapper
RUN chmod +x gradlew

# Copy the source code
COPY src src

# Build the application
RUN ./gradlew clean bootJar -x test

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Gradle puts the jar in build/libs/ instead of target/
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]