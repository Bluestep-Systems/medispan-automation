[ "TRUE" == "${M6N_DOWNLOAD_DIR_CLEAN}" ] && rm -rf "${M6N_DOWNLOAD_DIR}"/* &> /dev/null || true
echo "Starting Software Download to ${M6N_DOWNLOAD_DIR}"
/opt/medispan/downloader.sh