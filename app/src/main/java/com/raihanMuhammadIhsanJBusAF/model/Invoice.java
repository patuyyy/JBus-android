package com.raihanMuhammadIhsanJBusAF.model;

import java.sql.Timestamp;

public class Invoice extends Serializable{
    public enum BusRating
    {
        NONE, NEUTRAL, GOOD, BAD
    }
    public enum PaymentStatus
    {
        FAILED, WAITING, SUCCESS
    }
    // instance variables - replace the example below with your own
    public int buyerId;
    public int renterId;
    public BusRating rating;
    public Timestamp time;
    public PaymentStatus status;
}
