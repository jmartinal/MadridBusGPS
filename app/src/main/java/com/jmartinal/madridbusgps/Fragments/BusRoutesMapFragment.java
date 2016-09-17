package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusLineStop;
import com.jmartinal.madridbusgps.Model.BusLineWayPoint;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusRoutesMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusRoutesMapFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CHILD_NUMBER = "child_number";
    private static final String ARG_BUS_LINE = "bus_line";
    private static final String ARG_SHOW_DEPARTURE = "show_departure";
    private static final String ARG_SHOW_DETOUR = "show_detour";
    private static final String ARG_SHOW_STOPS = "show_stops";

    private int mSectionNumber, mChildNumber;
    private BusLine mBusLine;
    private boolean mShowDeparture, mShowDetour, mShowStops;

    private MapView mMap;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @param childNumber Parameter 2.
     * @param busLine Parameter 3.
     * @param showDeparture Parameter 4.
     * @param showDetour Parameter 5.
     * @param showStops Parameter 6.
     * @return A new instance of fragment BusRoutesMapFragment.
     */
    public static BusRoutesMapFragment newInstance(int sectionNumber, int childNumber, BusLine busLine, boolean showDeparture, boolean showDetour, boolean showStops) {
        BusRoutesMapFragment fragment = new BusRoutesMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_CHILD_NUMBER, childNumber);
        args.putSerializable(ARG_BUS_LINE, busLine);
        args.putBoolean(ARG_SHOW_DEPARTURE, showDeparture);
        args.putBoolean(ARG_SHOW_DETOUR, showDetour);
        args.putBoolean(ARG_SHOW_STOPS, showStops);
        fragment.setArguments(args);
        return fragment;
    }

    public BusRoutesMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            mChildNumber = getArguments().getInt(ARG_CHILD_NUMBER);
            mBusLine = (BusLine) getArguments().getSerializable(ARG_BUS_LINE);
            mShowDeparture = getArguments().getBoolean(ARG_SHOW_DEPARTURE);
            mShowDetour = getArguments().getBoolean(ARG_SHOW_DETOUR);
            mShowStops = getArguments().getBoolean(ARG_SHOW_STOPS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bus_routes_map, container, false);

        mMap = (MapView) rootView.findViewById(R.id.busRoutesMap);
        mMap.setClickable(true);
        mMap.getController().setZoom(14);
        LatLng center = getRouteCenter(mBusLine);
        mMap.getController().setCenter(center);

        if (mShowDeparture) {
            drawDepartureRoute(mMap, mBusLine);
            if (mShowStops) {
                drawDepartureStops(mMap, mBusLine);
            }
        }

        if (mShowDetour) {
            drawDetourRoute(mMap, mBusLine);
            if (mShowStops) {
                drawDetourStops(mMap, mBusLine);
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void drawDepartureRoute(MapView map, BusLine busLine){
        BusLineStop firstStop, lastStop;
        BusLineWayPoint pointA, pointB;
        ArrayList<BusLineWayPoint> departurePoints = new ArrayList<>();

        for (int i=0; i<busLine.getWayPoints().size(); i++) {
            BusLineWayPoint point = busLine.getWayPoints().get(i);
            if (point.getSecDetail() == 19) {
                departurePoints.add(point);
            }
        }

        // Draw line First stop -> First middle point
        firstStop = busLine.getDepartureStops().get(0);
        pointA = departurePoints.get(0);
        PathOverlay line = new PathOverlay(Color.parseColor("#05A700"), 3);
        line.addPoint(firstStop.getLatLng());
        line.addPoint(pointA.getLatLng());
        map.getOverlays().add(line);

        // Draw middle lines
        for (int i = 0; i < departurePoints.size() - 1; i++) {
            pointA = departurePoints.get(i);
            pointB = departurePoints.get(i + 1);
            line = new PathOverlay(Color.parseColor("#05A700"), 3);
            line.addPoint(pointA.getLatLng());
            line.addPoint(pointB.getLatLng());
            map.getOverlays().add(line);
        }

    }

    private void drawDepartureStops(MapView map, BusLine busLine){
        for (int i = 0; i < busLine.getDepartureStops().size(); i++) {
            BusLineStop stop = busLine.getDepartureStops().get(i);
            Marker stopMarker = new Marker(stop.getName(), String.valueOf(stop.getLine()), stop.getLatLng());
            if (i == 0) {
                stopMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "marker", "05A700"));
            } else {
                stopMarker.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "bus", "05A700"));
            }
            map.addMarker(stopMarker);
        }
    }

    private void drawDetourRoute(MapView map, BusLine busLine){
        BusLineStop firstStop, lastStop;
        BusLineWayPoint pointA, pointB;
        ArrayList<BusLineWayPoint> detourPoints = new ArrayList<>();

        for (int i=0; i<busLine.getWayPoints().size(); i++){
            BusLineWayPoint point = busLine.getWayPoints().get(i);
            if (point.getSecDetail() == 29){
                detourPoints.add(point);
            }
        }

        // Draw line First stop -> First middle point
        firstStop = busLine.getDetourStops().get(0);
        pointA = detourPoints.get(0);
        PathOverlay line = new PathOverlay(Color.parseColor("#EE0000"), 3);
        line.addPoint(firstStop.getLatLng());
        line.addPoint(pointA.getLatLng());
        map.getOverlays().add(line);

        // Draw middle lines
        for (int i = 0; i < detourPoints.size() - 1; i++) {
            pointA = detourPoints.get(i);
            pointB = detourPoints.get(i + 1);
            line = new PathOverlay(Color.parseColor("#EE0000"), 3);
            line.addPoint(pointA.getLatLng());
            line.addPoint(pointB.getLatLng());
            mMap.getOverlays().add(line);
        }

    }

    // Last detour stop == First departure stop; Draw departure marker instead detour marker
    private void drawDetourStops(MapView map, BusLine busLine){
        for (int i = 0; i < busLine.getDetourStops().size()-1; i++) {
            BusLineStop stop = busLine.getDetourStops().get(i);
            Marker stopMarker = new Marker(stop.getName(), String.valueOf(stop.getLine()), stop.getLatLng());
            if (i == 0) {
                stopMarker.setIcon(new Icon(getActivity(), Icon.Size.LARGE, "marker", "EE0000"));
            } else {
                stopMarker.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "bus", "EE0000"));
            }
            map.addMarker(stopMarker);
        }
    }

    private LatLng getRouteCenter(BusLine busLine){
        ArrayList<BusLineWayPoint> departurePoints = new ArrayList<>();
        for (int i=0; i<busLine.getWayPoints().size(); i++) {
            BusLineWayPoint point = busLine.getWayPoints().get(i);
            if (point.getSecDetail() == 19) {
                departurePoints.add(point);
            }
        }
        return new LatLng(departurePoints.get(departurePoints.size()/2).getLatitude(), departurePoints.get(departurePoints.size()/2).getLongitude());
    }


}
