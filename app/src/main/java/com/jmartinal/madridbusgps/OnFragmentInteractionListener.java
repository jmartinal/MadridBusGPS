package com.jmartinal.madridbusgps;

import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.Model.BusSchedule;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.Route;

/**
 * Created by Jorge on 02/11/2015.
 */
public interface OnFragmentInteractionListener {
    void onFragmentInteraction(int position, BusNetwork busNetwork);
    void onFragmentInteraction(int position, BusSchedule busSchedule);
    void onFragmentInteraction(int childNumber, GeocodingLocation from, GeocodingLocation to, Route routeoute);
    void onFragmentInteraction(int childNumber, BusLine busLine, boolean showDeparture, boolean showDetour, boolean showStops);
}
