# STAGE 1: Build the application (Keep this as is)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run the application
# CHANGE 1: Use 'jdk' instead of 'jre' to support the instrumentation agent
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the built .jar file
COPY --from=build /app/target/*.jar app.jar

# CHANGE 2: Copy the agent you downloaded into the container
# Ensure 'agent.jar' is in your local /backend folder
# COPY agent.jar agent.jar

# Expose the port
EXPOSE 8082

# CHANGE 3: Update the Entrypoint to load the agent BEFORE the app starts
ENTRYPOINT ["java", "-jar", "app.jar"]