package com.shiva.tissueanalyticsassignment;

import java.io.Serializable;

public class StreamFlowData implements Serializable {

    //declaring variables
    private String siteName;
    private String siteCode;
    private double minValue;
    private double maxValue;
    private double averageValue;
    private int[] streamValues;

    public StreamFlowData() {
    }


    //Getters and Setters
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public int[] getStreamValues() {
        return streamValues;
    }

    public void setStreamValues(int[] streamValues) {
        this.streamValues = streamValues;
    }
}
