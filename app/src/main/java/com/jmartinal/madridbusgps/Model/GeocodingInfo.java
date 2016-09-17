package com.jmartinal.madridbusgps.Model;

import java.util.ArrayList;

/**
 * Created by Jorge on 17/07/2015.
 * This class stores various paremeters about the response send to a geocoding request
 */
public class GeocodingInfo {

    private int statusCode;
    private String copyrightText;
    private String copyrightImageURL;
    private String copyrightImageAltText;
    private ArrayList<String> messages;

    public GeocodingInfo() {
    }

    public GeocodingInfo(int statusCode, String copyrightText, String copyrightImageURL, String copyrightImageAltText, ArrayList<String> messages) {
        this.statusCode = statusCode;
        this.copyrightText = copyrightText;
        this.copyrightImageURL = copyrightImageURL;
        this.copyrightImageAltText = copyrightImageAltText;
        this.messages = messages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public String getCopyrightImageURL() {
        return copyrightImageURL;
    }

    public void setCopyrightImageURL(String copyrightImageURL) {
        this.copyrightImageURL = copyrightImageURL;
    }

    public String getCopyrightImageAltText() {
        return copyrightImageAltText;
    }

    public void setCopyrightImageAltText(String copyrightImageAltText) {
        this.copyrightImageAltText = copyrightImageAltText;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
