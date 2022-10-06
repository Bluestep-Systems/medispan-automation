#!/usr/bin/env bash

set -e

: ${INSTALLER_DIR="/medispan/installer"}
: ${DATA_DIR="/medispan/data"}
: ${MEDISPAN_SRC="/medispan/conf/MediSpan.xml"}


mkdir -p "${DATA_DIR}"

cd "${INSTALLER_DIR}"/Java
dos2unix medispan-install.sh
ln -sf "${MEDISPAN_SRC}"
sh medispan-install.sh "$@"





