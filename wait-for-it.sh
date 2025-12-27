#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

until mysql -h "$host" -u"$SPRING_DATASOURCE_USERNAME" -p"$SPRING_DATASOURCE_PASSWORD" -e "select 1" >/dev/null 2>&1; do
  echo "Esperando pelo MySQL em $host..."
  sleep 2
done

exec $cmd
