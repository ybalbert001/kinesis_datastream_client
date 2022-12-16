package com.aws.kinesis.stream;


import software.amazon.awssdk.regions.Region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;
import software.amazon.kinesis.common.KinesisClientUtil;
import org.apache.commons.lang3.RandomStringUtils;
import software.amazon.awssdk.core.SdkBytes;

import java.util.concurrent.ExecutionException;


/**
 * Continuously sends simulated stock trades to Kinesis
 *
 */
public class StandaloneProducer {

    private static final Log LOG = LogFactory.getLog(StandaloneProducer.class);

    private static void checkUsage(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: " + StandaloneProducer.class.getSimpleName()
                    + " <stream name> <region>");
            System.exit(1);
        }
    }

    /**
     * Checks if the stream exists and is active
     *
     * @param kinesisClient Amazon Kinesis client instance
     * @param streamName Name of stream
     */
    private static void validateStream(KinesisAsyncClient kinesisClient, String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest =  DescribeStreamRequest.builder().streamName(streamName).build();
            DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest).get();
            if(!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        }catch (Exception e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void publishRecord(String streamName, KinesisAsyncClient kinesisClient, SampleRecord record) {
        PutRecordRequest request = PutRecordRequest.builder()
                .partitionKey(RandomStringUtils.randomAlphabetic(5, 20))
                .streamName(streamName)
                .data(SdkBytes.fromByteArray(record.toJsonAsBytes()))
                .build();
        try {
            PutRecordResponse resp = kinesisClient.putRecord(request).get();
            LOG.info(resp.toString());
        } catch (InterruptedException e) {
            LOG.info("Interrupted, assuming shutdown.");
        } catch (ExecutionException e) {
            LOG.error("Exception while sending data to Kinesis. Will try again next cycle.", e);
        }
    }

    public static void main(String[] args) throws Exception {
        checkUsage(args);

        String streamName = args[0];
        String regionName = args[1];
        String mock_data_path = args[2];
        Region region = Region.of(regionName);
        if (region == null) {
            System.err.println(regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        KinesisAsyncClient kinesisClient = KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder().region(region));

        validateStream(kinesisClient, streamName);

        RecordGenerator generator = new RecordGenerator(mock_data_path);
        while(true) {
            SampleRecord record = generator.getRecord();
            publishRecord(streamName, kinesisClient, record);
            Thread.sleep(100);
        }
    }
}