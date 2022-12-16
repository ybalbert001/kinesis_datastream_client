package com.aws.kinesis.stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.lang3.RandomUtils;
//import org.apache.hadoop.fs.Path;
//import org.apache.avro.generic.GenericRecord;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.parquet.hadoop.ParquetReader;
//import org.apache.parquet.avro.AvroParquetReader;
//import org.apache.parquet.hadoop.util.HadoopInputFile;
//import org.apache.parquet.io.InputFile;
//import org.apache.hadoop.fs.s3a.S3AFileSystem;
/**
 * Generates random stock trades by picking randomly from a collection of stocks, assigning a
 * random price based on the mean, and picking a random quantity for the shares.
 *
 */

public class RecordGenerator {

    private static final Log LOG = LogFactory.getLog(RecordGenerator.class);
    private AtomicLong id = new AtomicLong(1);

    private ArrayList<SampleRecord> m_records = new ArrayList<>();
    /**
     * Return a random stock trade with a unique id every time.
     *
     */
    public RecordGenerator(String mock_data_path) {
        //mock_data_path = "/Users/ybalbert/Documents/all_resources/AB23/trip2.csv";
        String COMMA_DELIMITER = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(mock_data_path));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                SampleRecord rec = new SampleRecord(
                        id.getAndIncrement(),
                        Long.parseLong(values[0]),
                        values[1],
                        values[2],
                        Double.parseDouble(values[3]),
                        values[4]);
                this.m_records.add(rec);
            }
        }
        catch (IOException e) {
            LOG.error("IO error... ", e);
        }
    }

    public SampleRecord getRecord() {
        int size = m_records.size();
        int idx = RandomUtils.nextInt(0, size);

        return m_records.get(idx);
    }


}