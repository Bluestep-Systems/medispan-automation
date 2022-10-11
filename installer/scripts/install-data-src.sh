pushd "${M6N_INSTALLER_DIR}"/Java > /dev/null

sleep ${M6N_INSTALL_DEBUG_SLEEP}

sh medispan-install.sh "${M6N_AUTO_START}"

popd > /dev/null