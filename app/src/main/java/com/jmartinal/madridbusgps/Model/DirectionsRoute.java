package com.jmartinal.madridbusgps.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 21/09/2015.
 */
public class DirectionsRoute implements Serializable {
    private boolean hasTollRoad;
    private int fuelUsed;
    private ArrayList<Integer> maneuverIndexes;
    private ArrayList<LatLng> shapePoints;
    private ArrayList<Integer> legIndexes;
    private boolean hasUnpaved;
    private boolean hasHighway;
    private int realTime;
    private LatLng boundingBoxUL;
    private LatLng boundingBoxLR;
    private double distance;
    private int time;
    private ArrayList<Integer> locationSequence;
    private boolean hasSeasonalClosure;
    private String sessionId;
    private ArrayList<GeocodingLocation> locations;
    private boolean hasCountryCross;
    private ArrayList<DirectionsLeg> legs;
    private String formattedTime;
    private String routeErrMessage;
    private int routeErrErrorCode;
    private DirectionsOptions options;
    private boolean hasFerry;


    public DirectionsRoute() {
    }

    public DirectionsRoute(boolean hasTollRoad, int fuelUsed, ArrayList<Integer> maneuverIndexes, ArrayList<LatLng> shapePoints, ArrayList<Integer> legIndexes, boolean hasUnpaved, boolean hasHighway, int realTime, LatLng boundingBoxUL, LatLng boundingBoxLR, double distance, int time, ArrayList<Integer> locationSequence, boolean hasSeasonalClosure, String sessionId, ArrayList<GeocodingLocation> locations, boolean hasCountryCross, ArrayList<DirectionsLeg> legs, String formattedTime, String routeErrMessage, int routeErrErrorCode, DirectionsOptions options, boolean hasFerry) {
        this.hasTollRoad = hasTollRoad;
        this.fuelUsed = fuelUsed;
        this.maneuverIndexes = maneuverIndexes;
        this.shapePoints = shapePoints;
        this.legIndexes = legIndexes;
        this.hasUnpaved = hasUnpaved;
        this.hasHighway = hasHighway;
        this.realTime = realTime;
        this.boundingBoxUL = boundingBoxUL;
        this.boundingBoxLR = boundingBoxLR;
        this.distance = distance;
        this.time = time;
        this.locationSequence = locationSequence;
        this.hasSeasonalClosure = hasSeasonalClosure;
        this.sessionId = sessionId;
        this.locations = locations;
        this.hasCountryCross = hasCountryCross;
        this.legs = legs;
        this.formattedTime = formattedTime;
        this.routeErrMessage = routeErrMessage;
        this.routeErrErrorCode = routeErrErrorCode;
        this.options = options;
        this.hasFerry = hasFerry;
    }

    public boolean isHasTollRoad() {
        return hasTollRoad;
    }

    public void setHasTollRoad(boolean hasTollRoad) {
        this.hasTollRoad = hasTollRoad;
    }

    public int getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(int fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public ArrayList<Integer> getManeuverIndexes() {
        return maneuverIndexes;
    }

    public void setManeuverIndexes(ArrayList<Integer> maneuverIndexes) {
        this.maneuverIndexes = maneuverIndexes;
    }

    public ArrayList<LatLng> getShapePoints() {
        return shapePoints;
    }

    public void setShapePoints(ArrayList<LatLng> shapePoints) {
        this.shapePoints = shapePoints;
    }

    public ArrayList<Integer> getLegIndexes() {
        return legIndexes;
    }

    public void setLegIndexes(ArrayList<Integer> legIndexes) {
        this.legIndexes = legIndexes;
    }

    public boolean isHasUnpaved() {
        return hasUnpaved;
    }

    public void setHasUnpaved(boolean hasUnpaved) {
        this.hasUnpaved = hasUnpaved;
    }

    public boolean isHasHighway() {
        return hasHighway;
    }

    public void setHasHighway(boolean hasHighway) {
        this.hasHighway = hasHighway;
    }

    public int getRealTime() {
        return realTime;
    }

    public void setRealTime(int realTime) {
        this.realTime = realTime;
    }

    public LatLng getBoundingBoxUL() {
        return boundingBoxUL;
    }

    public void setBoundingBoxUL(LatLng boundingBoxUL) {
        this.boundingBoxUL = boundingBoxUL;
    }

    public LatLng getBoundingBoxLR() {
        return boundingBoxLR;
    }

    public void setBoundingBoxLR(LatLng boundingBoxLR) {
        this.boundingBoxLR = boundingBoxLR;
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

    public ArrayList<Integer> getLocationSequence() {
        return locationSequence;
    }

    public void setLocationSequence(ArrayList<Integer> locationSequence) {
        this.locationSequence = locationSequence;
    }

    public boolean isHasSeasonalClosure() {
        return hasSeasonalClosure;
    }

    public void setHasSeasonalClosure(boolean hasSeasonalClosure) {
        this.hasSeasonalClosure = hasSeasonalClosure;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ArrayList<GeocodingLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<GeocodingLocation> locations) {
        this.locations = locations;
    }

    public boolean isHasCountryCross() {
        return hasCountryCross;
    }

    public void setHasCountryCross(boolean hasCountryCross) {
        this.hasCountryCross = hasCountryCross;
    }

    public ArrayList<DirectionsLeg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<DirectionsLeg> legs) {
        this.legs = legs;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getRouteErrMessage() {
        return routeErrMessage;
    }

    public void setRouteErrMessage(String routeErrMessage) {
        this.routeErrMessage = routeErrMessage;
    }

    public int getRouteErrErrorCode() {
        return routeErrErrorCode;
    }

    public void setRouteErrErrorCode(int routeErrErrorCode) {
        this.routeErrErrorCode = routeErrErrorCode;
    }

    public DirectionsOptions getOptions() {
        return options;
    }

    public void setOptions(DirectionsOptions options) {
        this.options = options;
    }

    public boolean isHasFerry() {
        return hasFerry;
    }

    public void setHasFerry(boolean hasFerry) {
        this.hasFerry = hasFerry;
    }
}
