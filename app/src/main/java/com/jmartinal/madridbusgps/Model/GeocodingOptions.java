package com.jmartinal.madridbusgps.Model;

/**
 * Created by Jorge on 17/07/2015.
 * This class represents the options that the server has used to response to a particular request
 */
public class GeocodingOptions {

    private int maxResults;
    private boolean thumbMaps;
    private String boundingBox;
    private boolean ignoreLatLngInput;
    private String delimiter;
    private String intlMode;

    public GeocodingOptions() {
    }

    public GeocodingOptions(int maxResults, boolean thumbMaps, String boundingBox, boolean ignoreLatLngInput, String delimiter, String intlMode) {
        this.maxResults = maxResults;
        this.thumbMaps = thumbMaps;
        this.boundingBox = boundingBox;
        this.ignoreLatLngInput = ignoreLatLngInput;
        this.delimiter = delimiter;
        this.intlMode = intlMode;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public boolean isThumbMaps() {
        return thumbMaps;
    }

    public void setThumbMaps(boolean thumbMaps) {
        this.thumbMaps = thumbMaps;
    }

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isIgnoreLatLngInput() {
        return ignoreLatLngInput;
    }

    public void setIgnoreLatLngInput(boolean ignoreLatLngInput) {
        this.ignoreLatLngInput = ignoreLatLngInput;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getIntlMode() {
        return intlMode;
    }

    public void setIntlMode(String intlMode) {
        this.intlMode = intlMode;
    }
}
