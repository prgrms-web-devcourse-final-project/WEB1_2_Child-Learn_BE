FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./build/libs/ijuju-*.jar ./ijuju-be.jar

ENTRYPOINT ["java", "-jar", "ijuju-be.jar"]

EXPOSE 8080