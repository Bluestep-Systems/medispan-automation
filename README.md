# Medi-Span Automation Helpers

Free tools to automate [Medi-Span®'s](https://www.wolterskluwer.com/en/solutions/medi-span) software and data downloads and their installations. 
You will already need an account and licenses from Medi-Span for these tools to be useful.

## Downloader

Automates the download of Medi-Span software and data. See [downloader README.md](downloader/README.md).

## Installer

Automates the installation/update of Medi-Span data. Also has a helm chart that installs a [CronJob](installer/helm/cronjob/README.md) to automatically update the database. See [installer README.md](installer/README.md).

## Docker build/push
```bash
(cd downloader; ./gradlew -x test -x check jar)
export TAG=$(date +%y.%m.%d).00
docker compose build
docker compose push
```