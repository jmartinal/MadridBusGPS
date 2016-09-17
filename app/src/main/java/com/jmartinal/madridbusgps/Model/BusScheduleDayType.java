package com.jmartinal.madridbusgps.Model;

import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by Jorge on 23/11/2015.
 */
public class BusScheduleDayType implements Serializable{
    private String dayTypeId;
    private BusScheduleDayTypeDirection direction1;
    private BusScheduleDayTypeDirection direction2;

    public BusScheduleDayType() {
    }

    public BusScheduleDayType(String dayTypeId, BusScheduleDayTypeDirection direction1, BusScheduleDayTypeDirection direction2) {
        this.dayTypeId = dayTypeId;
        this.direction1 = direction1;
        this.direction2 = direction2;
    }

    public String getDayTypeId() {
        return dayTypeId;
    }

    public void setDayTypeId(String dayTypeId) {
        this.dayTypeId = dayTypeId;
    }

    public BusScheduleDayTypeDirection getDirection1() {
        return direction1;
    }

    public void setDirection1(BusScheduleDayTypeDirection direction1) {
        this.direction1 = direction1;
    }

    public BusScheduleDayTypeDirection getDirection2() {
        return direction2;
    }

    public void setDirection2(BusScheduleDayTypeDirection direction2) {
        this.direction2 = direction2;
    }
}
