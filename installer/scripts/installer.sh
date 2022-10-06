#!/usr/bin/env bash

set -e

cd $(dirname "$0")

: ${M6N_DATA_DIR="/medispan/data"}
: ${M6N_DOWNLOAD_DIR=/"medispan/downloaded"}
: ${M6N_INSTALLER_DIR="/medispan/installer"}
: ${M6N_XML_SRC="/medispan/conf/MediSpan.xml"}
: ${M6N_USER_CONFIG_XML_SRC="/medispan/conf/MediSpan.Install.User.Config.xml"}
: ${M6N_AUTO_START="/Autostart:Data1"}
: ${M6N_DATA_DOWNLOAD_TYPE="INCREMENTAL_DB"}

if [ ! -f "${M6N_XML_SRC}" ]; then
    echo "Medispan source file not found: ${M6N_XML_SRC}"
    exit 1
fi

if [ -z "${M6N_DATA_ACCESS_ID}" ]; then
    echo "M6N_DATA_ACCESS_ID environment variable not set.  Example M6N_DATA_ACCESS_ID=PsqlData"
    exit 1
fi

/medispan/downloader.sh
m6n_download_types="${M6N_DATA_DOWNLOAD_TYPE}" m6n_output_path="${M6N_DATA_DIR}" /medispan/downloader.sh

source ./extractor.source.sh
source ./installer.source.sh
