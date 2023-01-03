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
python3 src/main/python/client.py ${stream_name} ${region_name} ${mock_data_path}
```



