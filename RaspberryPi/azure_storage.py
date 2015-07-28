#!/usr/bin/python

import os, subprocess, glob, sys, getopt, time, datetime, operator, logging

# Set up logging
logging.basicConfig(filename='logs/azure_storage.log', level=logging.DEBUG, format='%(asctime)s %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

# Define Azure BLOB, Service Bus, OpenIOT variables
from azure.storage import BlobService
#from azure.servicebus import ServiceBusService, Message, Topic, Rule, DEFAULT_RULE_NAME
azure_storage_acct_name = 'cailang'
azure_storage_acct_key = '0RyEVa+Vi1oOFDzjafT+R04MVb5dDKDJOz2sZn9VRK61TFXnFNIlvAKQCmX6tbaRYtJ79BXTJvysdvnywJIztg=='
azure_storage_acct_container = 'public'
#azure_servicebus_namespace = 'obe-raspi-ns'
#azure_servicebus_key_name = 'RootManageSharedAccessKey'
#azure_servicebus_key_value = 'akI02tTnZP6N+XDqRQfQV2ynATN+bB0O/tuPIXpiMdU='
#azure_servicebus_queue = 'obe-raspi'
sleep_time = 1.0
openiot_outbound = 'amqps://device:uWk12lmS00SnItMUzJgRd18oYi%2BfpFqmQGR%2BKbUb1%2BI%3D@iot-dev-ns.servicebus.chinacloudapi.cn/dev'
openiot_hardware_id = 'B8:27:EB:FB:47:08'
openiot_spec_id = '7dfd6d63-5e8d-4380-be04-fc5c73801dfb'

# Parse arguments
try:
#   opts, args = getopt.getopt(sys.argv[1:], "n:k:c:p:b:v:q:s:")
   opts, args = getopt.getopt(sys.argv[1:], "s:")
except getopt.GetoptError:
   print 'Argument error!'
   logging.debug('Argument error!')
   sys.exit(2)
for opt, arg in opts:
   if opt == '-s':
      sleep_time = float(arg)
#   elif opt == '-n':
#      azure_storage_acct_name = arg
#   elif opt == '-k':
#      azure_storage_acct_key = arg
#   elif opt == '-c':
#      azure_storage_acct_container = arg
#   elif opt == '-p':
#      azure_servicebus_namespace = arg
#   elif opt == '-b':
#      azure_servicebus_key_name = arg
#   elif opt == '-v':
#      azure_servicebus_key_value = arg
#   elif opt == '-q':
#      azure_servicebus_queue = arg
blob_service = BlobService(account_name=azure_storage_acct_name, account_key=azure_storage_acct_key)
logging.debug('Azure storage connection created.')
#bus_service = ServiceBusService(service_namespace=azure_servicebus_namespace, shared_access_key_name=azure_servicebus_key_name, shared_access_key_value=azure_servicebus_key_value)
#logging.debug('Azure service bus connection created.')

# Define method to get the oldest file
# Set _invert to True for getting the youngest file
def get_oldest_file(files, _invert=False):
   gt = operator.lt if _invert else operator.gt
   # Check if the list is empty
   if not files:
      return None
   # Select first as arbitrary sentinel file, storing name and age
   now = time.time()
   oldest = files[0], now - os.path.getctime(files[0])
   # Iterate over all remaining files
   for file in files[1:]:
      age = now - os.path.getctime(file)
      if gt(age, oldest[1]):
         # Set the new oldest file
         oldest = file, age
   # Return just the name of the oldest file
   return oldest[0]

# Start looping
while True:
   # Get image path
   img_filename = ''
   img_filepath = 'images/'
   img_filelist = glob.glob(img_filepath + '*.jpg')
   img_filefullpath = get_oldest_file(img_filelist)
   if img_filefullpath is None:
      # There is no image to upload, so sleep
      logging.debug('Image directory is empty.')
      time.sleep(1)
   else:
      img_filename = os.path.basename(img_filefullpath)
      # Upload the oldest image
      blob_service.put_block_blob_from_path(azure_storage_acct_container, img_filename, img_filefullpath, x_ms_blob_content_type='image/jpeg')
      img_azureblob_url = azure_storage_acct_name+'.blob.core.windows.net/'+azure_storage_acct_container+'/'+img_filename
      logging.debug('Uploaded to http://%s', img_azureblob_url)
#      msg = Message(img_azureblob_url)
#      bus_service.send_queue_message(azure_servicebus_queue, msg)
      msg = 'info.newImageUploaded:' + img_azureblob_url
      subprocess.call(["./openiot-agent.bin", "-o", openiot_outbound, "-i", openiot_hardware_id, "-s", openiot_spec_id, "-a", msg])
      # Remove the image after uploading
      os.remove(img_filefullpath)
      # If need to sleep for more than 1 second, sleep
#      if sleep_time > 1:
#         time.sleep(sleep_time-1)

# REST ARE ALL TEST CODE

# List all blobs
#blobs = blob_service.list_blobs('public')
#for blob in blobs:
#	print(blob.name)
#	print(blob.url)

# Upload blobs
#print datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')
#blob_service.put_block_blob_from_path('public', img_filename, img_fullpath, x_ms_blob_content_type='image/jpeg')

# Test
#print img_filefullpath
#print img_filename
#print sys.argv[1:]
