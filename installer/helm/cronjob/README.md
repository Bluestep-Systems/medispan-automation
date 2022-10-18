# Helm CronJob

A Kubernetes CronJob to automate the update of [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span) data. You will already need an account and licenses from Medi-Span for this helm chart to work.

The default behaviour is to do incremental updates weekly. 

See [values.yaml](values.yaml) to override behavior. 

## Examples
These commands are run from the /installer/helm folder.

### Convert an existing MediSpan.xml to an override file.
```bash
cat <(echo -e "conf:\n  MediSpan.xml: |"; \
      cat /etc/medispan/conf/MediSpan.xml | sed 's/^/    /') \
  > override-medispan-xml.yaml
```

### If you already have a formatted override-medispan-xml.yaml
```bash
helm upgrade --install --namespace default \
  m6n-installer ./cronjob \
  -f /myoverrides/override-medispan-xml.yaml \
  --set username=medispan-username \
  --set password=medispan-password \
  --set dataAccessId=MySqlData
  --set installs.downloadAndInstall.password=password-encrypted-and-base64
```

### Use an existing MediSpan.xml without converting it first.
This command combines the previous two into one command by using a subshell to properly format the MediSpan.xml as an override. It also uses an inline override instead of using `--set`.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /etc/medispan/conf/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
installs:
  downloadAndInstall:
    password: "somebase64string"
dataAccesId: PostgresData
EOF
```

### Do an initial full database install.
This command overrides `type` to do a `FULL_DB` instead of an `INCREMENTAL_DB` install.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /etc/medispan/conf/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
dataAccesId: PostgresData
installs:
  type: SPECIFY_DIRECTORY
  specifyDirectory:
    type: FULL_DB
EOF
```

Trigger the cronjob
```bash
echo "Manual Trigger cronjob to do a full database update"
kubectl --namespace default create job --from=cronjob/m6n-installer m6p-installer-manual-init
```

Now change the cronjob to do a Download and Install.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /etc/medispan/conf/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
dataAccesId: PostgresData
installs:
  type: DOWNLOAD_AND_INSTALL
  downloadAndInstall:
    password: "somebase64string"
EOF
```
