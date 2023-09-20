FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:17-jdk
WORKDIR /
COPY /src /src
COPY --from=build /target/*.jar application.jar
EXPOSE 4308
ENTRYPOINT ["java", "-jar", "application.jar"]