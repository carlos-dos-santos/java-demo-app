#!/usr/bin/bash
#
mvn clean package 
VERSION=$(mvn help:evaluate  -Dexpression=project.version -q  -DforceStdout 2>/dev/null)
docker build --build-arg VERSION=${VERSION} -t machamba/java-demo-app:${VERSION} .
