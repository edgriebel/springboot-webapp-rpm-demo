#!/bin/bash
SHUTDOWN_HOSTNAME=${SHUTDOWN_HOSTNAME:-localhost}

for x in 8080 80; do 
	(set -x; curl --connect-timeout 1 -d x http://${SHUTDOWN_HOSTNAME}:${x}/manage/shutdown) && exit 0
done
# hit the non-ssl ports in case it's running in dev and someone didn't change the port
for x in 8443 443 8080 80; do 
	(set -x; curl --insecure --connect-timeout 1 -d x https://${SHUTDOWN_HOSTNAME}:${x}/manage/shutdown) && exit 0
done
