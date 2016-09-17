package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 16/11/2015.
 */
public class BusSchedule implements Serializable{

    private long date;
    private int lineId;
    private String label;
    private String headerA;
    private String headerB;
    private int incidents;
    private ArrayList<BusScheduleDayType> typeDays;

    public BusSchedule() {
    }

    public BusSchedule(long date, int lineId, String label, String headerA, String headerB, int incidents, ArrayList<BusScheduleDayType> typeDays) {
        this.date = date;
        this.lineId = lineId;
        this.label = label;
        this.headerA = headerA;
        this.headerB = headerB;
        this.incidents = incidents;
        this.typeDays = typeDays;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHeaderA() {
        return headerA;
    }

    public void setHeaderA(String headerA) {
        this.headerA = headerA;
    }

    public String getHeaderB() {
        return headerB;
    }

    public void setHeaderB(String headerB) {
        this.headerB = headerB;
    }

    public int getIncidents() {
        return incidents;
    }

    public void setIncidents(int incidents) {
        this.incidents = incidents;
    }

    public ArrayList<BusScheduleDayType> getTypeDays() {
        return typeDays;
    }

    public void setTypeDays(ArrayList<BusScheduleDayType> typeDays) {
        this.typeDays = typeDays;
    }
}
