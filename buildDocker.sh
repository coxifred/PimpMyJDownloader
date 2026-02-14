#!/bin/bash

echo "Version (2.0):"
read VERSION
if [ -z "${VERSION}" ]
then
 VERSION=2.0
fi

docker build --tag pimpmyjdownloader:$VERSION .
docker login
docker tag pimpmysuperwatt:$VERSION docker.io/coxifred/pimpmyjdownloader:$VERSION
docker push coxifred/pimpmyjdownloader:$VERSION

docker tag docker.io/coxifred/pimpmyjdownloader:${VERSION} docker.io/coxifred/pimpmyjdownloader:latest
docker push  docker.io/coxifred/pimpmyjdownloader:latest
