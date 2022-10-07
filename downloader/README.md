# Medi-Span Downloader

Java code was adapted from author CHale code of [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span).

Used to automate the download of Medi-Span code and/or data using provided credentials.
The [installer](../installer/README.md) uses the downloader.

## Environment Variables
- `m6n_authentication_username`: Required. Medi-Span username
- `m6n_authentication_password`: Required. Medi-Span password
- `m6n_download_types`: Default `SOFTWARE`. Can be any of `SOFTWARE,INCREMENTAL_DB,FULL_DB`
- `m6n_download_latestOnly`: Default `true`.  Find the latest issue date of the select files and only download those files with that date.
- See [src/main/resources/config.properties](src/main/resources/config.properties) for other possible environmental variable you can use to override the downloaders behavior.

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

## License
[Apache License Version 2.0](LICENSE)
