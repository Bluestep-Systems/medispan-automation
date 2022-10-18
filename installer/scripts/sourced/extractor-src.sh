pushd "${M6N_INSTALLER_DIR}" > /dev/null

echo "Removing old files if they exist."
rm -rf * &> /dev/null || true

echo "Extracting MSClinical5.1 installer files ..."
unzip -o -q "${M6N_DOWNLOAD_DIR}/*java-.net*.zip" 'MSClinical5.1/*'
echo "Extracting Java files ..."
unzip -o -q "MSClinical5.1/*.zip" 'Java/*'
if [ "TRUE" == "${M6N_EXTRACT_DOTNET}" ]; then
    echo "Extracting DOTNET files ..."
    unzip -o -q "MSClinical5.1/*.zip" 'DOTNET/*'
fi
if [ "TRUE" == "${M6N_EXTRACT_NETSTANDARD}" ]; then
    echo 'Extracting "NET Standard" files ...'
    unzip -o -q "MSClinical5.1/*.zip" 'NET Standard/*'
fi
if [ "TRUE" == "${M6N_EXTRACT_DOCUMENTATION}" ]; then
    echo "Extracting Documenation files ..."
    unzip -o -q "MSClinical5.1/*.zip" 'Documentation/*'
fi
echo "Done Extracting files."
VERSION=$(ls Java/*.jar  | tail -1 | egrep -o '[0-9]+\.[0-9]+\.[0-9]+' || "UNKNOWN")
echo "Extracted Medispan version:"
echo "${VERSION}"

popd > /dev/null