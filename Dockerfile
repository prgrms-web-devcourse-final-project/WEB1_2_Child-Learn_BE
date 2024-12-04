FROM openjdk:17-jdk-alpine
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=Asia/Seoul
COPY ./build/libs/ijuju-*.jar ./ijuju-be.jar
ENTRYPOINT ["java", "-jar", "ijuju-be.jar"]
EXPOSE 8080