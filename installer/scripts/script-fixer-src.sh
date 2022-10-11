pushd "${M6N_INSTALLER_DIR}"/Java >/dev/null

#medispan-instal.sh is in dos format, so we need to convert it to unix format
dos2unix medispan-install.sh

if [ "$(ls -A "${M6N_DRIVERS_DIR}")" ]; then
  echo "Files exist in ${M6N_DRIVERS_DIR}. Overriding files in lib/driver."
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
            <Data1>
                <Installer>Directory</Installer>
                <Directory_Path>${M6N_DATA_DIR}</Directory_Path>
                <Product_DataAccessId>${M6N_DATA_ACCESS_ID}</Product_DataAccessId>
            </Data1>
        </Installs>
    </Install>
</MediSpanConfig>
EOF
fi

popd >/dev/null
