# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY backend/src ./src

RUN mvn clean package -DskipTests


# ---- Run stage ----
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/StudyTracker-1.0-SNAPSHOT.jar app.jar

EXPOSE 7000

CMD ["sh", "-c", "java -jar app.jar"]