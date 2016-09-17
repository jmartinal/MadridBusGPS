package com.jmartinal.madridbusgps.WSMapQuest;


import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.jmartinal.madridbusgps.Model.DirectionsInfo;
import com.jmartinal.madridbusgps.Model.DirectionsLeg;
import com.jmartinal.madridbusgps.Model.DirectionsManeuver;
import com.jmartinal.madridbusgps.Model.DirectionsOptions;
import com.jmartinal.madridbusgps.Model.DirectionsRoute;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;

/**
 * Created by Jorge on 14/07/2015.
 */
public class JSONDirectionsParser {

    public static JSONObject getJSONObject(InputStream inputStream){
        JSONObject result = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            result = new JSONObject(responseStrBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static DirectionsInfo parseInfo(JSONObject serverResponse){
        JSONObject info = null;
        DirectionsInfo parsedInfo = null;
        try{
            info = (JSONObject) serverResponse.get("info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (info != null){
            try {
                JSONObject copyright = info.getJSONObject("copyright");
                JSONArray msgs = (JSONArray)info.get("messages");

                int statusCode = info.getInt("statuscode");
                String copyrightText = copyright.getString("text");
                String copyrightImageURL = copyright.getString("imageUrl");
                String copyrightImageAltText = copyright.getString("imageAltText");
                ArrayList<String> messages = new ArrayList<String>();
                for(int i=0; i<msgs.length(); i++){
                    messages.add(msgs.get(i).toString());
                }

                parsedInfo = new DirectionsInfo(statusCode, copyrightText, copyrightImageURL, copyrightImageAltText, messages);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedInfo;
    }

    public static DirectionsRoute parseRoute(JSONObject serverResponse){
        JSONObject route = null;
        DirectionsRoute parsedRoute = null;
        try {
            route = serverResponse.getJSONObject("route");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (route != null){
            try {
                boolean hasTollRoad = route.has("hasTollRoad") ? route.getBoolean("hasTollRoad") : false;
                int fuelUsed = route.has("fuelUsed") ? route.getInt("fuelUsed") : 0;
                ArrayList<Integer> maneuverIndexes = null;
                ArrayList<LatLng> shapePoints = null;
                ArrayList<Integer> legIndexes = null;
                if (route.has("shape")){
                    JSONObject shape = route.getJSONObject("shape");
                    JSONArray maneuverIndexesJSONArray = shape.has("maneuverIndexes") ? shape.getJSONArray("maneuverIndexes") : null;
                    maneuverIndexes = new ArrayList<>();
                    for (int i=0; i<maneuverIndexesJSONArray.length(); i++){
                        maneuverIndexes.add(maneuverIndexesJSONArray.getInt(i));
                    }
                    JSONArray shapePointsJSONArray = shape.has("shapePoints") ? shape.getJSONArray("shapePoints") : null;
                    shapePoints = new ArrayList<>();
                    for (int i=0; i<shapePointsJSONArray.length()-1; i=i+2){
                        shapePoints.add(new LatLng(shapePointsJSONArray.getDouble(i), shapePointsJSONArray.getDouble(i+1)));
                    }
                    JSONArray legIndexesJSONArray = shape.has("legIndexes") ? shape.getJSONArray("legIndexes") : null;
                    legIndexes = new ArrayList<>();
                    for (int i=0; i<legIndexesJSONArray.length(); i++){
                        legIndexes.add(legIndexesJSONArray.getInt(i));
                    }
                }
                boolean hasUnpaved = route.has("hasUnpaved") ? route.getBoolean("hasUnpaved") : false;
                boolean hasHighway = route.has("hasHighway") ? route.getBoolean("hasHighway") : false;
                int realTime = route.has("realTime") ? route.getInt("realTime") : 0;
                LatLng boundingBoxUL = null;
                LatLng boundingBoxLR = null;
                if (route.has("boundingBox")){
                    JSONObject boundingBox = route.getJSONObject("boundingBox");
                    JSONObject ulJSONObject = boundingBox.getJSONObject("ul");
                    JSONObject lrJSONObject = boundingBox.getJSONObject("lr");
                    boundingBoxUL = new LatLng(ulJSONObject.getDouble("lat"), ulJSONObject.getDouble("lng"));
                    boundingBoxLR = new LatLng(lrJSONObject.getDouble("lat"), ulJSONObject.getDouble("lng"));
                }
                double distance = route.has("distance") ? route.getDouble("distance") : 0.0;
                int time = route.has("time") ? route.getInt("time") : 0;
                ArrayList<Integer> locationSequence = null;
                if (route.has("locationSequence")){
                    JSONArray locationSequenceJSONArray = route.getJSONArray("locationSequence");
                    locationSequence = new ArrayList<>();
                    for (int i=0; i<locationSequenceJSONArray.length(); i++){
                        locationSequence.add(locationSequenceJSONArray.getInt(i));
                    }
                }
                boolean hasSeasonalClosure = route.has("hasSeasonalClosure") ? route.getBoolean("hasSeasonalClosure") : false;
                String sessionId = route.has("sessionId") ? route.getString("sessionId") : "";
                ArrayList<GeocodingLocation> locations = null;
                if (route.has("locations")){
                    locations = new ArrayList<>();
                    JSONArray locationsJSONArray = route.getJSONArray("locations");
                    for (int i=0; i< locationsJSONArray.length(); i++){
                        GeocodingLocation location = JSONGeocodingParser.parseLocation(locationsJSONArray.getJSONObject(i));
                        locations.add(location);
                    }
                }
                boolean hasCountryCross = route.has("hasCountryCross") ? route.getBoolean("hasCountryCross") : false;
                ArrayList<DirectionsLeg> legs = route.has("legs") ? parseLegs(route.getJSONArray("legs")) : null;
                String formattedTime = route.has("formattedTime") ? route.getString("formattedTime") : "";

                String routeErrMessage = "";
                int routeErrErrorCode = 0;
                if (route.has("routeError")) {
                    JSONObject routeError = route.getJSONObject("routeError");
                    routeErrMessage = routeError.has("message") ? routeError.getString("message") : "";
                    routeErrErrorCode = routeError.has("errorCode") ? routeError.getInt("errorCode") : 0;
                }
                DirectionsOptions options = route.has("options") ? parseOptions(route.getJSONObject("options")) : null;
                boolean hasFerry = route.has("hasFerry") ? route.getBoolean("hasFerry") : false;


                parsedRoute = new DirectionsRoute(hasTollRoad, fuelUsed, maneuverIndexes, shapePoints, legIndexes, hasUnpaved,
                        hasHighway, realTime, boundingBoxUL, boundingBoxLR, distance, time, locationSequence,
                        hasSeasonalClosure, sessionId, locations, hasCountryCross, legs, formattedTime, routeErrMessage,
                        routeErrErrorCode, options, hasFerry);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedRoute;
    }

    private static ArrayList<DirectionsLeg> parseLegs(JSONArray routeLegs) {
        ArrayList<DirectionsLeg> parsedLegs = null;
        for (int i=0; i<routeLegs.length(); i++){
            try {
                parsedLegs = new ArrayList<>();
                JSONObject routeLeg = routeLegs.getJSONObject(i);
                boolean hasTollRoad = routeLeg.has("hasTollRoad") ? routeLeg.getBoolean("hasTollRoad") : false;
                int index = routeLeg.has("index") ? routeLeg.getInt("index") : 0;
                String roadGradeStrategy = "";
                boolean hasHighway = routeLeg.has("hasHighway") ? routeLeg.getBoolean("hasHighway") : false;
                boolean hasUnpaved = routeLeg.has("hasUnpaved") ? routeLeg.getBoolean("hasUnpaved") : false;
                double distance = routeLeg.has("distance") ? routeLeg.getDouble("distance") : 0.0;
                int time = routeLeg.has("time") ? routeLeg.getInt("time") : 0;
                int orgIndex = routeLeg.has("orgIndex") ? routeLeg.getInt("orgIndex") : 0;
                boolean hasSeasonalClosure = routeLeg.has("hasSeasonalClosure") ? routeLeg.getBoolean("hasSeasonalClosure") : false;
                String origNarrative = routeLeg.has("origNarrative") ? routeLeg.getString("origNarrative") : "";
                boolean hasCountryCross = routeLeg.has("hasCountryCross") ? routeLeg.getBoolean("hasCountryCross") : false;
                String formattedTime = routeLeg.has("formattedTime") ? routeLeg.getString("formattedTime") : "";
                String destNarrative = routeLeg.has("destNarrative") ? routeLeg.getString("destNarrative") : "";
                int destIndex = routeLeg.has("destIndex") ? routeLeg.getInt("destIndex") : 0;
                ArrayList<DirectionsManeuver> maneuvers = null;
                if (routeLeg.has("maneuvers")){
                    maneuvers = new ArrayList<>();
                    JSONArray routeLegManeuvers = routeLeg.getJSONArray("maneuvers");
                    for (int j=0; j<routeLegManeuvers.length(); j++){
                        JSONObject maneuver = routeLegManeuvers.getJSONObject(j);
                        ArrayList<String> maneuverSigns = null;
                        if (maneuver.has("signs")){
                            maneuverSigns = new ArrayList<>();
                            JSONArray maneuverSignsJSONArray = maneuver.getJSONArray("signs");
                            for (int k=0; k<maneuverSignsJSONArray.length(); k++){
                                maneuverSigns.add(maneuverSignsJSONArray.getString(k));
                            }
                        }
                        int maneuverIndex = maneuver.has("index") ? maneuver.getInt("index") : 0;
                        ArrayList<String> maneuverNotes = null;
                        if (maneuver.has("maneuverNotes")){
                            maneuverNotes = new ArrayList<>();
                            JSONArray maneuverNotesJSONArray = maneuver.getJSONArray("maneuverNotes");
                            for (int k=0; k<maneuverNotesJSONArray.length(); k++){
                                maneuverNotes.add(maneuverNotesJSONArray.getString(k));
                            }
                        }
                        int maneuverDirection = maneuver.has("direction") ? maneuver.getInt("direction") : 0;

                        String maneuverNarrative = "";
                        if (maneuver.has("narrative")){
                            maneuverNarrative = maneuver.getString("narrative");
                            maneuverNarrative = maneuverNarrative.contains("Gire izquierda") ? maneuverNarrative.replace("Gire izquierda", "Gire a la izquierda") : maneuverNarrative;
                            maneuverNarrative = maneuverNarrative.contains("Gire derecha") ? maneuverNarrative.replace("Gire derecha", "Gire a la derecha") : maneuverNarrative;
                            maneuverNarrative = maneuverNarrative.contains("Gire a la ligeramente") ? maneuverNarrative.replace("Gire a la ligeramente", "Gire ligeramente") : maneuverNarrative;
                        }

                        String maneuverIconUrl = maneuver.has("iconUrl") ? maneuver.getString("iconUrl") : "";
                        double maneuverDistance = maneuver.has("distance") ? maneuver.getDouble("distance") : 0.0;
                        int maneuverTime = maneuver.has("time") ? maneuver.getInt("time") : 0;
                        ArrayList<Object> maneuverlinkIds = null;
                        if (maneuver.has("linkIds")){
                            maneuverlinkIds = new ArrayList<>();
                            JSONArray maneuverlinkIdsJSONArray = maneuver.getJSONArray("linkIds");
                            for (int k=0; k<maneuverlinkIdsJSONArray.length(); k++){
                                maneuverlinkIds.add(maneuverlinkIdsJSONArray.get(k));
                            }
                        }
                        ArrayList<String> maneuverStreets = null;
                        if (maneuver.has("streets")){
                            maneuverStreets = new ArrayList<>();
                            JSONArray maneuverStreetsJSONArray = maneuver.getJSONArray("streets");
                            for (int k=0; k<maneuverStreetsJSONArray.length(); k++){
                                maneuverStreets.add(maneuverStreetsJSONArray.getString(k));
                            }
                        }
                        int maneuverAttributes = maneuver.has("attributes") ? maneuver.getInt("attributes") : 0;
                        String maneuverTransportMode = maneuver.has("transportMode") ? maneuver.getString("transportMode") : "";
                        String maneuverFormattedTime = maneuver.has("formattedTime") ? maneuver.getString("formattedTime") : "";
                        String maneuverDirectionName = maneuver.has("directionName") ? maneuver.getString("directionName") : "";
                        String maneuverMapUrl = maneuver.has("mapUrl") ? maneuver.getString("mapUrl") : "";
                        LatLng maneuverStartPoint = null;
                        if (maneuver.has("startPoint")){
                            JSONObject maneuverStartPointJSONObject = maneuver.getJSONObject("startPoint");
                            double lat = maneuverStartPointJSONObject.has("lat") ? maneuverStartPointJSONObject.getDouble("lat") : 0.0;
                            double lng = maneuverStartPointJSONObject.has("lng") ? maneuverStartPointJSONObject.getDouble("lng") : 0.0;
                            maneuverStartPoint = new LatLng(lat, lng);
                        }
                        int maneuverTurnType = maneuver.has("turnType") ? maneuver.getInt("turnType") : 0;

                        maneuvers.add(new DirectionsManeuver(maneuverSigns, maneuverIndex, maneuverNotes, maneuverDirection,
                                maneuverNarrative, maneuverIconUrl, maneuverDistance, maneuverTime, maneuverlinkIds,
                                maneuverStreets, maneuverAttributes, maneuverTransportMode, maneuverFormattedTime, maneuverDirectionName,
                                maneuverMapUrl, maneuverStartPoint, maneuverTurnType));
                    }
                }
                boolean hasFerry = routeLeg.has("hasFerry") ? routeLeg.getBoolean("hasFerry") : false;

                parsedLegs.add(new DirectionsLeg(hasTollRoad, index, roadGradeStrategy, hasHighway, hasUnpaved, distance, time,
                        orgIndex, hasSeasonalClosure, origNarrative, hasCountryCross, formattedTime, destNarrative,
                        destIndex, maneuvers, hasFerry));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return parsedLegs;
    }

    private static DirectionsOptions parseOptions(JSONObject routeOptions) {
        DirectionsOptions parsedOptions = null;
            try {
                ArrayList<Object> mustAvoidLinkIds = null;
                if (routeOptions.has("linkIds")){
                    mustAvoidLinkIds = new ArrayList<>();
                    JSONArray mustAvoidLinkIdsJSONArray = routeOptions.getJSONArray("linkIds");
                    for (int k=0; k<mustAvoidLinkIdsJSONArray.length(); k++){
                        mustAvoidLinkIds.add(mustAvoidLinkIdsJSONArray.get(k));
                    }
                }
                int drivingStyle = routeOptions.has("drivingStyle") ? routeOptions.getInt("drivingStyle") : 0;
                boolean countryBoundaryDisplay = routeOptions.has("countryBoundaryDisplay") ? routeOptions.getBoolean("countryBoundaryDisplay") : false;
                int generalize = routeOptions.has("generalize") ? routeOptions.getInt("generalize") : 0;
                String narrativeType = routeOptions.has("narrativeType") ? routeOptions.getString("narrativeType") : "";
                String locale = routeOptions.has("locale") ? routeOptions.getString("locale") : "";
                boolean avoidTimedConditions = routeOptions.has("avoidTimedConditions") ? routeOptions.getBoolean("avoidTimedConditions") : false;
                boolean destinationManeuverDisplay = routeOptions.has("destinationManeuverDisplay") ? routeOptions.getBoolean("destinationManeuverDisplay") : false;
                boolean enhancedNarrative = routeOptions.has("enhancedNarrative") ? routeOptions.getBoolean("enhancedNarrative") : false;
                int filterZoneFactor = routeOptions.has("filterZoneFactor") ? routeOptions.getInt("filterZoneFactor") : 0;
                int timeType = routeOptions.has("timeType") ? routeOptions.getInt("timeType") : 0;
                double maxWalkingDistance = routeOptions.has("maxWalkingDistance") ? routeOptions.getDouble("maxWalkingDistance") : 0.0;
                String routeType = routeOptions.has("routeType") ? routeOptions.getString("routeType") : "";
                int transferPenalty = routeOptions.has("transferPenalty") ? routeOptions.getInt("transferPenalty") : 0;
                boolean stateBoundaryDisplay = routeOptions.has("stateBoundaryDisplay") ? routeOptions.getBoolean("stateBoundaryDisplay") : false;
                double walkingSpeed = routeOptions.has("walkingSpeed") ? routeOptions.getDouble("walkingSpeed") : 0.0;
                int maxLinkId = routeOptions.has("maxLinkId") ? routeOptions.getInt("maxLinkId") : 0;
                ArrayList<Object> arteryWeights = null;
                if (routeOptions.has("arteryWeights")){
                    arteryWeights = new ArrayList<>();
                    JSONArray arteryWeightsJSONArray = routeOptions.getJSONArray("arteryWeights");
                    for (int k=0; k<arteryWeightsJSONArray.length(); k++){
                        arteryWeights.add(arteryWeightsJSONArray.get(k));
                    }
                }
                ArrayList<Object> tryAvoidLinkIds = null;
                if (routeOptions.has("tryAvoidLinkIds")){
                    tryAvoidLinkIds = new ArrayList<>();
                    JSONArray tryAvoidLinkIdsJSONArray = routeOptions.getJSONArray("tryAvoidLinkIds");
                    for (int k=0; k<tryAvoidLinkIdsJSONArray.length(); k++){
                        tryAvoidLinkIds.add(tryAvoidLinkIdsJSONArray.get(k));
                    }
                }
                String unit = routeOptions.has("unit") ? routeOptions.getString("unit") : "";
                int routeNumber = routeOptions.has("routeNumber") ? routeOptions.getInt("routeNumber") : 0;
                String shapeFormat = routeOptions.has("shapeFormat") ? routeOptions.getString("shapeFormat") : "";
                int maneuverPenalty = routeOptions.has("maneuverPenalty") ? routeOptions.getInt("maneuverPenalty") : 0;
                boolean useTraffic = routeOptions.has("useTraffic") ? routeOptions.getBoolean("useTraffic") : false;
                boolean returnLinkDirections = routeOptions.has("returnLinkDirections") ? routeOptions.getBoolean("returnLinkDirections") : false;
                ArrayList<Object> avoidTripIds = null;
                if (routeOptions.has("avoidTripIds")){
                    avoidTripIds = new ArrayList<>();
                    JSONArray avoidTripIdsJSONArray = routeOptions.getJSONArray("avoidTripIds");
                    for (int k=0; k<avoidTripIdsJSONArray.length(); k++){
                        avoidTripIds.add(avoidTripIdsJSONArray.get(k));
                    }
                }
                boolean manmaps = routeOptions.has("manmaps") ? routeOptions.getBoolean("manmaps") : false;
                int highwayEfficiency = routeOptions.has("highwayEfficiency") ? routeOptions.getInt("highwayEfficiency") : 0;
                boolean sideOfStreetDisplay = routeOptions.has("sideOfStreetDisplay") ? routeOptions.getBoolean("sideOfStreetDisplay") : false;
                int cyclingRoadFactor = routeOptions.has("cyclingRoadFactor") ? routeOptions.getInt("cyclingRoadFactor") : 0;
                int urbanAvoidFactor = routeOptions.has("urbanAvoidFactor") ? routeOptions.getInt("urbanAvoidFactor") : 0;


                parsedOptions = new DirectionsOptions(mustAvoidLinkIds, drivingStyle, countryBoundaryDisplay, generalize,
                        narrativeType, locale, avoidTimedConditions, destinationManeuverDisplay, enhancedNarrative,
                        filterZoneFactor, timeType, maxWalkingDistance, routeType, transferPenalty, stateBoundaryDisplay,
                        walkingSpeed, maxLinkId, arteryWeights, tryAvoidLinkIds, unit, routeNumber, shapeFormat,
                        maneuverPenalty, useTraffic, returnLinkDirections, avoidTripIds, manmaps, highwayEfficiency,
                        sideOfStreetDisplay, cyclingRoadFactor, urbanAvoidFactor);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return parsedOptions;
    }

}
