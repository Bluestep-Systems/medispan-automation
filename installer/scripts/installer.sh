#!/usr/bin/env bash

set -e
cd $(dirname "$0")

source ./sourced/env-src.sh
source ./sourced/check-required-src.sh
source ./sourced/software-download-src.sh
source ./sourced/extractor-src.sh
source ./sourced/script-fixer-src.sh
source ./sourced/data-download-src.sh
source ./sourced/install-data-src.sh
source ./sourced/post-install-hooks-src.sh