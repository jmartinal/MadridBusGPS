package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;

/**
 * Created by Jorge on 08/12/2015.
 */
public class Route implements Serializable{

    private DirectionsRoute firstWalkingRoute;
    private BusRoute busRoute;
    private DirectionsRoute lastWalkingRoute;

    public Route() {
    }

    public Route(DirectionsRoute firstWalkingRoute, BusRoute busRoute, DirectionsRoute lastWalkingRoute) {
        this.firstWalkingRoute = firstWalkingRoute;
        this.busRoute = busRoute;
        this.lastWalkingRoute = lastWalkingRoute;
    }

    public DirectionsRoute getFirstWalkingRoute() {
        return firstWalkingRoute;
    }

    public void setFirstWalkingRoute(DirectionsRoute firstWalkingRoute) {
        this.firstWalkingRoute = firstWalkingRoute;
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    public DirectionsRoute getLastWalkingRoute() {
        return lastWalkingRoute;
    }

    public void setLastWalkingRoute(DirectionsRoute lastWalkingRoute) {
        this.lastWalkingRoute = lastWalkingRoute;
    }

    public double getDistance(){
        return getFirstWalkingRoute().getDistance() + getBusRoute().getDistance() + getLastWalkingRoute().getDistance();
    }

    public int getBusStopsNumber(){
        return getBusRoute().getNodes().size();
    }
}
