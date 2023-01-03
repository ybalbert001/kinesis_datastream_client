import sys
import json
import random
import time

from boto import kinesis

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

for item_id,dict_item in enumerate(data_records):
  dict_item.update({"id": item_id})

data_len = len(data_records)

kinesis_client = kinesis.connect_to_region(region_name)

while True:
  idx = int(random.uniform(0, data_len))
  test_record = data_records[idx]
  time.sleep(0.1)

  kinesis_client.put_record(
    stream_name,
    json.dumps(test_record), # put_record expects a string
    str(hash(test_record['id'])) # partition key
  )