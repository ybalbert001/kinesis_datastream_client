package com.aws.kinesis.stream;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Captures the key elements of a stock trade, such as the ticker symbol, price,
 * number of shares, the type of the trade (buy or sell), and an id uniquely identifying
 * the trade.
 */
public class SampleRecord {

    private final static ObjectMapper JSON = new ObjectMapper();

    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public long getId() {
        return id;
    }

    public long getVendorid() {
        return vendorid;
    }

    public String getLpep_pickup_datetime() {
        return lpep_pickup_datetime;
    }

    public String getLpep_dropoff_datetime() {
        return lpep_dropoff_datetime;
    }

    public double getTrip_distance() {
        return trip_distance;
    }

    public String getStore_and_fwd_flag() {
        return store_and_fwd_flag;
    }

    public double getFare_amount() {
        return fare_amount;
    }

    private long id;
    private long vendorid;
    private String lpep_pickup_datetime;
    private String lpep_dropoff_datetime;

    private double trip_distance;
    private String store_and_fwd_flag;
    private double fare_amount;

    public SampleRecord() {
    }

    public SampleRecord(long id, long vendorid, String lpep_pickup_datetime, String lpep_dropoff_datetime, double trip_distance, String store_and_fwd_flag) {
        this.id = id;
        this.vendorid = vendorid;
        this.lpep_pickup_datetime = lpep_pickup_datetime;
        this.lpep_dropoff_datetime = lpep_dropoff_datetime;
        this.trip_distance = trip_distance;
        this.store_and_fwd_flag = store_and_fwd_flag;
    }


    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    public static SampleRecord fromJsonAsBytes(byte[] bytes) {
        try {
            return JSON.readValue(bytes, SampleRecord.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("[%d] vendorid:%d |%d - %d|, distance: %f", id,
                vendorid, lpep_pickup_datetime, lpep_dropoff_datetime, trip_distance);
    }
}