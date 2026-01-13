# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
COPY . .
# This line ensures the script has permission to run inside the container
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]