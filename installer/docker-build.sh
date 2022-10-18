#!/usr/bin/env bash

set -e

cd $(dirname "$0")

docker build . -t caffeine01/medispan-installer:jdbc-default

[ "--push" == "${1}" ] && docker push caffeine01/medispan-installer:jdbc-default