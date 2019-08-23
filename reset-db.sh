#!/usr/bin/env bash

docker stop pg-inpd
docker rm pg-inpd
docker run --name pg-inpd -e POSTGRES_PASSWORD=inpd -d -p 5432:5432 postgres
sleep 5
PGPASSWORD=inpd psql -h localhost -U postgres      < create.sql
PGPASSWORD=inpd psql -h localhost -U postgres inpd < schema.sql
PGPASSWORD=inpd psql -h localhost -U postgres inpd < sample-data.sql
