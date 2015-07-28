#!/bin/bash

ps -ef | grep snapshot.sh | awk '{print $2}' | xargs kill
