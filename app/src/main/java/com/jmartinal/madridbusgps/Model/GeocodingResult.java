package com.jmartinal.madridbusgps.Model;

import java.util.List;

/**
 * Created by Jorge on 17/07/2015.
 * This class stores a list of locations that match with a location provided by the user
 */
public class GeocodingResult {

    private GeocodingLocation providedLocation;
    private List<GeocodingLocation> locations;

    public GeocodingResult() {
    }

    public GeocodingResult(GeocodingLocation providedLocation, List<GeocodingLocation> locations) {
        this.providedLocation = providedLocation;
        this.locations = locations;
    }

    public GeocodingLocation getProvidedLocation() {
        return providedLocation;
    }

    public void setProvidedLocation(GeocodingLocation providedLocation) {
        this.providedLocation = providedLocation;
    }

    public List<GeocodingLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GeocodingLocation> locations) {
        this.locations = locations;
    }
}
