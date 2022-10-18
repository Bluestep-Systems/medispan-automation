#!/usr/bin/env bash

set -e

cd $(dirname "$0")

: ${PGVERSION="42.5.0"}
export PGVERSION
docker build . --build-arg PGVERSION -t caffeine01/medispan-installer:jdbc-postgres -t caffeine01/medispan-installer:jdbc-postgres-${PGVERSION} -f Dockerfile-postgres

[ "--push" == "${1}" ] \
    && docker push caffeine01/medispan-installer:jdbc-postgres \
    && docker push caffeine01/medispan-installer:jdbc-postgres-${PGVERSION}    