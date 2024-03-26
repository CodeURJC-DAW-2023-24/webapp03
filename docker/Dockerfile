# Use an official Java runtime as a parent image
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory in the container to /webapp03
WORKDIR /app/backend

ENV RUNNING_IN_DOCKER = true

COPY backend/pom.xml /app/backend/

COPY backend/src /app/backend/src/

COPY api-docs /app/api-docs

RUN mvn clean install -DskipTests


FROM amazoncorretto:17

WORKDIR /app

# Add the project's jar file to the container
COPY --from=build /app/backend/target/webapp03-0.0.1-SNAPSHOT.jar /app

COPY backend/src /backend/src/

# Copia el script wait-for-it.sh al directorio /app en la imagen de Docker
RUN curl -LJO https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && chmod +x /app/wait-for-it.sh