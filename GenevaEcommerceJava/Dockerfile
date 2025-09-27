FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app
COPY GenevaEcommerceJava/pom.xml ./
COPY GenevaEcommerceJava/src ./src
RUN mvn clean package -DskipTests
RUN cp target/*.jar target/app.jar
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# Build stage
#FROM maven:3.9.3-eclipse-temurin-17 AS build
#WORKDIR /app
#
## Copy pom.xml first for better layer caching
#COPY GenevaEcommerceJava/pom.xml ./
#RUN mvn dependency:go-offline -B
#
## Copy source code
#COPY GenevaEcommerceJava/src ./src
#
## Build the application
#RUN mvn clean package -DskipTests -B
#
## Runtime stage
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
#
## Create non-root user for security
#RUN addgroup -g 1001 -S appgroup && \
#    adduser -u 1001 -S appuser -G appgroup
#
## Copy the jar file
#COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar
#
## Switch to non-root user
#USER appuser
#
## Expose port
#EXPOSE 8080
#
## Health check
#HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
#  CMD curl -f http://localhost:8080/actuator/health || exit 1
#
## Run the application
#ENTRYPOINT ["java", "-Xmx512m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]