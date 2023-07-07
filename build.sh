#!/usr/bin/bash
#
TARGET_LAYER=${1:-""}

mvn package 
#VERSION=$(mvn help:evaluate  -Dexpression=${project.version} -q  -DforceStdout 2>/dev/null)
#VERSION=$(grep -oPm1 "(?<=<version>)[^<]+" "pom.xml")
eval $(printf 'VERSION=${project.version}\n' | mvn  help:evaluate 2>/dev/null | grep 'VERSION')

echo $VERSION
#docker build --build-arg VERSION=${VERSION} -t test/java-demo-app:${VERSION} --no-cache --target debug -f Dockerfile.stage .
echo docker build --build-arg VERSION=${VERSION} -t kubernetes.docker.internal/java-demo-app:${VERSION} ${TARGET_LAYER} -f Dockerfile.stage .

docker build --build-arg VERSION=${VERSION} \
    -t kubernetes.docker.internal/java-demo-app:${VERSION} \
    --progress plain \
    --target ${TARGET_LAYER} \
    --file Dockerfile.stage .
#docker push machamba/java-demo-app:${VERSION} 
