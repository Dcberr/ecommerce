docker exec garage /garage layout assign -z default -c 5G $(docker exec garage /garage node id | cut -d@ -f1)
docker exec garage /garage layout apply --version 1