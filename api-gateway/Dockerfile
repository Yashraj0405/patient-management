# ============================================
# BUILD STAGE - Compiles the application
# ============================================

# Use Maven 3.9.9 with Java 21 (Eclipse Temurin) as the base image for building
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container to /app
WORKDIR /app

# Copy only the pom.xml file from the host machine to the container
# This is done separately to leverage Docker's layer caching
COPY pom.xml .

# (COMMENTED OUT) This would compile and package the project, skipping tests
# RUN mvn clean package -DskipTests

# Download all Maven dependencies specified in pom.xml
# The -B flag runs in batch mode (non-interactive)
# This is done before copying the source code to cache dependencies
RUN mvn dependency:go-offline -B

# Copy the entire src directory from the host machine to the container
# This contains all your Java source code and resources
COPY src ./src

# Clean any previous builds and compile the entire project
# Produces the JAR file at /app/target/patient-service-0.0.1-SNAPSHOT.jar
RUN mvn clean package

# ============================================
# RUNTIME STAGE - Runs the application
# ============================================

# Use only the Java Runtime Environment (JRE) 21 as the base image
# This is much smaller than the full Maven image, reducing final container size
FROM eclipse-temurin:21-jre

# (COMMENTED OUT) Alternative base image that could be used instead
# FROM open-jdk:21 AS runner

# Set the working directory inside the container to /app
WORKDIR /app

# Copy the compiled JAR file from the builder stage to this runtime stage
# Source: /app/target/patient-service-0.0.1-SNAPSHOT.jar (from builder)
# Destination: ./app.jar (in runtime container at /app/app.jar)
COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar ./app.jar

# Expose port 4000 to allow communication with the application
# (This is documentation; actual port binding happens with -p flag when running)
EXPOSE 4004

# (COMMENTED OUT) Spring profile that could be activated for Docker environment
# This would use application-docker.properties configuration
# "-Dspring.profiles.active=docker",

# Define the command to run when the container starts
# Launches the Java application with the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]