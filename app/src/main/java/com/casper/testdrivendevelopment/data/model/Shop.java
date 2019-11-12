package com.casper.testdrivendevelopment.data.model;

public class Shop {
    private String name;
    private String memo;
    private double latitude;
    private double longitude;

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
