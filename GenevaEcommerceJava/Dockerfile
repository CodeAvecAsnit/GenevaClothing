# Stage 1: Build the project
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files from the build context
COPY GenevaEcommerceJava/pom.xml ./
COPY GenevaEcommerceJava/src ./src

# Build the Spring Boot project, skip tests
RUN mvn clean package -DskipTests

# Copy the built jar to a standard location
RUN cp target/*.jar target/app.jar

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/app.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
