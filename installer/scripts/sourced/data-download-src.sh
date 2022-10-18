if [ "SPECIFY_DIRECTORY" == "${M6N_INSTALLS_TYPE}" ]; then
    echo "Starting Data Download: ${M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE}"
    m6n_download_types="${M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE}" m6n_output_path="${M6N_DATA_DIR}" /opt/medispan/downloader.sh
fi