package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 17/07/2015.
 */
public class DirectionsLeg implements Serializable {
    private boolean hasTollRoad;
    private int index;
    private String roadGradeStrategy;
    private boolean hasHighway;
    private boolean hasUnpaved;
    private double distance;
    private int time;
    private int origIndex;
    private boolean hasSeasonalClosure;
    private String origNarrative;
    private boolean hasCountryCross;
    private String formattedTime;
    private String destNarrative;
    private int destIndex;
    private ArrayList<DirectionsManeuver> maneuvers;
    private boolean hasFerry;

    public DirectionsLeg() {
    }

    public DirectionsLeg(boolean hasTollRoad, int index, String roadGradeStrategy, boolean hasHighway, boolean hasUnpaved, double distance, int time, int origIndex, boolean hasSeasonalClosure, String origNarrative, boolean hasCountryCross, String formattedTime, String destNarrative, int destIndex, ArrayList<DirectionsManeuver> maneuvers, boolean hasFerry) {
        this.hasTollRoad = hasTollRoad;
        this.index = index;
        this.roadGradeStrategy = roadGradeStrategy;
        this.hasHighway = hasHighway;
        this.hasUnpaved = hasUnpaved;
        this.distance = distance;
        this.time = time;
        this.origIndex = origIndex;
        this.hasSeasonalClosure = hasSeasonalClosure;
        this.origNarrative = origNarrative;
        this.hasCountryCross = hasCountryCross;
        this.formattedTime = formattedTime;
        this.destNarrative = destNarrative;
        this.destIndex = destIndex;
        this.maneuvers = maneuvers;
        this.hasFerry = hasFerry;
    }

    public boolean isHasTollRoad() {
        return hasTollRoad;
    }

    public void setHasTollRoad(boolean hasTollRoad) {
        this.hasTollRoad = hasTollRoad;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRoadGradeStrategy() {
        return roadGradeStrategy;
    }

    public void setRoadGradeStrategy(String roadGradeStrategy) {
        this.roadGradeStrategy = roadGradeStrategy;
    }

    public boolean isHasHighway() {
        return hasHighway;
    }

    public void setHasHighway(boolean hasHighway) {
        this.hasHighway = hasHighway;
    }

    public boolean isHasUnpaved() {
        return hasUnpaved;
    }

    public void setHasUnpaved(boolean hasUnpaved) {
        this.hasUnpaved = hasUnpaved;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getOrigIndex() {
        return origIndex;
    }

    public void setOrigIndex(int origIndex) {
        this.origIndex = origIndex;
    }

    public boolean isHasSeasonalClosure() {
        return hasSeasonalClosure;
    }

    public void setHasSeasonalClosure(boolean hasSeasonalClosure) {
        this.hasSeasonalClosure = hasSeasonalClosure;
    }

    public String getOrigNarrative() {
        return origNarrative;
    }

    public void setOrigNarrative(String origNarrative) {
        this.origNarrative = origNarrative;
    }

    public boolean isHasCountryCross() {
        return hasCountryCross;
    }

    public void setHasCountryCross(boolean hasCountryCross) {
        this.hasCountryCross = hasCountryCross;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getDestNarrative() {
        return destNarrative;
    }

    public void setDestNarrative(String destNarrative) {
        this.destNarrative = destNarrative;
    }

    public int getDestIndex() {
        return destIndex;
    }

    public void setDestIndex(int destIndex) {
        this.destIndex = destIndex;
    }

    public ArrayList<DirectionsManeuver> getManeuvers() {
        return maneuvers;
    }

    public void setManeuvers(ArrayList<DirectionsManeuver> maneuvers) {
        this.maneuvers = maneuvers;
    }

    public boolean isHasFerry() {
        return hasFerry;
    }

    public void setHasFerry(boolean hasFerry) {
        this.hasFerry = hasFerry;
    }
}
