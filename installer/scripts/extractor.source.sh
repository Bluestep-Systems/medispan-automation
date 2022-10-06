mkdir -p "${M6N_INSTALLER_DIR}"
pushd "${M6N_INSTALLER_DIR}" > /dev/null

echo "Removing old files if they exist: "
rm -r * &> /dev/null || true

unzip -o "${M6N_DOWNLOAD_DIR}/*java-.net*.zip"
unzip -o "MSClinical5.1/*.zip"
VERSION=`ls Java/*.jar  | tail -1 | egrep -o '[0-9]+\.[0-9]+\.[0-9]+'`
echo "Extracted Medispan version:"
echo "${VERSION}"

popd > /dev/null