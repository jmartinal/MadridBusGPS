package com.jmartinal.madridbusgps.Model;

import java.util.ArrayList;

/**
 * Created by Jorge on 01/02/2016.
 */
public class BusNetworkRouteNode extends BusNetworkNode {

    private boolean isTransferNode;

    public BusNetworkRouteNode() {
    }

    public BusNetworkRouteNode(int node, String name, ArrayList<String> lines, double latitude, double longitude, boolean isTransferNode) {
        super(node, name, lines, latitude, longitude);
        this.isTransferNode = isTransferNode;
    }

    public boolean isTransferNode() {
        return isTransferNode;
    }

    public void setIsTransferNode(boolean isTransferNode) {
        this.isTransferNode = isTransferNode;
    }
}
