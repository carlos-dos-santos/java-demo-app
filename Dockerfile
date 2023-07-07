FROM openjdk:17-alpine as debug
ENV JAVA_TOOL_OPTIONS='-Xdebug -agentlib:jdwp=transport=dt_socket,address=0.0.0.0:5005,server=y,suspend=n'

ARG VERSION
ENV APP_VERSION=$VERSION

EXPOSE 80

COPY ./target/java-app-${APP_VERSION}.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT java -jar java-app-${APP_VERSION}.jar


FROM openjdk:17-alpine as plain

ARG VERSION
ENV APP_VERSION=$VERSION

EXPOSE 80

COPY ./target/java-app-${APP_VERSION}.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT java -jar java-app-${APP_VERSION}.jar


