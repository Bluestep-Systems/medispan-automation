#!/usr/bin/env bash

set -e

DOWNLOAD_DIR=${1:-/medispan/downloaded}
: ${INSTALLER_DIR="/medispan/installer"}

mkdir -p "${INSTALLER_DIR}"
cd "${INSTALLER_DIR}"

echo "Removing old files if they exist: "
rm -r * &> /dev/null || true

unzip -o "${DOWNLOAD_DIR}/*java-.net*.zip"
unzip -o "MSClinical5.1/*.zip"
VERSION=`ls Java/*.jar  | tail -1 | egrep -o '[0-9]+\.[0-9]+\.[0-9]+'`
echo "Extracted Medispan version:"
echo "${VERSION}"





