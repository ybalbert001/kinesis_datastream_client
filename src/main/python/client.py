import sys
import json
import random
import time

import boto3

if len(sys.argv) != 4:
  print("invalid arguments...")

stream_name = sys.argv[1]
region_name = sys.argv[2]
mock_data_path = sys.argv[3]

data_records = []
with open(mock_data_path, 'r') as data_file:
  lines = data_file.readlines()
  head = ['vendorid', 'lpep_pickup_datetime', 'lpep_dropoff_datetime', 'trip_distance', 'store_and_fwd_flag']
  data_records = [ dict(zip(head, line.split(','))) for line in lines ]


kinesis_client = boto3.client('kinesis',region_name=region_name)

for item_id,dict_item in enumerate(data_records):
  dict_item.update({"id": item_id})
  time.sleep(1)

  kinesis_client.put_record(
    StreamName=stream_name,
    Data=json.dumps(dict_item), # put_record expects a string
    PartitionKey=str(hash(dict_item['id'])) # partition key
  )

  print("produce data record {}".format(dict_item['id']))
