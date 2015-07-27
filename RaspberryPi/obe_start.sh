#!/bin/bash

SLEEP_COUNTER=$1

rm logs/*
touch logs/azure_storage.log

./snapshot.sh $SLEEP_COUNTER &

python azure_storage.py -n cailang -k 0RyEVa+Vi1oOFDzjafT+R04MVb5dDKDJOz2sZn9VRK61TFXnFNIlvAKQCmX6tbaRYtJ79BXTJvysdvnywJIztg== -c public -p obe-raspi-ns -b RootManageSharedAccessKey -v akI02tTnZP6N+XDqRQfQV2ynATN+bB0O/tuPIXpiMdU= -q obe-raspi -s $SLEEP_COUNTER &

tail -f logs/azure_storage.log
