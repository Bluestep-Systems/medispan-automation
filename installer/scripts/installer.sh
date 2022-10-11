#!/usr/bin/env bash

set -e
cd $(dirname "$0")

source ./env-src.sh
source ./check-required-src.sh
source ./software-download-src.sh
source ./extractor-src.sh
source ./script-fixer-src.sh
source ./data-download-src.sh
source ./install-data-src.sh
