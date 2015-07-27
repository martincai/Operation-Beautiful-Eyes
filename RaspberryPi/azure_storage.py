#!/usr/bin/python

import os, glob, sys, getopt, time, datetime, operator, logging

# Set up logging
logging.basicConfig(filename='logs/azure_storage.log', level=logging.DEBUG, format='%(asctime)s %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p')

# Define Azure BLOB and Service Bus services
from azure.storage import BlobService
from azure.servicebus import ServiceBusService, Message, Topic, Rule, DEFAULT_RULE_NAME
azure_storage_acct_name = ''
azure_storage_acct_key = ''
azure_storage_acct_container = ''
azure_servicebus_namespace = ''
azure_servicebus_key_name = ''
azure_servicebus_key_value = ''
azure_servicebus_queue = ''
sleep_time = 0.0
try:
   opts, args = getopt.getopt(sys.argv[1:], "n:k:c:p:b:v:q:s:")
except getopt.GetoptError:
   print 'Argument error! Usage: azure_storage.py -n account_name -k account_key -c container_name -p servicebus_namespace -b servicebus_keyname -v servicebus_keyvalue -q servicebus_queue -s sleep_second'
   logging.debug('Argument error!')
   sys.exit(2)
for opt, arg in opts:
   if opt == '-n':
      azure_storage_acct_name = arg
   elif opt == '-k':
      azure_storage_acct_key = arg
   elif opt == '-c':
      azure_storage_acct_container = arg
   elif opt == '-p':
      azure_servicebus_namespace = arg
   elif opt == '-b':
      azure_servicebus_key_name = arg
   elif opt == '-v':
      azure_servicebus_key_value = arg
   elif opt == '-q':
      azure_servicebus_queue = arg
   elif opt == '-s':
      sleep_time = float(arg)
blob_service = BlobService(account_name=azure_storage_acct_name, account_key=azure_storage_acct_key)
logging.debug('Azure storage connection created.')
bus_service = ServiceBusService(service_namespace=azure_servicebus_namespace, shared_access_key_name=azure_servicebus_key_name, shared_access_key_value=azure_servicebus_key_value)
logging.debug('Azure service bus connection created.')

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
      time.sleep(sleep_time)
   else:
      img_filename = os.path.basename(img_filefullpath)
      # Upload the oldest image
      blob_service.put_block_blob_from_path(azure_storage_acct_container, img_filename, img_filefullpath, x_ms_blob_content_type='image/jpeg')
      img_azureblob_url = 'http://'+azure_storage_acct_name+'.blob.core.windows.net/'+azure_storage_acct_container+'/'+img_filename
      logging.debug('Uploaded to %s', img_azureblob_url)
      msg = Message(img_azureblob_url)
      bus_service.send_queue_message(azure_servicebus_queue, msg)
      # Remove the image after uploading
      os.remove(img_filefullpath)
      # If need to sleep for more than 1 second, sleep
      #if sleep_time > 1:
      #   time.sleep(sleep_time-1)

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
