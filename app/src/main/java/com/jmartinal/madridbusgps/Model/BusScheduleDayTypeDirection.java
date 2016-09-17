package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;

/**
 * Created by Jorge on 23/11/2015.
 */
public class BusScheduleDayTypeDirection implements Serializable{
    private String startTime;
    private String stopTime;
    private int minimumFrequency;
    private int maximumFrequency;
    private String frequencyDescription;

    public BusScheduleDayTypeDirection() {
    }

    public BusScheduleDayTypeDirection(String startTime, String stopTime, int minimumFrequency, int maximumFrequency, String frequencyDescription) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.minimumFrequency = minimumFrequency;
        this.maximumFrequency = maximumFrequency;
        this.frequencyDescription = frequencyDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getMinimumFrequency() {
        return minimumFrequency;
    }

    public void setMinimumFrequency(int minimumFrequency) {
        this.minimumFrequency = minimumFrequency;
    }

    public int getMaximumFrequency() {
        return maximumFrequency;
    }

    public void setMaximumFrequency(int maximumFrequency) {
        this.maximumFrequency = maximumFrequency;
    }

    public String getFrequencyDescription() {
        return frequencyDescription;
    }

    public void setFrequencyDescription(String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
    }
}
