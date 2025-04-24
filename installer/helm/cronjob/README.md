# Helm CronJob

A Kubernetes CronJob to automate the update of [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span) data. You will already need an account and licenses from Medi-Span for this helm chart to work.

The default behaviour is to do incremental updates weekly. 

See [values.yaml](values.yaml) to override behavior. 

## Examples
These commands are run from the /installer/helm folder.

### Convert an existing MediSpan.xml to an override file.
```bash
cat <(echo -e "conf:\n  MediSpan.xml: |"; \
      cat /path/to/MediSpan.xml | sed 's/^/    /') \
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
       cat /path/to/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
installs:
  downloadAndInstall:
    password: "somebase64string"
dataAccesId: PostgresData
EOF
```

### Do a manual initial full database  using the `SPECIFY_DIRECTORY` instead of `DOWNLOAD_AND_INSTALL`
This command overrides `type` to do a `FULL_DB` instead of an `INCREMENTAL_DB` install.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /path/to/MediSpan.xml | sed 's/^/    /') \
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

### Trigger the cronjob
```bash
echo "Manual Trigger cronjob to run installer immediately"
kubectl --namespace default create job --from=cronjob/m6n-installer m6p-installer-manual-init
```

### Change the cronjob to do a `DOWNLOAD_AND_INSTALL`.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /path/to/MediSpan.xml | sed 's/^/    /') \
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

### Delete Data using `DELETE`.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /path/to/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
dataAccesId: PostgresData
installs:
  type: DELETE
EOF
```

### Persist images and installer to local filestem.
This example use hostPath volume. A nfs volume could also be used if sharing of the files is needed.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /path/to/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
installs:
  downloadAndInstall:
    password: "somebase64string"
dataAccesId: PostgresData
extraVolumes:
  - name: images
    hostPath:
      path: /var/medispan/images
      type: Directory
  - name: installer
    hostPath:
      path: /var/medispan/installer
      type: Directory
extraVolumeMounts:
  - name: images
    mountPath: /var/medispan/images
  - name: installer
    mountPath: /var/medispan/installer
EOF
```

# What happens in the situation where the incremental stops working because the data is so far out of date that basically none of the above information applies?

## Step 1. Find this part in the YAML
```yaml
  - name: M6N_INSTALLS_TYPE
    value: SPECIFY_DIRECTORY
  - name: M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE
    value: INCREMENTAL_DB 
```

## Step 2. change the section to look like this
```yaml
  - name: M6N_INSTALLS_TYPE
    value: DELETE #change this value and comment out the lines below
  - name: M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE #leave this alone
    value: INCREMENTAL_DB
```
## Step 3. then manually trigger the job. wait for it to finish running

## Step 4. Go get the full db
```yaml
  - name: M6N_INSTALLS_TYPE
    value: SPECIFY_DIRECTORY # change this value back
  - name: M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE
    value: FULL_DB # change this value to FULL_DB
```
## Step 5. manually trigger this as well

## Step 6. Reset it back to what you originally found
```yaml
  - name: M6N_INSTALLS_TYPE
    value: SPECIFY_DIRECTORY
  - name: M6N_INSTALLS_SPECIFY_DIRECTORY_TYPE
    value: INCREMENTAL_DB  # change this value to 
```
#### you don't need to trigger this, because it will take effect next time the job runs