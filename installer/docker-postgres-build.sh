#!/usr/bin/env bash

set -e

cd $(dirname "$0")

docker build . -t medispan-installer-postgres -f Dockerfile-postgres