package com.jmartinal.madridbusgps.WSEMTOpenData;

import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusLineStop;
import com.jmartinal.madridbusgps.Model.BusLineWayPoint;
import com.jmartinal.madridbusgps.Model.BusNetworkNode;
import com.jmartinal.madridbusgps.Model.BusSchedule;
import com.jmartinal.madridbusgps.Model.BusScheduleDayType;
import com.jmartinal.madridbusgps.Model.BusScheduleDayTypeDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jorge on 30/01/2016.
 */
public class EMTOpenDataParser {

    public static BusSchedule parseInfoLineExtend(JSONObject serverResponse){
        JSONObject line = null;
        BusSchedule parsedResultValue = null;
        try {
            line = serverResponse.has("Line") ? serverResponse.getJSONObject("Line") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (line != null) {
            try {
                long date = line.has("date") ? line.getLong("date") : 0;
                int lineId = line.has("lineId") ? line.getInt("lineId") : 0;
                String label = line.has("label") ? line.getString("label") : "";
                String headerA = line.has("headerA") ? line.getString("headerA") : "";
                String headerB = line.has("headerB") ? line.getString("headerB") : "";
                int incidents = line.has("incidents") ? line.getInt("incidents") : 0;
                JSONArray dayTypesJSONArray = line.has("dayType") ? line.getJSONArray("dayType") : null;
                ArrayList<BusScheduleDayType> dayTypes = new ArrayList<>();
                for(int i=0; i<dayTypesJSONArray.length(); i++){
                    JSONObject dayTypeJSONObject = dayTypesJSONArray.getJSONObject(i);
                    String dayTypeId = dayTypeJSONObject.has("dayTypeId") ? dayTypeJSONObject.getString("dayTypeId") : "";
                    JSONObject direction1JSONObject = dayTypeJSONObject.has("direction1") ? dayTypeJSONObject.getJSONObject("direction1") : null;
                    String startTime1 = direction1JSONObject.has("startTime") ? direction1JSONObject.getString("startTime") : "";
                    String stopTime1 = direction1JSONObject.has("stopTime") ? direction1JSONObject.getString("stopTime") : "";
                    int minimumFrequency1 = direction1JSONObject.has("minimumFrequency") ? direction1JSONObject.getInt("minimumFrequency") : 0;
                    int maximumFrequency1 = direction1JSONObject.has("maximumFrequency") ? direction1JSONObject.getInt("maximumFrequency") : 0;
                    String frequencyDescription1 = direction1JSONObject.has("frequencyDescription") ? direction1JSONObject.getString("frequencyDescription") : "";
                    BusScheduleDayTypeDirection direction1 = new BusScheduleDayTypeDirection(startTime1, stopTime1, minimumFrequency1, maximumFrequency1, frequencyDescription1);
                    JSONObject direction2JSONObject = dayTypeJSONObject.has("direction2") ? dayTypeJSONObject.getJSONObject("direction2") : null;
                    String startTime2 = direction2JSONObject.has("startTime") ? direction2JSONObject.getString("startTime") : "";
                    String stopTime2 = direction2JSONObject.has("stopTime") ? direction2JSONObject.getString("stopTime") : "";
                    int minimumFrequency2 = direction2JSONObject.has("minimumFrequency") ? direction2JSONObject.getInt("minimumFrequency") : 0;
                    int maximumFrequency2 = direction2JSONObject.has("maximumFrequency") ? direction2JSONObject.getInt("maximumFrequency") : 0;
                    String frequencyDescription2 = direction2JSONObject.has("frequencyDescription") ? direction2JSONObject.getString("frequencyDescription") : "";
                    BusScheduleDayTypeDirection direction2 = new BusScheduleDayTypeDirection(startTime2, stopTime2, minimumFrequency2, maximumFrequency2, frequencyDescription2);
                    dayTypes.add(new BusScheduleDayType(dayTypeId, direction1, direction2));
                }
                parsedResultValue = new BusSchedule(date, lineId, label, headerA, headerB, incidents, dayTypes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedResultValue;
    }

    public static ArrayList<BusLine> parseListLines(JSONObject serverResponse) {
        String resultCode = "";
        String resultDescription = "";
        JSONArray resultValues = null;
        ArrayList<BusLine> parsedResultValues = null;
        try {
            resultCode = serverResponse.has("resultCode") ? serverResponse.getString("resultCode") : "";
            resultDescription = serverResponse.has("resultDescription") ? serverResponse.getString("resultDescription") : "";
            resultValues = serverResponse.has("resultValues") ? serverResponse.getJSONArray("resultValues") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (resultValues != null) {
            try {
                parsedResultValues = new ArrayList<>();
                for (int i = 0; i < resultValues.length(); i++) {
                    JSONObject resultValue = resultValues.getJSONObject(i);
                    String groupNumber = resultValue.has("groupNumber") ? resultValue.getString("groupNumber") : "";
                    String dateFirst = resultValue.has("dateFirst") ? resultValue.getString("dateFirst").replace(" ", "") : "";
                    String dateEnd = resultValue.has("dateEnd") ? resultValue.getString("dateEnd").replace(" ", "") : "";
                    int line = resultValue.has("line") ? Integer.valueOf(resultValue.getString("line")) : -1;
                    String label = resultValue.has("label") ? resultValue.getString("label") : "";
                    String nameA = resultValue.has("nameA") ? resultValue.getString("nameA") : "";
                    String nameB = resultValue.has("nameB") ? resultValue.getString("nameB") : "";
                    BusLine busLine = new BusLine(groupNumber, dateFirst, dateEnd, line, label, nameA, nameB);
                    parsedResultValues.add(busLine);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedResultValues;
    }

    public static ArrayList<BusNetworkNode> parseNodesLines(JSONObject serverResponse) {
        String resultCode = "";
        String resultDescription = "";
        JSONArray resultValues = null;
        ArrayList<BusNetworkNode> parsedResultValues = null;
        try {
            resultCode = serverResponse.has("resultCode") ? serverResponse.getString("resultCode") : "";
            resultDescription = serverResponse.has("resultDescription") ? serverResponse.getString("resultDescription") : "";
            resultValues = serverResponse.has("resultValues") ? serverResponse.getJSONArray("resultValues") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (resultValues != null){
            try {
                parsedResultValues = new ArrayList<>();
                for (int i=0; i<resultValues.length(); i++){
                    JSONObject resultValue = resultValues.getJSONObject(i);
                    int node = resultValue.has("node") ? resultValue.getInt("node") : -1;
                    String name = resultValue.has("name") ? resultValue.getString("name") : "";
                    JSONArray linesArray = resultValue.has("lines") ? resultValue.getJSONArray("lines") : null;
                    ArrayList<String> lines = new ArrayList<>();
                    if (linesArray != null){
                        for (int j=0; j<linesArray.length(); j++){
                            if (!linesArray.get(j).equals("")) {
                                lines.add(linesArray.getString(j));
                            }
                        }
                    }
                    double latitude = resultValue.has("latitude") ? resultValue.getDouble("latitude") : 0.0;
                    double longitude = resultValue.has("latitude") ? resultValue.getDouble("longitude") : 0.0;
                    parsedResultValues.add(new BusNetworkNode(node, name, lines, latitude, longitude));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedResultValues;    }

    public static ArrayList<BusLineStop> parseRouteLines(JSONObject serverResponse) {
        String resultCode = "";
        String resultDescription = "";
        JSONArray resultValues = null;
        ArrayList<BusLineStop> parsedResultValues = null;
        try {
            resultCode = serverResponse.has("resultCode") ? serverResponse.getString("resultCode") : "";
            resultDescription = serverResponse.has("resultDescription") ? serverResponse.getString("resultDescription") : "";
            resultValues = serverResponse.has("resultValues") ? serverResponse.getJSONArray("resultValues") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (resultValues != null){
            try {
                parsedResultValues = new ArrayList<>();
                for (int i=0; i<resultValues.length(); i++){
                    JSONObject resultValue = resultValues.getJSONObject(i);
                    int line = resultValue.has("line") ? resultValue.getInt("line") : -1;
                    int secDetail = resultValue.has("secDetail") ? resultValue.getInt("secDetail") : -1;
                    String orderDetail = resultValue.has("orderDetail") ? resultValue.getString("orderDetail") : "";
                    int node = resultValue.has("node") ? resultValue.getInt("node") : -1;
                    String name = resultValue.has("name") ? resultValue.getString("name") : "";
                    int distance = resultValue.has("distance") ? resultValue.getInt("distance") : -1;;
                    int distancePreviousStop = resultValue.has("distancePreviousStop") ? resultValue.getInt("distancePreviousStop") : -1;
                    double latitude = resultValue.has("latitude") ? resultValue.getDouble("latitude") : -1;
                    double longitude = resultValue.has("longitude") ? resultValue.getDouble("longitude") : -1;
                    parsedResultValues.add(new BusLineStop(line, secDetail, orderDetail, node, distance, distancePreviousStop, name, latitude, longitude));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedResultValues;
    }

    public static ArrayList<BusLineWayPoint> parseRouteLinesRoute (JSONObject serverResponse){
        String resultCode = "";
        String resultDescription = "";
        JSONArray resultValues = null;
        ArrayList<BusLineWayPoint> parsedResultValues = null;
        try {
            resultCode = serverResponse.has("resultCode") ? serverResponse.getString("resultCode") : "";
            resultDescription = serverResponse.has("resultDescription") ? serverResponse.getString("resultDescription") : "";
            resultValues = serverResponse.has("resultValues") ? serverResponse.getJSONArray("resultValues") : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (resultValues != null){
            try {
                parsedResultValues = new ArrayList<>();
                for (int i=0; i<resultValues.length(); i++){
                    JSONObject resultValue = resultValues.getJSONObject(i);
                    int line = resultValue.has("line") ? resultValue.getInt("line") : -1;
                    int secDetail = resultValue.has("secDetail") ? resultValue.getInt("secDetail") : -1;
                    String orderDetail = resultValue.has("orderDetail") ? resultValue.getString("orderDetail") : "";
                    String node = resultValue.has("node") ? resultValue.getString("node") : "";
                    String name = resultValue.has("name") ? resultValue.getString("name") : "";
                    int distance = resultValue.has("distance") ? resultValue.getInt("distance") : -1;;
                    int distancePreviousStop = resultValue.has("distancePreviousStop") ? resultValue.getInt("distancePreviousStop") : -1;
                    double latitude = resultValue.has("latitude") ? resultValue.getDouble("latitude") : -1;
                    double longitude = resultValue.has("longitude") ? resultValue.getDouble("longitude") : -1;
                    parsedResultValues.add(new BusLineWayPoint(line, secDetail, orderDetail, node, name, distance, distancePreviousStop, latitude, longitude));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedResultValues;
    }
}
