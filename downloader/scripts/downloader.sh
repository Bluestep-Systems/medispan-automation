#!/usr/bin/env bash

if [ -z "${m6n_authentication_username}" ]; then
    echo "Please set m6n_authentication_username environment variable"
    exit 1
fi
if [ -z "${m6n_authentication_password}" ]; then
    echo "Please set m6n_authentication_password environment variable"
    exit 1
fi

java -classpath /opt/medispan/m6n-downloader.jar net.bluestep.medispan.downloader.App
