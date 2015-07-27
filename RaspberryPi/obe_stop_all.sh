#!/bin/bash

ps -ef | grep snapshot.sh | awk '{print $2}' | xargs kill
ps -ef | grep azure_storage.py | awk '{print $2}' | xargs kill
