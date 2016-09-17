package com.jmartinal.madridbusgps.Model;

import java.io.Serializable;

/**
 * Created by Jorge on 14/12/2015.
 */
public class Maneuver implements Serializable {

    private String narrative;
    private int turnType;

    public Maneuver() {
    }

    public Maneuver(String narrative, int turnType) {
        this.narrative = narrative;
        this.turnType = turnType;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public int getTurnType() {
        return turnType;
    }

    public void setTurnType(int turnType) {
        this.turnType = turnType;
    }
}
