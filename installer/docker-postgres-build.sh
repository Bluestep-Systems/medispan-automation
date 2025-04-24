#!/usr/bin/env bash

set -e

cd $(dirname "$0")

./docker-build.sh ${1}

: ${PGVERSION="42.5.0"}
NOW=$(date +%Y%m%d%H%M)
export PGVERSION
docker build . --build-arg PGVERSION \
  -t ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres \
  -t ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres-${PGVERSION} \
  -t ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres-${NOW} \
  -f Dockerfile-postgres

[ "--push" == "${1}" ] \
    && docker push ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres \
    && docker push ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres-${PGVERSION} \
    && docker push ghcr.io/bluestep-systems/medispan-automation:jdbc-postgres-${NOW}