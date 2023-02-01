#!/usr/bin/bash
#
mvn clean package 
#VERSION=$(mvn help:evaluate  -Dexpression=${project.version} -q  -DforceStdout 2>/dev/null)
#VERSION=$(grep -oPm1 "(?<=<version>)[^<]+" "pom.xml")
eval $(printf 'VERSION=${project.version}\n' | mvn  help:evaluate 2>/dev/null | grep 'VERSION')
docker build --build-arg VERSION=${VERSION} -t machamba/java-demo-app:${VERSION} .
