package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jorge on 17/07/2015.
 */
public class DirectionsOptions implements Serializable {
    private ArrayList<Object> mustAvoidLinkIds;
    private int drivingStyle;
    private boolean countryBoundaryDisplay;
    private int generalize;
    private String narrativeType;
    private String locale;
    private boolean avoidTimedConditions;
    private boolean destinationManeuverDisplay;
    private boolean enhancedNarrative;
    private int filterZoneFactor;
    private int timeType;
    private double maxWalkingDistance;
    private String routeType;
    private int transferPenalty;
    private boolean stateBoundaryDisplay;
    private double walkingSpeed;
    private int maxLinkId;
    private ArrayList<Object> arteryWeights;
    private ArrayList<Object> tryAvoidLinkIds;
    private String unit;
    private int routeNumber;
    private String shapeFormat;
    private int maneuverPenalty;
    private boolean useTraffic;
    private boolean returnLinkDirections;
    private ArrayList<Object> avoidTripIds;
    private boolean manmaps;
    private int highwayEfficiency;
    private boolean sideOfStreetDisplay;
    private int cyclingRoadFactor;
    private int urbanAvoidFactor;

    public DirectionsOptions() {
    }

    public DirectionsOptions(ArrayList<Object> mustAvoidLinkIds, int drivingStyle, boolean countryBoundaryDisplay, int generalize, String narrativeType, String locale, boolean avoidTimedConditions, boolean destinationManeuverDisplay, boolean enhancedNarrative, int filterZoneFactor, int timeType, double maxWalkingDistance, String routeType, int transferPenalty, boolean stateBoundaryDisplay, double walkingSpeed, int maxLinkId, ArrayList<Object> arteryWeights, ArrayList<Object> tryAvoidLinkIds, String unit, int routeNumber, String shapeFormat, int maneuverPenalty, boolean useTraffic, boolean returnLinkDirections, ArrayList<Object> avoidTripIds, boolean manmaps, int highwayEfficiency, boolean sideOfStreetDisplay, int cyclingRoadFactor, int urbanAvoidFactor) {
        this.mustAvoidLinkIds = mustAvoidLinkIds;
        this.drivingStyle = drivingStyle;
        this.countryBoundaryDisplay = countryBoundaryDisplay;
        this.generalize = generalize;
        this.narrativeType = narrativeType;
        this.locale = locale;
        this.avoidTimedConditions = avoidTimedConditions;
        this.destinationManeuverDisplay = destinationManeuverDisplay;
        this.enhancedNarrative = enhancedNarrative;
        this.filterZoneFactor = filterZoneFactor;
        this.timeType = timeType;
        this.maxWalkingDistance = maxWalkingDistance;
        this.routeType = routeType;
        this.transferPenalty = transferPenalty;
        this.stateBoundaryDisplay = stateBoundaryDisplay;
        this.walkingSpeed = walkingSpeed;
        this.maxLinkId = maxLinkId;
        this.arteryWeights = arteryWeights;
        this.tryAvoidLinkIds = tryAvoidLinkIds;
        this.unit = unit;
        this.routeNumber = routeNumber;
        this.shapeFormat = shapeFormat;
        this.maneuverPenalty = maneuverPenalty;
        this.useTraffic = useTraffic;
        this.returnLinkDirections = returnLinkDirections;
        this.avoidTripIds = avoidTripIds;
        this.manmaps = manmaps;
        this.highwayEfficiency = highwayEfficiency;
        this.sideOfStreetDisplay = sideOfStreetDisplay;
        this.cyclingRoadFactor = cyclingRoadFactor;
        this.urbanAvoidFactor = urbanAvoidFactor;
    }

    public ArrayList<Object> getMustAvoidLinkIds() {
        return mustAvoidLinkIds;
    }

    public void setMustAvoidLinkIds(ArrayList<Object> mustAvoidLinkIds) {
        this.mustAvoidLinkIds = mustAvoidLinkIds;
    }

    public int getDrivingStyle() {
        return drivingStyle;
    }

    public void setDrivingStyle(int drivingStyle) {
        this.drivingStyle = drivingStyle;
    }

    public boolean isCountryBoundaryDisplay() {
        return countryBoundaryDisplay;
    }

    public void setCountryBoundaryDisplay(boolean countryBoundaryDisplay) {
        this.countryBoundaryDisplay = countryBoundaryDisplay;
    }

    public int getGeneralize() {
        return generalize;
    }

    public void setGeneralize(int generalize) {
        this.generalize = generalize;
    }

    public String getNarrativeType() {
        return narrativeType;
    }

    public void setNarrativeType(String narrativeType) {
        this.narrativeType = narrativeType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isAvoidTimedConditions() {
        return avoidTimedConditions;
    }

    public void setAvoidTimedConditions(boolean avoidTimedConditions) {
        this.avoidTimedConditions = avoidTimedConditions;
    }

    public boolean isDestinationManeuverDisplay() {
        return destinationManeuverDisplay;
    }

    public void setDestinationManeuverDisplay(boolean destinationManeuverDisplay) {
        this.destinationManeuverDisplay = destinationManeuverDisplay;
    }

    public boolean isEnhancedNarrative() {
        return enhancedNarrative;
    }

    public void setEnhancedNarrative(boolean enhancedNarrative) {
        this.enhancedNarrative = enhancedNarrative;
    }

    public int getFilterZoneFactor() {
        return filterZoneFactor;
    }

    public void setFilterZoneFactor(int filterZoneFactor) {
        this.filterZoneFactor = filterZoneFactor;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public double getMaxWalkingDistance() {
        return maxWalkingDistance;
    }

    public void setMaxWalkingDistance(double maxWalkingDistance) {
        this.maxWalkingDistance = maxWalkingDistance;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public int getTransferPenalty() {
        return transferPenalty;
    }

    public void setTransferPenalty(int transferPenalty) {
        this.transferPenalty = transferPenalty;
    }

    public boolean isStateBoundaryDisplay() {
        return stateBoundaryDisplay;
    }

    public void setStateBoundaryDisplay(boolean stateBoundaryDisplay) {
        this.stateBoundaryDisplay = stateBoundaryDisplay;
    }

    public double getWalkingSpeed() {
        return walkingSpeed;
    }

    public void setWalkingSpeed(double walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public int getMaxLinkId() {
        return maxLinkId;
    }

    public void setMaxLinkId(int maxLinkId) {
        this.maxLinkId = maxLinkId;
    }

    public ArrayList<Object> getArteryWeights() {
        return arteryWeights;
    }

    public void setArteryWeights(ArrayList<Object> arteryWeights) {
        this.arteryWeights = arteryWeights;
    }

    public ArrayList<Object> getTryAvoidLinkIds() {
        return tryAvoidLinkIds;
    }

    public void setTryAvoidLinkIds(ArrayList<Object> tryAvoidLinkIds) {
        this.tryAvoidLinkIds = tryAvoidLinkIds;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getShapeFormat() {
        return shapeFormat;
    }

    public void setShapeFormat(String shapeFormat) {
        this.shapeFormat = shapeFormat;
    }

    public int getManeuverPenalty() {
        return maneuverPenalty;
    }

    public void setManeuverPenalty(int maneuverPenalty) {
        this.maneuverPenalty = maneuverPenalty;
    }

    public boolean isUseTraffic() {
        return useTraffic;
    }

    public void setUseTraffic(boolean useTraffic) {
        this.useTraffic = useTraffic;
    }

    public boolean isReturnLinkDirections() {
        return returnLinkDirections;
    }

    public void setReturnLinkDirections(boolean returnLinkDirections) {
        this.returnLinkDirections = returnLinkDirections;
    }

    public ArrayList<Object> getAvoidTripIds() {
        return avoidTripIds;
    }

    public void setAvoidTripIds(ArrayList<Object> avoidTripIds) {
        this.avoidTripIds = avoidTripIds;
    }

    public boolean isManmaps() {
        return manmaps;
    }

    public void setManmaps(boolean manmaps) {
        this.manmaps = manmaps;
    }

    public int getHighwayEfficiency() {
        return highwayEfficiency;
    }

    public void setHighwayEfficiency(int highwayEfficiency) {
        this.highwayEfficiency = highwayEfficiency;
    }

    public boolean isSideOfStreetDisplay() {
        return sideOfStreetDisplay;
    }

    public void setSideOfStreetDisplay(boolean sideOfStreetDisplay) {
        this.sideOfStreetDisplay = sideOfStreetDisplay;
    }

    public int getCyclingRoadFactor() {
        return cyclingRoadFactor;
    }

    public void setCyclingRoadFactor(int cyclingRoadFactor) {
        this.cyclingRoadFactor = cyclingRoadFactor;
    }

    public int getUrbanAvoidFactor() {
        return urbanAvoidFactor;
    }

    public void setUrbanAvoidFactor(int urbanAvoidFactor) {
        this.urbanAvoidFactor = urbanAvoidFactor;
    }
}
