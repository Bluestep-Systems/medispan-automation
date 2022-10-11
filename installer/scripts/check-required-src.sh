if [ ! -f "${M6N_XML_SRC}" ]; then
    echo "Medispan source file not found: ${M6N_XML_SRC}"
    exit 1
fi

if [ -z "${M6N_DATA_ACCESS_ID}" ]; then
    echo "M6N_DATA_ACCESS_ID environment variable not set.  Example M6N_DATA_ACCESS_ID=PsqlData"
    exit 1
fi