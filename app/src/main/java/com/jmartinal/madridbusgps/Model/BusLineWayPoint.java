package com.jmartinal.madridbusgps.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

/**
 * Created by Jorge on 24/10/2015.
 */
public class BusLineWayPoint implements Serializable {

    private int line;
    private int secDetail;
    private String orderDetail;
    private String node;
    private String name;
    private int distance;
    private int distancePreviousStop;
    private double latitude;
    private double longitude;

    public BusLineWayPoint() {
    }

    public BusLineWayPoint(int line, int secDetail, String orderDetail, String node, String name, int distance, int distancePreviousStop, double latitude, double longitude) {
        this.line = line;
        this.secDetail = secDetail;
        this.orderDetail = orderDetail;
        this.node = node;
        this.name = name;
        this.distance = distance;
        this.distancePreviousStop = distancePreviousStop;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getSecDetail() {
        return secDetail;
    }

    public void setSecDetail(int secDetail) {
        this.secDetail = secDetail;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistancePreviousStop() {
        return distancePreviousStop;
    }

    public void setDistancePreviousStop(int distancePreviousStop) {
        this.distancePreviousStop = distancePreviousStop;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(this.getLatitude(), this.getLongitude());
    }
}
