# Medi-Span Downloader

Under the Apache License 2.0, the downloader code was modified from JavaDownloadSample, author CHale of [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span).

Used to automate the download of Medi-Span code and/or data using provided credentials.
The [installer](../installer/README.md) uses the downloader.

## Environment Variables
- `m6n_authentication_username`: Required. Medi-Span username
- `m6n_authentication_password`: Required. Medi-Span password
- `m6n_download_types`: Default `SOFTWARE`. Can be any of `SOFTWARE,INCREMENTAL_DB,FULL_DB`
- `m6n_download_latestOnly`: Default `true`.  Find the latest issue date of the select files and only download those files with that date.
- See [src/main/resources/config.properties](src/main/resources/config.properties) for other possible environmental variable you can use to override the downloader's behavior.

### Example
```bash
# Change credentials.env to use your credentials
cd ..
docker compose run m6n-downloader -it
```
If you want to keep the data and software outside of docker then map a volume like `-v /var/medispan:/var/medispan`

## License
[Apache License Version 2.0](LICENSE)
