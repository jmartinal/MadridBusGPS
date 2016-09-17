package com.jmartinal.madridbusgps.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 30/01/2016.
 */
public class BusNetworkNode implements Serializable {

    private int node;
    private String name;
    private ArrayList<String> lines;
    private double latitude;
    private double longitude;
    private ArrayList<Integer> nextNodes;

    public BusNetworkNode() {
    }

    public BusNetworkNode(int node, String name, ArrayList<String> lines, double latitude, double longitude) {
        this.node = node;
        this.name = name;
        this.lines = lines;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nextNodes = new ArrayList<>();
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
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

    public ArrayList<Integer> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(ArrayList<Integer> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public LatLng getLatLng(){
        return new LatLng(getLatitude(), getLongitude());
    }
}
