FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src
RUN chmod +x mvnw && ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S portal && adduser -S portal -G portal
COPY --from=build /app/target/*.jar app.jar
USER portal
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
