package com.jmartinal.madridbusgps.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 22/09/2015.
 */
public class DirectionsManeuver extends Maneuver implements Serializable {
    private ArrayList<String> signs;
    private int index;
    private ArrayList<String> maneuverNotes;
    private int direction;
    private String narrative;
    private String iconUrl;
    private double distance;
    private int time;
    private ArrayList<Object> linkIds;
    private ArrayList<String> streets;
    private int attributes;
    private String transportMode;
    private String formattedTime;
    private String directionName;
    private String mapUrl;
    private LatLng startPoint;
    private int turnType;

    public DirectionsManeuver() {
    }

    public DirectionsManeuver(ArrayList<String> signs, int index, ArrayList<String> maneuverNotes, int direction, String narrative, String iconUrl, double distance, int time, ArrayList<Object> linkIds, ArrayList<String> streets, int attributes, String transportMode, String formattedTime, String directionName, String mapUrl, LatLng startPoint, int turnType) {
        this.signs = signs;
        this.index = index;
        this.maneuverNotes = maneuverNotes;
        this.direction = direction;
        this.narrative = narrative;
        this.iconUrl = iconUrl;
        this.distance = distance;
        this.time = time;
        this.linkIds = linkIds;
        this.streets = streets;
        this.attributes = attributes;
        this.transportMode = transportMode;
        this.formattedTime = formattedTime;
        this.directionName = directionName;
        this.mapUrl = mapUrl;
        this.startPoint = startPoint;
        this.turnType = turnType;
    }

    public ArrayList<String> getSigns() {
        return signs;
    }

    public void setSigns(ArrayList<String> signs) {
        this.signs = signs;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<String> getManeuverNotes() {
        return maneuverNotes;
    }

    public void setManeuverNotes(ArrayList<String> maneuverNotes) {
        this.maneuverNotes = maneuverNotes;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public ArrayList<Object> getLinkIds() {
        return linkIds;
    }

    public void setLinkIds(ArrayList<Object> linkIds) {
        this.linkIds = linkIds;
    }

    public ArrayList<String> getStreets() {
        return streets;
    }

    public void setStreets(ArrayList<String> streets) {
        this.streets = streets;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public int getTurnType() {
        return turnType;
    }

    public void setTurnType(int turnType) {
        this.turnType = turnType;
    }
}
