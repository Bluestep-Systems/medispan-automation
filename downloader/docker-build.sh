#!/usr/bin/env bash

set -e

DIR=$(dirname "$0")

pushd "${DIR}"/.. > /dev/null
./gradlew clean jar
popd > /dev/null
docker build . -t caffeine01/medispan-downloader

[ "--push" == "${1}" ] && docker push caffeine01/medispan-downloader
