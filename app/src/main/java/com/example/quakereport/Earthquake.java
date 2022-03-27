package com.example.quakereport;

public class Earthquake {
    private double magnitude;
    private String location;
    private long time;
    private String url;

    Earthquake(double magnitude,String location,long time,String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude() { return magnitude; }
    public long getTime() { return time; }
    public String getLocation() { return location; }
    public String getUrl() { return url; }
}
