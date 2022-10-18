pushd "${M6N_INSTALLER_DIR}"/Java >/dev/null

#medispan-instal.sh is in dos format, so we need to convert it to unix format
dos2unix medispan-install.sh

if [ -d "${M6N_DRIVERS_DIR}" ]; then
  echo "${M6N_DRIVERS_DIR} exists. Overriding files in lib/driver. Drivers:"
  ls -l "${M6N_DRIVERS_DIR}"
  echo "Deleting all files in lib/driver."
  rm lib/driver/* &> /dev/null || true
  echo "Modifying medispan-install.sh: Setting default_database_drivers=${M6N_DRIVERS_DIR}/*"
  sed -i "s|^default_database_drivers=.*|default_database_drivers="${M6N_DRIVERS_DIR}/*"|" medispan-install.sh
fi

ln -sf "${M6N_XML_SRC}"

if [ -f "${M6N_USER_CONFIG_XML_SRC}" ]; then
  ln -sf "${M6N_USER_CONFIG_XML_SRC}"
else
  cat <<EOF >MediSpan.Install.User.Config.xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<MediSpanConfig>
    <Install>
        <Installs>
            <SPECIFY_DIRECTORY>
                <Installer>Directory</Installer>
                <Directory_Path>${M6N_DATA_DIR}</Directory_Path>
                <Product_DataAccessId>${M6N_DATA_ACCESS_ID}</Product_DataAccessId>
            </SPECIFY_DIRECTORY>
            <DOWNLOAD_AND_INSTALL>
                <Installer>MBL.Web</Installer>
                <Directory_Path>${M6N_DATA_DIR}</Directory_Path>
                <Product_DataAccessId>${M6N_DATA_ACCESS_ID}</Product_DataAccessId>
                <WebDownload_UserId>${m6n_authentication_username}</WebDownload_UserId>
                <WebDownload_Password>${M6N_WEBDOWNLOAD_PASSWORD}</WebDownload_Password>
            </DOWNLOAD_AND_INSTALL>
            <DELETE>
                <Installer>MBL.DeleteByDataAccessId</Installer>
                <Directory_Path>${M6N_DATA_DIR}</Directory_Path>
                <Product_DataAccessId>${M6N_DATA_ACCESS_ID}</Product_DataAccessId>
            </DELETE>
        </Installs>
    </Install>
</MediSpanConfig>
EOF
fi

popd >/dev/null
