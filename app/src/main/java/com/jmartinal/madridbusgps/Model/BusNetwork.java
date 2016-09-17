package com.jmartinal.madridbusgps.Model;

import android.location.Location;
import android.util.Log;

import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.Utils.App;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jorge on 30/01/2016.
 */
public class BusNetwork implements Serializable {

    private ArrayList<BusLine> busLines;
    private ArrayList<BusNetworkNode> busNetworkNodes;
    private ArrayList<BusNetworkNode> busNetworkDayNodes;
    private ArrayList<BusNetworkNode> busNetworkNightNodes;

    public BusNetwork() {
    }

    public BusNetwork(ArrayList<BusLine> busLines, ArrayList<BusLineStop> busLineStops, ArrayList<BusNetworkNode> busNetworkNodes) {
        this.busLines = busLines;
        this.busNetworkNodes = busNetworkNodes;
        this.busNetworkDayNodes = new ArrayList<>();
        this.busNetworkNightNodes = new ArrayList<>();
        assignStopToLine(busLineStops);
        generateNetwork(busNetworkNodes);
    }

    public ArrayList<BusLine> getBusLines() {
        return busLines;
    }

    public void setBusLines(ArrayList<BusLine> busLines) {
        this.busLines = busLines;
    }

    public ArrayList<BusNetworkNode> getBusNetworkNodes() {
        return busNetworkNodes;
    }

    public void setBusNetworkNodes(ArrayList<BusNetworkNode> busNetworkNodes) {
        this.busNetworkNodes = busNetworkNodes;
    }

    public ArrayList<BusNetworkNode> getBusNetworkDayNodes() {
        return busNetworkDayNodes;
    }

    public void setBusNetworkDayNodes(ArrayList<BusNetworkNode> busNetworkDayNodes) {
        this.busNetworkDayNodes = busNetworkDayNodes;
    }

    public ArrayList<BusNetworkNode> getBusNetworkNightNodes() {
        return busNetworkNightNodes;
    }

    public void setBusNetworkNightNodes(ArrayList<BusNetworkNode> busNetworkNightNodes) {
        this.busNetworkNightNodes = busNetworkNightNodes;
    }

    private void assignStopToLine(ArrayList<BusLineStop> busLineStops) {
        for (BusLineStop busStop : busLineStops) {
            BusLine busLine = findLineByLineId(busStop.getLine());
            busLine.getStops().add(busStop);
            if (busStop.getSecDetail() == 10) {
                busLine.getDepartureStops().add(busStop);
            } else if (busStop.getSecDetail() == 20) {
                busLine.getDetourStops().add(busStop);
            }
        }
    }

    public BusNetworkNode findBusNetworkNodeByNode(int node, boolean useNightLines) {
        BusNetworkNode result = null;

        ArrayList<BusNetworkNode> nodes;
        if (useNightLines){
            nodes = getBusNetworkNightNodes();
        } else {
            nodes = getBusNetworkDayNodes();
        }

        int i = 0;
        boolean positiveMatch = false;
        while (i < nodes.size() && !positiveMatch) {
            positiveMatch = nodes.get(i).getNode() == node;
            if (!positiveMatch) {
                i++;
            }
        }
        if (positiveMatch) {
            result = nodes.get(i);
        }
        return result;
    }

    private BusNetworkNode findBusNetworkNodeByNode(int node){
        BusNetworkNode result = null;

        int i = 0;
        boolean positiveMatch = false;
        while (i < getBusNetworkNodes().size() && !positiveMatch) {
            positiveMatch = getBusNetworkNodes().get(i).getNode() == node;
            if (!positiveMatch) {
                i++;
            }
        }
        if (positiveMatch) {
            result = getBusNetworkNodes().get(i);
        }
        return result;

    }

    private BusLine findLineByLineId(int line) {
        BusLine busLine = null;
        boolean positiveMatch = false;
        int i = 0;
        do {
            positiveMatch = this.getBusLines().get(i).getLine() == line;
            if (!positiveMatch) {
                i++;
            } else {
                busLine = this.getBusLines().get(i);
            }
        } while (i < this.getBusLines().size() && !positiveMatch);

        return busLine;
    }

    private void generateNetwork(ArrayList<BusNetworkNode> busNetworkNodes) {
        for (BusNetworkNode busNetworkNode : busNetworkNodes) {
            ArrayList<String> busLineIDS = busNetworkNode.getLines();
            for (String busLineID : busLineIDS) {
                try {
                    int lineID = Integer.valueOf(busLineID.split("/")[0]);
                    int lineWay = Integer.valueOf(busLineID.split("/")[1]);
                    BusLine busLine = findLineByLineId(lineID);
                    int busStopIndex = findBusStopIndexByNetworkNodeNode(busNetworkNode.getNode(), busLine, lineWay);

                    BusNetworkNode nextBusNetworkNode = null;
                    if (lineWay == 1) {
                        if (busStopIndex <= busLine.getDepartureStops().size() - 1) {
                            if (busStopIndex == busLine.getDepartureStops().size() - 1) {
                                nextBusNetworkNode = findBusNetworkNodeByNode(busLine.getDetourStops().get(0).getNode());
                            } else {
                                nextBusNetworkNode = findBusNetworkNodeByNode(busLine.getDepartureStops().get(busStopIndex + 1).getNode());
                            }
                        }
                    } else {
                        if (busStopIndex <= busLine.getDetourStops().size() - 1) {
                            if (busStopIndex == busLine.getDetourStops().size() - 1) {
                                nextBusNetworkNode = findBusNetworkNodeByNode(busLine.getDepartureStops().get(0).getNode());
                            } else {
                                nextBusNetworkNode = findBusNetworkNodeByNode(busLine.getDetourStops().get(busStopIndex + 1).getNode());
                            }
                        }
                    }

                    if (!busLine.getGroupNumber().startsWith("3")) {
                        BusNetworkNode dayNode = findBusNetworkNodeByNode(busNetworkNode.getNode(), false);
                        if (dayNode == null) {
                            ArrayList<String> lines = new ArrayList<>();
                            for (String line : busNetworkNode.getLines()) {
                                int nodeLineID = Integer.valueOf(line.split("/")[0]);
                                if (!findLineByLineId(nodeLineID).getGroupNumber().startsWith("3")) {
                                    lines.add(line);
                                }
                            }
                            dayNode = new BusNetworkNode(busNetworkNode.getNode(), busNetworkNode.getName(), lines, busNetworkNode.getLatitude(), busNetworkNode.getLongitude());
                            if (nextBusNetworkNode != null) {
                                dayNode.getNextNodes().add(nextBusNetworkNode.getNode());
                                getBusNetworkDayNodes().add(dayNode);
                            }
                        } else {
                            if (nextBusNetworkNode != null) {
                                dayNode.getNextNodes().add(nextBusNetworkNode.getNode());
                            }
                        }
                    } else {
                        BusNetworkNode nightNode = findBusNetworkNodeByNode(busNetworkNode.getNode(), true);
                        if (nightNode == null) {
                            ArrayList<String> lines = new ArrayList<>();
                            for (String line : busNetworkNode.getLines()) {
                                int nodeLineID = Integer.valueOf(line.split("/")[0]);
                                if (findLineByLineId(nodeLineID).getGroupNumber().startsWith("3")) {
                                    lines.add(line);
                                }
                            }
                            nightNode = new BusNetworkNode(busNetworkNode.getNode(), busNetworkNode.getName(), lines, busNetworkNode.getLatitude(), busNetworkNode.getLongitude());
                            if (nextBusNetworkNode != null) {
                                nightNode.getNextNodes().add(nextBusNetworkNode.getNode());
                                getBusNetworkNightNodes().add(nightNode);
                            }
                        } else {
                            if (nextBusNetworkNode != null) {
                                nightNode.getNextNodes().add(nextBusNetworkNode.getNode());
                            }
                        }
                    }
                } catch (NumberFormatException formatException) {
                    Log.d("LineID no reconocido: ", formatException.getMessage());
                }
            }
        }
    }

    private ArrayList<BusNetworkNode> findBusNetworkNodeContainingLines(String lineBeginId, String lineEndId, boolean useNightLines) {
        ArrayList<BusNetworkNode> results = new ArrayList<>();

        ArrayList<BusNetworkNode> nodes = new ArrayList<>();
        if (!useNightLines){
            nodes = getBusNetworkDayNodes();
        } else {
            nodes = getBusNetworkNightNodes();
        }

        for (BusNetworkNode networkNode : nodes) {
            if (networkNode.getLines().contains(lineBeginId) && networkNode.getLines().contains(lineEndId)) {
                results.add(networkNode);
            }
        }

        return results;
    }

    private ArrayList<BusNetworkRouteNode> findRouteNodesBetweenNetworkNodes(BusLine busLine, String busLineID, BusNetworkNode busNetworkNodeBegin, BusNetworkNode busNetworkNodeEnd, boolean useNightLines) {
        ArrayList<BusNetworkRouteNode> result = null;

        ArrayList<BusNetworkRouteNode> nodes = new ArrayList<>();
        int firstStopIndex = -1, lastStopIndex = -1;

        ArrayList<BusLineStop> stops = null;
        if (Integer.valueOf(busLineID.split("/")[1]) == 1) {
            stops = busLine.getDepartureStops();
        } else if (Integer.valueOf(busLineID.split("/")[1]) == 2) {
            stops = busLine.getDetourStops();
        }

        firstStopIndex = findBusStopIndexByNetworkNodeNode(busNetworkNodeBegin.getNode(), busLine, Integer.valueOf(busLineID.split("/")[1]));
        lastStopIndex = findBusStopIndexByNetworkNodeNode(busNetworkNodeEnd.getNode(), busLine, Integer.valueOf(busLineID.split("/")[1]));

        if (firstStopIndex < lastStopIndex) {
            for (int l = firstStopIndex; l <= lastStopIndex; l++) {
                if (l < stops.size()) {
                    BusNetworkNode networkNode = findBusNetworkNodeByNode(stops.get(l).getNode(), useNightLines);
                    nodes.add(new BusNetworkRouteNode(networkNode.getNode(), networkNode.getName(), networkNode.getLines(), networkNode.getLatitude(), networkNode.getLongitude(), false));
                }
            }
        } else {
            for (int l = firstStopIndex; l >= lastStopIndex; l--) {
                if (l < stops.size()) {
                    BusNetworkNode networkNode = findBusNetworkNodeByNode(stops.get(l).getNode(), useNightLines);
                    nodes.add(new BusNetworkRouteNode(networkNode.getNode(), networkNode.getName(), networkNode.getLines(), networkNode.getLatitude(), networkNode.getLongitude(), false));
                }
            }
        }

        result = nodes;

        return result;
    }

    public BusLine findBusLineCoincidences(BusNetworkNode nodeA, BusNetworkNode nodeB, boolean useNightLines){
        BusLine result = null;
        for (String lineAid : nodeA.getLines()) {
            for (String lineBid : nodeB.getLines()) {
                if(lineAid.equals(lineBid)){
                    BusLine line = findLineByLineId(Integer.valueOf(lineAid.split("/")[0]));
                    boolean timeMacth = (!useNightLines && !line.getGroupNumber().startsWith("3")) || (useNightLines && line.getGroupNumber().startsWith("3"));
                    if (timeMacth){
                        result = findLineByLineId(Integer.valueOf(lineAid.split("/")[0]));
                    }
                }
            }
        }
        return result;
    }

    public int findBusStopIndexByNetworkNodeNode(int node, BusLine busLine, int way) {
        ArrayList<BusLineStop> stops = null;
        if (way == 1) {
            stops = busLine.getDepartureStops();
        } else {
            stops = busLine.getDetourStops();
        }
        int i = 0;
        boolean positiveMatch = false;
        while (i < stops.size() && !positiveMatch) {
            if (stops.get(i).getNode() == node) {
                positiveMatch = true;
            }
            if (!positiveMatch) {
                i++;
            }
        }
        return i;
    }

    public BusRoute findNoTransferBusRoute(ArrayList<BusNetworkNode> nodesNearBegin, ArrayList<BusNetworkNode> nodesNearEnd, boolean useNightLines) {
        BusRoute result = null;

        BusLine simpleLine = null;
        boolean positiveMatch = false;
        int i = 0, j = 0;

        while (!positiveMatch && i < nodesNearBegin.size()){
            while (!positiveMatch && j < nodesNearEnd.size()){
                simpleLine = findBusLineCoincidences(nodesNearBegin.get(i), nodesNearEnd.get(j), useNightLines);
                positiveMatch = simpleLine != null;
                if (!positiveMatch){
                    j++;
                }
            }
            if (!positiveMatch){
                i++;
                j = 0;
            }
        }
        if (positiveMatch) {
            BusNetworkNode busNetworkNodeBegin = nodesNearBegin.get(i);
            BusNetworkNode busNetworkNodeEnd = nodesNearEnd.get(j);
            String simpleLineID = "";
            for (String beginNodeLineID : busNetworkNodeBegin.getLines()) {
                for (String endNodeLineID : busNetworkNodeEnd.getLines()) {
                    if (beginNodeLineID.equals(endNodeLineID)) {
                        simpleLineID = beginNodeLineID;
                    }
                }
            }
            int nTransfers = 0;

            ArrayList<Integer> lines = new ArrayList<>();
            lines.add(simpleLine.getLine());

            ArrayList<Maneuver> maneuvers = new ArrayList<>();
            maneuvers.add(new Maneuver(String.format(App.getContext().getResources().getString(R.string.bus_route_first_stop), busNetworkNodeBegin.getName(), simpleLine.getLabel()), 100));
            maneuvers.add(new Maneuver(String.format(App.getContext().getResources().getString(R.string.bus_route_last_stop), busNetworkNodeEnd.getName()), 120));

            ArrayList<BusNetworkRouteNode> nodes = findRouteNodesBetweenNetworkNodes(simpleLine, simpleLineID, busNetworkNodeBegin, busNetworkNodeEnd, useNightLines);
            ArrayList<BusLineWayPoint> wayPoints = new ArrayList<>();
            result = new BusRoute(busNetworkNodeBegin, busNetworkNodeEnd, nTransfers, lines, maneuvers, nodes, wayPoints);
            ArrayList<Integer> ways = new ArrayList<>();
            ways.add(Integer.valueOf(simpleLineID.split("/")[1]));
            result.setWays(ways);
        }
        return result;
    }

    public BusRoute findOneTransferBusRoute(ArrayList<BusNetworkNode> nodesNearBegin, ArrayList<BusNetworkNode> nodesNearEnd, boolean useNightLines) {
        ArrayList<BusRoute> routes = new ArrayList<>();
        BusRoute bestRoute = null;
        ArrayList<ArrayList<BusNetworkNode>> routeNetworkNodes = new ArrayList<>();

        for (int i = 0; i < nodesNearBegin.size(); i++) {
            for (int j = 0; j < nodesNearEnd.size(); j++) {
                BusNetworkNode networkNodeBegin = findBusNetworkNodeByNode(nodesNearBegin.get(i).getNode(), useNightLines);
                BusNetworkNode networkNodeEnd = findBusNetworkNodeByNode(nodesNearEnd.get(j).getNode(), useNightLines);
                for (String lineBeginId : networkNodeBegin.getLines()) {
                    for (String lineEndId : networkNodeEnd.getLines()) {
                        BusLine lineBegin, lineEnd;
                        lineBegin = findLineByLineId(Integer.valueOf(lineBeginId.split("/")[0]));
                        lineEnd = findLineByLineId(Integer.valueOf(lineEndId.split("/")[0]));
                        ArrayList<BusNetworkNode> nodesContainingLines = new ArrayList<>();
                        boolean timeMatch = (useNightLines && lineBegin.getGroupNumber().startsWith("3") && lineEnd.getGroupNumber().startsWith("3")) || (!useNightLines && !lineBegin.getGroupNumber().startsWith("3") && !lineEnd.getGroupNumber().startsWith("3"));
                        if (timeMatch){
                            nodesContainingLines = findBusNetworkNodeContainingLines(lineBeginId, lineEndId, useNightLines);
                            if (!nodesContainingLines.isEmpty()){
                                for(int k=0; k<nodesContainingLines.size(); k++){
                                    ArrayList<BusNetworkNode> aux = new ArrayList<>();

                                    int firstStopBeginIndex = findBusStopIndexByNetworkNodeNode(networkNodeBegin.getNode(), lineBegin, Integer.valueOf(lineBeginId.split("/")[1]));
                                    int lastStopBeginIndex = findBusStopIndexByNetworkNodeNode(nodesContainingLines.get(k).getNode(), lineBegin, Integer.valueOf(lineBeginId.split("/")[1]));

                                    int firstStopEndIndex = findBusStopIndexByNetworkNodeNode(nodesContainingLines.get(k).getNode(), lineEnd, Integer.valueOf(lineEndId.split("/")[1]));
                                    int lastStopEndIndex = findBusStopIndexByNetworkNodeNode(networkNodeEnd.getNode(), lineEnd, Integer.valueOf(lineEndId.split("/")[1]));

                                    if (firstStopBeginIndex<lastStopBeginIndex && firstStopEndIndex<lastStopEndIndex) {
                                        aux.add(networkNodeBegin);
                                        aux.add(nodesContainingLines.get(k));
                                        aux.add(networkNodeEnd);
                                        routeNetworkNodes.add(aux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        for (int k = 0; k < routeNetworkNodes.size(); k++) {
            ArrayList<BusNetworkRouteNode> nodes = new ArrayList<>();
            ArrayList<Integer> lines = new ArrayList<>();
            ArrayList<Maneuver> maneuvers = new ArrayList<>();
            ArrayList<BusLineWayPoint> wayPoints = new ArrayList<>();
            ArrayList<Integer> ways = new ArrayList<>();

            BusNetworkNode busNetworkNodeBegin = routeNetworkNodes.get(k).get(0);
            BusNetworkNode busNetworkNodeTransfer = routeNetworkNodes.get(k).get(1);
            BusNetworkNode busNetworkNodeEnd = routeNetworkNodes.get(k).get(2);

            String busLineID = "";
            for (String beginNodeLineID : busNetworkNodeBegin.getLines()) {
                for (String transferNodeLineID : busNetworkNodeTransfer.getLines()) {
                    if (beginNodeLineID.equals(transferNodeLineID)) {
                        busLineID = beginNodeLineID;
                    }
                }
            }
            BusLine busLine = findLineByLineId(Integer.valueOf(busLineID.split("/")[0]));
            lines.add(Integer.valueOf(busLineID.split("/")[0]));
            ways.add(Integer.valueOf(busLineID.split("/")[1]));
            nodes.addAll(findRouteNodesBetweenNetworkNodes(busLine, busLineID, busNetworkNodeBegin, busNetworkNodeTransfer, useNightLines));
            maneuvers.add(new Maneuver(String.format(App.getContext().getResources().getString(R.string.bus_route_first_stop), busNetworkNodeBegin.getName(), busLine.getLabel()), 100));
            nodes.get(nodes.size() - 1).setIsTransferNode(true);

            for (String endNodeLineID : busNetworkNodeEnd.getLines()) {
                for (String transferNodeLineID : busNetworkNodeTransfer.getLines()) {
                    if (endNodeLineID.equals(transferNodeLineID)) {
                        busLineID = endNodeLineID;
                    }
                }
            }
            lines.add(Integer.valueOf(busLineID.split("/")[0]));
            ways.add(Integer.valueOf(busLineID.split("/")[1]));
            busLine = findLineByLineId(Integer.valueOf(busLineID.split("/")[0]));
            ArrayList<BusNetworkRouteNode> aux = findRouteNodesBetweenNetworkNodes(busLine, busLineID, busNetworkNodeTransfer, busNetworkNodeEnd, useNightLines);
            for (int l = 0; l < aux.size(); l++) {
                if (l != 0) {
                    nodes.add(aux.get(l));
                }
            }

            maneuvers.add(new Maneuver(String.format(App.getContext().getResources().getString(R.string.bus_route_transfer_stop), busNetworkNodeTransfer.getName(), busLine.getLabel()), 110));
            maneuvers.add(new Maneuver(String.format(App.getContext().getResources().getString(R.string.bus_route_last_stop), busNetworkNodeEnd.getName()), 120));

            BusRoute route = new BusRoute(busNetworkNodeBegin, busNetworkNodeEnd, 1, lines, maneuvers, nodes, wayPoints);
            route.setWays(ways);
            routes.add(route);
        }

        for (int i = 0; i < routes.size(); i++) {
            if (bestRoute == null) {
                bestRoute = routes.get(i);
            } else {
                if (bestRoute.getNodes().size() > routes.get(i).getNodes().size()) {
                    bestRoute = routes.get(i);
                }
            }
        }

        return bestRoute;
    }

    public BusRoute findNTransferBusRoute(final BusNetworkNode firstNode, BusNetworkNode lastNode, boolean useNightLines) {
        BusRoute result = null;

        ArrayList<Boolean> statesList = new ArrayList<>();
        ArrayList<Integer> distancesList = new ArrayList<>();
        ArrayList<Integer> parentIdsList = new ArrayList<>();

        // Initialize lists
        ArrayList<BusNetworkNode> nodes = new ArrayList<>();
        if (!useNightLines){
            nodes = getBusNetworkDayNodes();
        } else {
            nodes = getBusNetworkNightNodes();
        }
        for (BusNetworkNode busNetworkNode : nodes) {
            statesList.add(false);
            distancesList.add(Integer.MAX_VALUE);
            parentIdsList.add(-1);
        }

        // First stop
        statesList.set(nodes.indexOf(firstNode), true);
        distancesList.set(nodes.indexOf(firstNode), 0);
        parentIdsList.set(nodes.indexOf(firstNode), firstNode.getNode());

        Queue<Integer> stopsQueue = new LinkedList<>();
        stopsQueue.clear();
        stopsQueue.add(firstNode.getNode());

        int auxPos = 0;
        int auxId = -1;

        while (!stopsQueue.isEmpty()) {
            auxId = stopsQueue.remove();
            auxPos = nodes.indexOf(findBusNetworkNodeByNode(auxId, useNightLines));
            ArrayList<BusNetworkNode> adjacentNodes = new ArrayList<>();
            for (int i = 0; i < findBusNetworkNodeByNode(auxId, useNightLines).getNextNodes().size(); i++) {
                BusNetworkNode adjacentNode = findBusNetworkNodeByNode(findBusNetworkNodeByNode(auxId, useNightLines).getNextNodes().get(i), useNightLines);
                if (adjacentNode != null){
                    adjacentNodes.add(adjacentNode);
                }
            }
            for (BusNetworkNode adjacentNode : adjacentNodes) {
                int posAdj = nodes.indexOf(findBusNetworkNodeByNode(adjacentNode.getNode(), useNightLines));
                if (!statesList.get(posAdj)) {
                    statesList.set(posAdj, true);
                    distancesList.set(posAdj, distancesList.get(auxPos) + 1);
                    parentIdsList.set(posAdj, auxId);
                    stopsQueue.add(adjacentNode.getNode());
                }
            }
        }

        LinkedList<BusNetworkNode> results = new LinkedList<>();

        int auxParent;
        int auxPosition;
        BusNetworkNode current = lastNode;

        while (current != null && !current.equals(firstNode)) {
            results.addFirst(current);
            auxPosition = nodes.indexOf(findBusNetworkNodeByNode(current.getNode(), useNightLines));
            auxParent = parentIdsList.get(auxPosition);
            current = findBusNetworkNodeByNode(auxParent, useNightLines);
        }

        if (current == null) {
            return null;
        }

        results.addFirst(firstNode);

        final ArrayList<BusNetworkRouteNode> routeNodes = new ArrayList<>();
        // Convert from network node list to route node
        for (BusNetworkNode networkNode : results) {
            routeNodes.add(new BusNetworkRouteNode(networkNode.getNode(), networkNode.getName(), networkNode.getLines(), networkNode.getLatitude(), networkNode.getLongitude(), false));
        }

        final ArrayList<BusLineWayPoint> wayPoints = new ArrayList<>();
        ArrayList<Maneuver> maneuvers = new ArrayList<>();
        int nTransfers = 0;
        int i = 0;
        final ArrayList<Integer> routeNodesLines = new ArrayList<>();
        do {
            int bestLineId = Integer.valueOf(routeNodes.get(i).getLines().get(0).split("/")[0]);
            int bestNumStops = 1;
            for (String lineId : routeNodes.get(i).getLines()) {
                int numStops = 1;
                int j = i + 1;
                boolean isTransfer = false;
                do {
                    isTransfer = !routeNodes.get(j).getLines().contains(lineId);
                    if (!isTransfer) {
                        numStops++;
                        j++;
                    }
                } while (j < routeNodes.size() && !isTransfer);
                if (numStops > bestNumStops) {
                    bestLineId = Integer.valueOf(lineId.split("/")[0]);
                    bestNumStops = numStops;
                }
            }
            BusLine bestLine = findLineByLineId(bestLineId);
            routeNodesLines.add(bestLine.getLine());

            String narrative = new String();
            int turnType = 0;
            if (i == 0) {
                narrative = String.format(App.getContext().getResources().getString(R.string.bus_route_first_stop), routeNodes.get(i).getName(), bestLine.getLabel());
                turnType = 100;
            } else {
                narrative = String.format(App.getContext().getResources().getString(R.string.bus_route_transfer_stop), routeNodes.get(i).getName(), bestLine.getLabel());
                turnType = 110;
                routeNodes.get(i).setIsTransferNode(true);
                nTransfers++;
            }
            maneuvers.add(new Maneuver(narrative, turnType));

            i = i + bestNumStops - 1;
        } while (i < routeNodes.size() - 1);

        String lastManeuverNarrative = String.format(App.getContext().getResources().getString(R.string.bus_route_last_stop), routeNodes.get(routeNodes.size() - 1).getName());
        maneuvers.add(new Maneuver(lastManeuverNarrative, 120));

        result = new BusRoute(firstNode, lastNode, nTransfers, routeNodesLines, maneuvers, routeNodes, wayPoints);

        return result;

    }

    public ArrayList<BusNetworkNode> findNClosestBusNetworkNodes(int n, GeocodingLocation geocodingLocation, boolean useNightLines){
        ArrayList<BusNetworkNode> results = new ArrayList<>();

        ArrayList<BusNetworkNode> nodes;
        if (useNightLines){
            nodes = getBusNetworkNightNodes();
        } else {
            nodes = getBusNetworkDayNodes();
        }
        Location location = new Location("");
        location.setLatitude(geocodingLocation.getLatitude());
        location.setLongitude(geocodingLocation.getLongitude());
        for(BusNetworkNode node : nodes){
            if (results.size() < n){
                results.add(node);
            } else {
                results.add(node);
                int i = 0;
                while (i<results.size()-1){
                    Location locationA = new Location("");
                    locationA.setLatitude(results.get(i).getLatitude());
                    locationA.setLongitude(results.get(i).getLongitude());
                    Location locationB = new Location("");
                    locationB.setLatitude(results.get(i + 1).getLatitude());
                    locationB.setLongitude(results.get(i + 1).getLongitude());
                    if (locationA.distanceTo(location) > locationB.distanceTo(location)){
                        BusNetworkNode aux = results.get(i);
                        results.set(i, results.get(i+1));
                        results.set(i+1, aux);
                    } else {
                        i++;
                    }
                }
                results.remove(results.size()-1);
            }
        }

        return results;
    }

}
