# syntax=docker/dockerfile:1.6

FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline
COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:24-jre AS runtime
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
