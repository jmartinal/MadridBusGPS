package com.jmartinal.madridbusgps.WSMapQuest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.jmartinal.madridbusgps.Model.GeocodingInfo;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.GeocodingOptions;
import com.jmartinal.madridbusgps.Model.GeocodingResult;

/**
 * Created by Jorge on 14/07/2015.
 */
public class JSONGeocodingParser {

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

    public static GeocodingInfo parseInfo(JSONObject serverResponse){
        JSONObject info = null;
        GeocodingInfo parsedInfo = null;
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

                parsedInfo = new GeocodingInfo(statusCode, copyrightText, copyrightImageURL, copyrightImageAltText, messages);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedInfo;
    }

    public static List<GeocodingLocation> parseLocations(JSONObject serverResponse) {
        GeocodingResult results = parseResults(serverResponse);
        return results.getLocations();
    }

    public static GeocodingOptions parseOptions(JSONObject serverResponse){
        JSONObject options = null;
        GeocodingOptions parsedOptions = null;
        try {
            options = serverResponse.getJSONObject("options");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (options != null){
            try {
                int maxResults =  options.has("maxResults") ? options.getInt("maxResults") : -1;
                boolean thumbMaps =  options.has("thumbMaps") ? options.getBoolean("thumbMaps") : true;
                String boundingBox =  options.has("boundingBox") ? options.getString("boundingBox") : null;
                boolean ignoreLatLngInput =  options.has("ignoreLatLngInput") ? options.getBoolean("ignoreLatLngInput") : false;
                String delimiter =  options.has("delimiter") ? options.getString("delimiter") : ",";
                String intlMode =  options.has("intlMode") ? options.getString("intlMode") : "AUTO";
                parsedOptions = new GeocodingOptions(maxResults, thumbMaps, boundingBox, ignoreLatLngInput, delimiter, intlMode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedOptions;
    }

    public static GeocodingResult parseResults(JSONObject serverResponse){
        JSONArray results = null;
        GeocodingResult parsedResults = null;
        try {
            results = serverResponse.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (results != null){
            try {
                JSONObject providedLocation = results.getJSONObject(0).getJSONObject("providedLocation");
                GeocodingLocation parsedProvidedLocation;

                String providedLocationStreet = providedLocation.has("street") ? providedLocation.getString("street") : null;
                String providedLocationAdminArea6 = providedLocation.has("adminArea6") ? providedLocation.getString("adminArea6") : null;
                String providedLocationAdminArea5 = providedLocation.has("adminArea5") ? providedLocation.getString("adminArea5") : null;
                if (providedLocationAdminArea5 == null){
                    providedLocationAdminArea5 = providedLocation.has("city") ? providedLocation.getString("city") : null;
                }
                String providedLocationAdminArea4 = providedLocation.has("adminArea4") ? providedLocation.getString("adminArea4") : null;
                if (providedLocationAdminArea4 == null){
                    providedLocationAdminArea4 = providedLocation.has("county") ? providedLocation.getString("county") : null;
                }
                String providedLocationAdminArea3 = providedLocation.has("adminArea3") ? providedLocation.getString("adminArea3") : null;
                if (providedLocationAdminArea3 == null){
                    providedLocationAdminArea3 = providedLocation.has("state") ? providedLocation.getString("state") : null;
                }
                String providedLocationAdminArea1 = providedLocation.has("adminArea1") ? providedLocation.getString("adminArea1") : null;
                if (providedLocationAdminArea1 == null){
                    providedLocationAdminArea1 = providedLocation.has("country") ? providedLocation.getString("country") : null;
                }
                String providedLocationPostalCode = providedLocation.has("postalCode") ? providedLocation.getString("postalCode") : null;
                Boolean providedLocationDragPoint = providedLocation.has("dragPoint") ? providedLocation.getBoolean("dragPoint") : null;
                String providedLocationType = providedLocation.has("type") ? providedLocation.getString("type") : null;
                String providedLocationLatLng = providedLocation.has("latLng") ? providedLocation.getString("latLng") : null;

                JSONObject latLng = null;
                double providedLatitude = 0.0;
                double providedLongitude = 0.0;
                if (providedLocation.has("latLng")){
                    latLng = (JSONObject) providedLocation.get("latLng");
                    providedLatitude = latLng.getDouble("lat");
                    providedLongitude = latLng.getDouble("lng");
                }

                parsedProvidedLocation = new GeocodingLocation(providedLocationStreet, providedLocationAdminArea6, null, providedLocationAdminArea5, null, providedLocationAdminArea4, null, providedLocationAdminArea3, null, providedLocationAdminArea1, null, providedLocationPostalCode, null, null, providedLocationDragPoint, null, null, null, providedLocationType, providedLatitude, providedLongitude, 0.0, 0.0, null);

                JSONArray locations = results.getJSONObject(0).getJSONArray("locations");
                List<GeocodingLocation> parsedLocations = new ArrayList<>();
                for (int i=0; i<locations.length(); i++){
                    try {
                        JSONObject location = locations.getJSONObject(i);

                        String street = location.getString("street");
                        String adminArea6 = location.getString("adminArea6");
                        String adminArea6Type = location.getString("adminArea6Type");
                        String adminArea5 = location.getString("adminArea5");
                        String adminArea5Type = location.getString("adminArea5Type");
                        String adminArea4 = location.getString("adminArea4");
                        String adminArea4Type = location.getString("adminArea4Type");
                        String adminArea3 = location.getString("adminArea3").replace("Community of ", "");
                        String adminArea3Type = location.getString("adminArea3Type");
                        String adminArea1 = location.getString("adminArea1");
                        String adminArea1Type = location.getString("adminArea1Type");
                        String postalCode = location.getString("postalCode");
                        String geocodeQualityCode = location.getString("geocodeQualityCode");
                        String geocodeQuality = location.getString("geocodeQuality");
                        Boolean dragPoint = location.getBoolean("dragPoint");
                        String sideOfStreet = location.getString("sideOfStreet");
                        String linkId = location.getString("linkId");
                        String unknownInput = location.getString("unknownInput");
                        String type = location.getString("type");
                        double latitude = location.getJSONObject("latLng").getDouble("lat");
                        double longitude = location.getJSONObject("latLng").getDouble("lng");
                        double displayLatitude = location.getJSONObject("displayLatLng").getDouble("lat");
                        double dislpayLongitude = location.getJSONObject("displayLatLng").getDouble("lng");
                        String mapUrl = location.getString("mapUrl");

                        GeocodingLocation geocodingLocation = new GeocodingLocation(street, adminArea6, adminArea6Type, adminArea5, adminArea5Type, adminArea4, adminArea4Type, adminArea3, adminArea3Type, adminArea1, adminArea1Type, postalCode, geocodeQualityCode, geocodeQuality, dragPoint, sideOfStreet, linkId, unknownInput, type, latitude, longitude, displayLatitude, dislpayLongitude, mapUrl);
                        parsedLocations.add(geocodingLocation);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                parsedResults = new GeocodingResult(parsedProvidedLocation, parsedLocations);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return parsedResults;
    }



    public static GeocodingLocation parseLocation(JSONObject location) {
        GeocodingLocation geocodingLocation = null;

        try {
            JSONObject latLng = location.has("latLng") ? location.getJSONObject("latLng") : null;
            double latitude = latLng.has("lat") ? latLng.getDouble("lat") : 0.0;
            double longitude = latLng.has("lng") ? latLng.getDouble("lng") : 0.0;

            String adminArea1 = location.has("adminArea1") ? location.getString("adminArea1") : null;
            String adminArea3 = location.has("adminArea3") ? location.getString("adminArea3") : null;
            String adminArea4 = location.has("adminArea4") ? location.getString("adminArea4") : null;
            String adminArea5 = location.has("adminArea5") ? location.getString("adminArea5") : null;
            String adminArea6 = location.has("adminArea6") ? location.getString("adminArea6") : null;

            String adminArea1Type = location.has("adminArea1Type") ? location.getString("adminArea1Type") : null;
            String adminArea3Type = location.has("adminArea3Type") ? location.getString("adminArea3Type") : null;
            String adminArea4Type = location.has("adminArea4Type") ? location.getString("adminArea4Type") : null;
            String adminArea5Type = location.has("adminArea5Type") ? location.getString("adminArea5Type") : null;
            String adminArea6Type = location.has("adminArea6Type") ? location.getString("adminArea6Type") : null;

            String street = location.has("street") ? location.getString("street") : null;
            String type = location.has("type") ? location.getString("type") : null;

            JSONObject displayLatLng = location.has("displayLatLng") ? location.getJSONObject("displayLatLng") : null;
            double displayLatitude = displayLatLng.has("lat") ? displayLatLng.getDouble("lat") : 0.0;
            double displayLongitude = displayLatLng.has("lng") ? displayLatLng.getDouble("lng") : 0.0;

            String postalCode = location.has("postalCode") ? location.getString("postalCode") : null;
            String linkId = location.has("linkId") ? location.getString("linkId") : null;
            String sideOfStreet = location.has("sideOfStreet") ? location.getString("sideOfStreet") : null;

            String geocodeQualityCode = location.has("geocodeQualityCode") ? location.getString("geocodeQualityCode") : null;
            String geocodeQuality = location.has("geocodeQuality") ? location.getString("geocodeQuality") : null;
            Boolean dragPoint = location.has("dragPoint") ? location.getBoolean("dragPoint") : null;
            String unknownInput = location.has("unknownInput") ? location.getString("unknownInput") : null;
            String mapUrl = location.has("mapUrl") ? location.getString("mapUrl") : null;

            geocodingLocation = new GeocodingLocation(street, adminArea6, adminArea6Type, adminArea5, adminArea5Type, adminArea4, adminArea4Type, adminArea3, adminArea3Type, adminArea1, adminArea1Type, postalCode, geocodeQualityCode, geocodeQuality, dragPoint, sideOfStreet, linkId, unknownInput, type, latitude, longitude, displayLatitude, displayLongitude, mapUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return geocodingLocation;
    }

}
