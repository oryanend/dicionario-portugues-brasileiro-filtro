#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean install -DskipTests

ENV SPRING_PROFILES_ACTIVE=prod
#
# Package stage
#
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build target/dicionario-filtro-0.0.1-SNAPSHOT.jar /app/app.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]