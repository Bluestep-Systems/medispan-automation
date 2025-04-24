#!/usr/bin/env bash

set -e

cd $(dirname "$0")

docker build . -t ghcr.io/bluestep-systems/medispan-automation:jdbc-default

[ "--push" == "${1}" ] && docker push ghcr.io/bluestep-systems/medispan-automation:jdbc-default