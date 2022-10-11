pushd "${M6N_INSTALLER_DIR}" > /dev/null

echo "Removing old files if they exist."
rm -r * &> /dev/null || true

echo "Extracting installer files ..."
unzip -o -q "${M6N_DOWNLOAD_DIR}/*java-.net*.zip"
echo "Extracting MSClinical5.1 files ..."
unzip -o -q "MSClinical5.1/*.zip"
echo "Done Extracting files."
VERSION=$(ls Java/*.jar  | tail -1 | egrep -o '[0-9]+\.[0-9]+\.[0-9]+' || "UNKNOWN")
echo "Extracted Medispan version:"
echo "${VERSION}"

popd > /dev/null