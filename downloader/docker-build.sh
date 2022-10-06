#!/usr/bin/env bash

set -e

DIR=$(dirname "$0")

pushd "${DIR}"/.. > /dev/null
./gradlew clean jar
popd > /dev/null
docker build . -t medispan-downloader
