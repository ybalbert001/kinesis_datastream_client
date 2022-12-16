### Usage

1. 创建kinesis data stream  ${stream_name}
2. 发送数据到 KDS
+ java版
```shell
mock_data_path=tripdata.csv

java -cp kds_client.jar com.aws.kinesis.stream.StandaloneProducer ${stream_name} ${region_name} ${mock_data_path}
```
+ python版
```python
import json
from boto import kinesis
kinesis_client = kinesis.connect_to_region("${region_name}")

test_record = {"id": 1132, "vendorid": 2, "lpep_pickup_datetime" : "2022-06-01 00 (tel:2022060100):29:05.000","lpep_dropoff_datetime" : "2022-06-01 00:31:05.000", "trip_distance" : 3.12, "store_and_fwd_flag": "N", "fare_amount":0.0}

kinesis_client.put_record(
  "${stream_name}",
  json.dumps(test_record), # put_record expects a string
  str(hash(test_record['id'])) # partition key
)
```



