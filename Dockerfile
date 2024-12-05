FROM openjdk:17-jdk-alpine

RUN apk add --no-cache tzdata
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
COPY ./build/libs/ijuju-*.jar ./ijuju-be.jar
ENTRYPOINT ["java", "-jar", "ijuju-be.jar"]
EXPOSE 8080