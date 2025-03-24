#!/bin/bash
set -e
cd $(dirname ${0})
helm package cronjob -d ../../../charts
helm repo index ../../../charts