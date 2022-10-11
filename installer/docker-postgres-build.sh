#!/usr/bin/env bash

set -e

cd $(dirname "$0")

docker build . -t medispan-installer:jdbc-postgres -f Dockerfile-postgres