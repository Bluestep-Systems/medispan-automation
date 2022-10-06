# medispan-downloader

Java code was adapted from author CHale.

Used to download Medispan code and/or data using provided credentials.
The installer uses the downloader.

Enviroment Variables
- `m6n_authentication_username`: Required. Medispan username
- `m6n_authentication_password`: Required. Medispan password
- `m6n_download_types`: Default `SOFTWARE`. Can be any of `SOFTWARE,INCREMENTAL_DB,FULL_DB`
- `m6n_download_latestOnly`: Default `true`.  Find the latest issue date of the select files and only download those files with that date.
- See **downloader/src/main/resources/config.properties** for other environmental variable overrides available.

## Docker
### Create
```bash
downloader/docker-build.sh
```
### Example
```bash
docker run -it -e m6n_authentication_username="username" -e m6n_authentication_password="password" medispan-downloader
```
If you want to keep the data and software outside of docker then map a volume like `-v /medispan/downloaded:/medispan/downloaded`

# medispan-installer

Used to install/upgrade datafiles to your database of choosing.
Another use of the installer is to run as a monthly CronJob in Kubernetes.

## Default installer will.
1. Download the latest java jar files
2. Download the latest incremental db files.
3. Run the medispan installer
## Enviroment Variables
- `m6n_authentication_username`: Required. Medispan username
- `m6n_authentication_password`: Required. Medispan password
- `M6N_DATA_ACCESS_ID`: Required. DataId in MediSpan.xml
- `M6N_DATA_DOWNLOAD_TYPE`: Default `INCREMENTAL_DB`. Can be used to download `FULL_DB` instead of `INCREMENTAL_DB` files or both `INCREMENTAL_DB,FULL_DB`.
- `M6N_AUTO_START`: Default `/Autostart:Data1`. Can be used to select a different installer or more than one.  Will also need to mount a custom file at /medispan/conf/MediSpan.Install.User.Config.xml
- See **installer/scripts/installer.sh** for other environmental variable overrides available.

## Docker

Dependent on medispan-downloader docker build.

### Create
```bash
installer/docker-build.sh
```

### Example
```bash
docker run -it \
  -e m6n_authentication_username="your-medispan-user-id" \
  -e m6n_authentication_password="your-medispan-password" \
  -e M6N_DATA_ACCESS_ID=OracleData \
  -v /medispan/downloaded:/medispan/downloaded \
  -v /medispan/data:/medispan/data  \ 
  -v /medispan/conf/MediSpan.xml:/medispan/conf/MediSpan.xml \
  --network=host \
  medispan-installer
```

#### Arguments Explained:
- `-e M6N_DATA_ACCESS_ID=OracleData`: DataId in MediSpan.xml
- `-v /medispan/downloaded:/medispan/downloaded`: If you want to keep software local
- `-v /medispan/data:/medispan/data`: If you want to keep data local
- `--network=host`: If you want to access the host network to find the database