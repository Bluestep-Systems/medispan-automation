#!/usr/bin/env bash

set -e

cd $(dirname "$0")

./docker-build.sh ${1}

: ${PGVERSION="42.5.0"}
NOW=$(date +%Y%m%d%H%M)
export PGVERSION
docker build . --build-arg PGVERSION \
  -t caffeine01/medispan-installer:jdbc-postgres \
  -t caffeine01/medispan-installer:jdbc-postgres-${PGVERSION} \
  -t caffeine01/medispan-installer:jdbc-postgres-${NOW} \
  -f Dockerfile-postgres

[ "--push" == "${1}" ] \
    && docker push caffeine01/medispan-installer:jdbc-postgres \
    && docker push caffeine01/medispan-installer:jdbc-postgres-${PGVERSION} \
    && docker push caffeine01/medispan-installer:jdbc-postgres-${NOW}