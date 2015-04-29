#!/bin/bash

containerId=`docker run -d -p 6379:6379 redis:3`
./activator test-all
docker stop ${containerId}
