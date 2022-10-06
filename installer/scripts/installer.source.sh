mkdir -p "${M6N_DATA_DIR}"

pushd "${M6N_INSTALLER_DIR}"/Java > /dev/null
dos2unix medispan-install.sh
ln -sf "${M6N_XML_SRC}"

cat <<EOF > MediSpan.Install.User.Config.xml
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

sh medispan-install.sh "${M6N_AUTO_START}"

popd > /dev/null