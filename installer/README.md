# Medi-Span Installer

Used to automate the installation/upgrade of datafiles from [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span) to your database.
Another use of the installer is to run as a weekly [CronJob](helm/cronjob/README.md) in Kubernetes. You will need an account and licenses from Medi-Span for this to be useful.

## Default installer will.
1. Download the latest java jar files
1. Run the medispan installer with the `DOWNLOAD_AND_INSTALL` type.
## Environment Variables
- `m6n_authentication_username`: Required. Medi-Span username
- `m6n_authentication_password`: Required. Medi-Span password
- `M6N_WEBDOWNLOAD_PASSWORD`: Required if using `DOWNLOAD_AND_INSTALL`. Medi-Span encrypted base64 password
- `M6N_DATA_ACCESS_ID`: Required. DataId in MediSpan.xml
- `M6N_DRIVERS_DIR`: If jdbc drivers exist here, then they will be used instead of the defaults.
- `M6N_INSTALLS_TYPE`: Default `DOWNLOAD_AND_INSTALL`.  Can be one of `DOWNLOAD_AND_INSTALL`, `SPECIFY_DIRECTORY`, `DELETE`.
- `M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE`: Default `INCREMENTAL_DB`. Used with `SPECIFY_DIRECTORY`. Can be used to download `FULL_DB` instead of `INCREMENTAL_DB` files or both `INCREMENTAL_DB,FULL_DB`.
- `M6N_AUTO_START`: Default `/Autostart:${M6N_INSTALLS_TYPE}`. Can be used to select a different installer or more than one.  Will also need to mount a custom file at /etc/medispan/conf/MediSpan.Install.User.Config.xml
- `M6N_POST_INSTALL_HOOKS_DIR`: Any *.sh files in this directory will run(using source) after the installer.sh finishes. Could be helpful if you want scp/ftp any of the downloaded files somewhere else.
- See [scripts/env-src.sh](scripts/env-src.sh) for other environmental variable overrides available.

## MediSpan.xml
Unless /etc/medispan/conf/MediSpan.Install.User.Config.xml exists. The following xml file will be dynamically created:
```xml
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
```

## Docker

Dependent on ghcr.io/bluestep-systems/medispan-automation:downloader docker build.


### Example
```bash
docker run -it \
  -e m6n_authentication_username="your-medispan-user-id" \
  -e m6n_authentication_password="your-medispan-password" \
  -e M6N_WEBDOWNLOAD_PASSWORD="pre-encrypted-download-and-install-base64-password" \
  -e M6N_DATA_ACCESS_ID=OracleData \
  -v /var/medispan:/var/medispan \
  -v /etc/medispan/conf/MediSpan.xml:/etc/medispan/conf/MediSpan.xml \
  ghcr.io/bluestep-systems/medispan-automation
```

#### Arguments Explained:
- `-e M6N_DATA_ACCESS_ID=OracleData`: DataId in MediSpan.xml
- `-v /var/medispan:/var/medispan`: If you want to keep the produced content local

## License
[MIT License](LICENSE)

