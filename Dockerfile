FROM node:14 AS build-frontend

WORKDIR /app
COPY src/main/webapp/reactjs/package.json ./
COPY src/main/webapp/reactjs/ ./

RUN npm install && npm run build

FROM maven:3.8.1-openjdk-17 AS build-backend
WORKDIR /app

COPY pom.xml .
COPY src ./src

COPY --from=build-frontend /app/build ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=build-backend /app/target/wordle-0.0.1-SNAPSHOT.jar /app/wordle-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "wordle-0.0.1-SNAPSHOT.jar"]
