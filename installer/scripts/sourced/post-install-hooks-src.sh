if [ -d "${M6N_POST_INSTALL_HOOKS_DIR" ]; then
    echo "Sourcing post-install hooks ..."
    for hook in "${M6N_POST_INSTALL_HOOKS_DIR}"/*.sh; do
        echo "Running post-install hook: ${hook}"
        source "${hook}"
    done
fi