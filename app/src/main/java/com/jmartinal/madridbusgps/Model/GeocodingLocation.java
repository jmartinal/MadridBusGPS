package com.jmartinal.madridbusgps.Model;

import android.location.Location;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 14/07/2015.
 * This class stores information about a particular location obtained by a Geocoding/ReverseGeocoding request
 */
public class GeocodingLocation implements Serializable {

    private String street;
    private String adminArea6;
    private String adminArea6Type;
    private String adminArea5;
    private String adminArea5Type;
    private String adminArea4;
    private String adminArea4Type;
    private String adminArea3;
    private String adminArea3Type;
    private String adminArea1;
    private String adminArea1Type;
    private String postalCode;
    private String geocodeQualityCode;
    private String geocodeQuality;
    private Boolean dragPoint;
    private String sideOfStreet;
    private String linkId;
    private String unknownInput;
    private String type;
    private double latitude;
    private double longitude;
    private double displayLatitude;
    private double dislpayLongitude;
    private String mapUrl;

    public boolean equals(GeocodingLocation location){
        if (location == null){
            return false;
        } else {
            return this.getLatitude() == location.getLatitude() && this.getLongitude() == location.getLongitude();
        }
    }

    public GeocodingLocation(String street, String adminArea6, String adminArea6Type, String adminArea5, String adminArea5Type, String adminArea4, String adminArea4Type, String adminArea3, String adminArea3Type, String adminArea1, String adminArea1Type, String postalCode, String geocodeQualityCode, String geocodeQuality, Boolean dragPoint, String sideOfStreet, String linkId, String unknownInput, String type, double latitude, double longitude, double displayLatitude, double dislpayLongitude, String mapUrl) {
        this.street = street;
        this.adminArea6 = adminArea6;
        this.adminArea6Type = adminArea6Type;
        this.adminArea5 = adminArea5;
        this.adminArea5Type = adminArea5Type;
        this.adminArea4 = adminArea4;
        this.adminArea4Type = adminArea4Type;
        this.adminArea3 = adminArea3;
        this.adminArea3Type = adminArea3Type;
        this.adminArea1 = adminArea1;
        this.adminArea1Type = adminArea1Type;
        this.postalCode = postalCode;
        this.geocodeQualityCode = geocodeQualityCode;
        this.geocodeQuality = geocodeQuality;
        this.dragPoint = dragPoint;
        this.sideOfStreet = sideOfStreet;
        this.linkId = linkId;
        this.unknownInput = unknownInput;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayLatitude = displayLatitude;
        this.dislpayLongitude = dislpayLongitude;
        this.mapUrl = mapUrl;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdminArea6() {
        return adminArea6;
    }

    public void setAdminArea6(String adminArea6) {
        this.adminArea6 = adminArea6;
    }

    public String getAdminArea6Type() {
        return adminArea6Type;
    }

    public void setAdminArea6Type(String adminArea6Type) {
        this.adminArea6Type = adminArea6Type;
    }

    public String getAdminArea5() {
        return adminArea5;
    }

    public void setAdminArea5(String adminArea5) {
        this.adminArea5 = adminArea5;
    }

    public String getAdminArea5Type() {
        return adminArea5Type;
    }

    public void setAdminArea5Type(String adminArea5Type) {
        this.adminArea5Type = adminArea5Type;
    }

    public String getAdminArea4() {
        return adminArea4;
    }

    public void setAdminArea4(String adminArea4) {
        this.adminArea4 = adminArea4;
    }

    public String getAdminArea4Type() {
        return adminArea4Type;
    }

    public void setAdminArea4Type(String adminArea4Type) {
        this.adminArea4Type = adminArea4Type;
    }

    public String getAdminArea3() {
        return adminArea3;
    }

    public void setAdminArea3(String adminArea3) {
        this.adminArea3 = adminArea3;
    }

    public String getAdminArea3Type() {
        return adminArea3Type;
    }

    public void setAdminArea3Type(String adminArea3Type) {
        this.adminArea3Type = adminArea3Type;
    }

    public String getAdminArea1() {
        return adminArea1;
    }

    public void setAdminArea1(String adminArea1) {
        this.adminArea1 = adminArea1;
    }

    public String getAdminArea1Type() {
        return adminArea1Type;
    }

    public void setAdminArea1Type(String adminArea1Type) {
        this.adminArea1Type = adminArea1Type;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGeocodeQualityCode() {
        return geocodeQualityCode;
    }

    public void setGeocodeQualityCode(String geocodeQualityCode) {
        this.geocodeQualityCode = geocodeQualityCode;
    }

    public String getGeocodeQuality() {
        return geocodeQuality;
    }

    public void setGeocodeQuality(String geocodeQuality) {
        this.geocodeQuality = geocodeQuality;
    }

    public Boolean getDragPoint() {
        return dragPoint;
    }

    public void setDragPoint(Boolean dragPoint) {
        this.dragPoint = dragPoint;
    }

    public String getSideOfStreet() {
        return sideOfStreet;
    }

    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getUnknownInput() {
        return unknownInput;
    }

    public void setUnknownInput(String unknownInput) {
        this.unknownInput = unknownInput;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getDisplayLatitude() {
        return displayLatitude;
    }

    public void setDisplayLatitude(double displayLatitude) {
        this.displayLatitude = displayLatitude;
    }

    public double getDislpayLongitude() {
        return dislpayLongitude;
    }

    public void setDislpayLongitude(double dislpayLongitude) {
        this.dislpayLongitude = dislpayLongitude;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public LatLng getLatLng(){
        return new LatLng(this.getLatitude(), this.getLongitude());
    }

    @Override
    public String toString() {
        String result = "";
        if (street != null && !street.equals("")){
            result += street + ", ";
        }
        if (postalCode != null && !postalCode.equals("")){
            result += postalCode + " ";
        }
        if (adminArea5 != null && !adminArea5.equals("")){
            result += adminArea5 + ", ";
        }
        if (adminArea3 != null && !adminArea3.equals("")){
            result += adminArea3 + ", ";
        }
        if (adminArea1 != null && !adminArea1.equals("")){
            result += adminArea1;
        }
        return result;
    }



}
