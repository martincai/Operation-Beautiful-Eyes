#!/bin/bash

SLEEP_COUNTER=$1

rm logs/*
touch logs/azure_storage.log

./openiot-agent.sh >> logs/openiot.log 2>&1 &

./snapshot.sh $SLEEP_COUNTER &

python azure_storage.py -s $SLEEP_COUNTER &

tail -f logs/azure_storage.log
