# STAGE 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies first (caches them for faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the actual source code and build it
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built .jar file from the 'build' stage above
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]