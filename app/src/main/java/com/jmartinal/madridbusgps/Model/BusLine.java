package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 30/01/2016.
 */
public class BusLine implements Serializable {
    private String groupNumber;
    private String dateFirst;
    private String dateEnd;
    private int line;
    private String label;
    private String nameA;
    private String nameB;
    private ArrayList<BusLineStop> departureStops;
    private ArrayList<BusLineStop> detourStops;
    private ArrayList<BusLineWayPoint> wayPoints;

    public BusLine() {
    }

    public BusLine(String groupNumber, String dateFirst, String dateEnd, int line, String label, String nameA, String nameB) {
        this.groupNumber = groupNumber;
        this.dateFirst = dateFirst;
        this.dateEnd = dateEnd;
        this.line = line;
        this.label = label;
        this.nameA = nameA;
        this.nameB = nameB;
        this.departureStops = new ArrayList<>();
        this.detourStops = new ArrayList<>();
        this.wayPoints = new ArrayList<>();
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getDateFirst() {
        return dateFirst;
    }

    public void setDateFirst(String dateFirst) {
        this.dateFirst = dateFirst;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public ArrayList<BusLineStop> getDepartureStops() {
        return departureStops;
    }

    public void setDepartureStops(ArrayList<BusLineStop> departureStops) {
        this.departureStops = departureStops;
    }

    public ArrayList<BusLineStop> getDetourStops() {
        return detourStops;
    }

    public void setDetourStops(ArrayList<BusLineStop> detourStops) {
        this.detourStops = detourStops;
    }

    public ArrayList<BusLineStop> getStops() {
        ArrayList<BusLineStop> stops = new ArrayList<>();
        for(BusLineStop stop : getDepartureStops()){
            stops.add(stop);
        }
        for(BusLineStop stop : getDetourStops()){
            stops.add(stop);
        }
        return stops;
    }

    public ArrayList<BusLineWayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(ArrayList<BusLineWayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    @Override
    public String toString() {
        return "Línea " + this.getLabel() + ": " + this.getNameA() + " - " + this.getNameB();
    }
}
