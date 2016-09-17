package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 07/12/2015.
 */
public class BusRoute implements Serializable{

    private BusNetworkNode firstNode;
    private BusNetworkNode lastNode;
    private int nTransfers;
    private ArrayList<Integer> lines;
    private ArrayList<Integer> ways;
    private ArrayList<Maneuver> maneuvers;
    private ArrayList<BusNetworkRouteNode> nodes;
    private ArrayList<BusLineWayPoint> wayPoints;

    public BusRoute() {
    }

    public BusRoute(BusNetworkNode firstNode, BusNetworkNode lastNode, int nTransfers, ArrayList<Integer> lines, ArrayList<Maneuver> maneuvers, ArrayList<BusNetworkRouteNode> nodes, ArrayList<BusLineWayPoint> wayPoints) {
        this.firstNode = firstNode;
        this.lastNode = lastNode;
        this.nTransfers = nTransfers;
        this.lines = lines;
        this.ways = new ArrayList<>();
        this.maneuvers = maneuvers;
        this.nodes = nodes;
        this.wayPoints = wayPoints;
    }

    public BusNetworkNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(BusNetworkNode firstNode) {
        this.firstNode = firstNode;
    }

    public BusNetworkNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(BusNetworkNode lastNode) {
        this.lastNode = lastNode;
    }

    public ArrayList<Integer> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Integer> lines) {
        this.lines = lines;
    }

    public ArrayList<Integer> getWays() {
        return ways;
    }

    public void setWays(ArrayList<Integer> ways) {
        this.ways = ways;
    }

    public int getnTransfers() {
        return nTransfers;
    }

    public void setnTransfers(int nTransfers) {
        this.nTransfers = nTransfers;
    }

    public ArrayList<Maneuver> getManeuvers() {
        return maneuvers;
    }

    public void setManeuvers(ArrayList<Maneuver> maneuvers) {
        this.maneuvers = maneuvers;
    }

    public ArrayList<BusNetworkRouteNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<BusNetworkRouteNode> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<BusLineWayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(ArrayList<BusLineWayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public double getDistance() {
        double distance = 0;
        if (getWayPoints() != null && !getWayPoints().isEmpty()) {
            for (BusLineWayPoint point : getWayPoints()) {
                distance += point.getDistancePreviousStop();
            }
        }
        return distance;
    }

    public ArrayList<BusNetworkRouteNode> getTransfers(){
        ArrayList<BusNetworkRouteNode> results = new ArrayList<>();
        for(BusNetworkRouteNode routeNode : getNodes()){
            if (routeNode.isTransferNode()){
                results.add(routeNode);
            }
        }
        return results;
    }

}
