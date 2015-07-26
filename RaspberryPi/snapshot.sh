#!/bin/bash

SLEEP_COUNTER=$1
#CONTAINER="public"

#while [ $COUNTER -gt 0 ]
while true
do
   DATE=$(date +"%Y-%m-%d_%H%M%S")
   FILENAME="image_$DATE.jpg"

   raspistill -vf -hf -w 1024 -h 768 -t 1 -o images/$FILENAME >> logs/raspi_camera.log 2>&1

   # Use Azure X-CLI Node.js
   # NOT USED NOW BECAUSE WE ARE USING PYTHON
   #echo "===== Starting at $DATE =====" >> logs/azure_blob.log
   #azure storage blob upload --container $CONTAINER --file images/$FILENAME --blobtype Block --quiet >> logs/azure_blob.log 2>&1
   #rm images/$FILENAME

   sleep $SLEEP_COUNTER
   #COUNTER=$(( $COUNTER - 1 ))
done
