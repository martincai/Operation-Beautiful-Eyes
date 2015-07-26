#!/bin/bash

SLEEP_COUNTER=$1

rm logs/*

./snapshot.sh $SLEEP_COUNTER &
python azure_storage.py -n cailang -k {storage_account_key} -c public -s $SLEEP_COUNTER &

