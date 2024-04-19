FROM node:20.12.2 as frontend

WORKDIR /frontend

#Copy the files needed to build with nod the angular appplication
COPY /frontend/package.json /frontend/package-lock.json /frontend/angular.json /frontend/tsconfig.app.json /frontend/tsconfig.json /frontend/tsconfig.spec.json /frontend/

RUN npm install && npm install -g @angular/cli

#Copy source files from the frontend folder to the container
COPY frontend/src /frontend/src

RUN ng build --configuration production

COPY frontend/dist/frontend/browser /frontend/dist/new

# Use an official Java runtime as a parent image
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory in the container to /webapp03
WORKDIR /app

ENV RUNNING_IN_DOCKER = true

COPY backend/pom.xml /app/

COPY backend/src/ /app/src

#Copy production files to resources
COPY --from=frontend /frontend/dist/new /app/src/main/resources/public/new

COPY api-docs /api-docs

RUN mvn clean install -DskipTests


FROM amazoncorretto:17

WORKDIR /app

# Add the project's jar file to the container
COPY --from=build /app/target/webapp03-0.0.1-SNAPSHOT.jar /app

# Copy the wait-for-it.sh script to /app in the docker image
RUN curl -LJO https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && chmod +x /app/wait-for-it.sh