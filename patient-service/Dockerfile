# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
#RUN mvn clean package -DskipTests
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package

# Runtime stage
FROM eclipse-temurin:21-jre
#FROM open-jdk:21 AS runner
WORKDIR /app
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 4000
#"-Dspring.profiles.active=docker",
ENTRYPOINT ["java", "-jar", "app.jar"]

