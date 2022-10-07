# Helm CronJob

A Kubernetes CronJob to automate the update of [Medi-Span®](https://www.wolterskluwer.com/en/solutions/medi-span) data. You will already need an account and licenses from Medi-Span to use this helm chart.

The default behaviour is to do incremental updates weekly. 

See [values.yaml](values.yaml) to override behavior. 

## Examples
These commands are run from the /installer/helm folder.

### Convert an existing MediSpan.xml to an override file.
```bash
cat <(echo -e "conf:\n  MediSpan.xml: |"; \
      cat /medispan/conf/MediSpan.xml | sed 's/^/    /') \
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
```

### Use an existing MediSpan.xml without converting it first.
This command combines the previous two into one command by using a subshell to properly format the MediSpan.xml as an override. It also uses an inline override instead of using `--set`.
```bash
cat <<EOF | helm upgrade --install --namespace default \ 
  m6n-installer . \
  -f <(echo -e "conf:\n  MediSpan.xml: |"; \ 
       cat /medispan/conf/MediSpan.xml | sed 's/^/    /') \
  -f -
username: medispan-username
password: medispan-password
dataAccesId: PostgresData
EOF
```