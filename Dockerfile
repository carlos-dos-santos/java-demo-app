FROM openjdk:8-jre-alpine

ARG VERSION
ENV APP_VERSION=$VERSION

EXPOSE 80


COPY ./target/java-app-${APP_VERSION}.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT java -jar java-app-${APP_VERSION}.jar
