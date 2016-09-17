package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.Route;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.views.MapView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRouteMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRouteMapFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CHILD_NUMBER = "child_number";
    private static final String ARG_FROM = "from";
    private static final String ARG_TO = "to";
    private static final String ARG_ROUTE = "route";

    private int mSectionNumber, mChildNumber;
    private GeocodingLocation mFrom, mTo;
    private Route mRoute;

    private MapView mRouteMap;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parentSectionNumber Parameter 1.
     * @param childNumber Parameter 2.
     * @param from Parameter 3.
     * @param to Parameter 4.
     * @param route Parameter 5.
     * @return A new instance of fragment NewRouteMapFragment.
     */
    public static NewRouteMapFragment newInstance(int parentSectionNumber, int childNumber, GeocodingLocation from, GeocodingLocation to, Route route) {
        NewRouteMapFragment fragment = new NewRouteMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, parentSectionNumber);
        args.putInt(ARG_CHILD_NUMBER, childNumber);
        args.putSerializable(ARG_FROM, from);
        args.putSerializable(ARG_TO, to);
        args.putSerializable(ARG_ROUTE, route);
        fragment.setArguments(args);
        return fragment;
    }

    public NewRouteMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            mChildNumber = getArguments().getInt(ARG_CHILD_NUMBER);
            mFrom = (GeocodingLocation) getArguments().getSerializable(ARG_FROM);
            mTo = (GeocodingLocation) getArguments().getSerializable(ARG_TO);
            mRoute = (Route) getArguments().getSerializable(ARG_ROUTE);
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
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_new_route_map, container, false);

        if (savedInstanceState != null){
            mRoute = (Route) savedInstanceState.getSerializable("mRoute");
        }

        mRouteMap = (MapView) rootView.findViewById(R.id.newRouteMap);
        mRouteMap.setClickable(true);
        mRouteMap.getController().setZoom(14);
        mRouteMap.getController().setCenter(mFrom.getLatLng());

        // User's current position
        GpsLocationProvider gpsLocationProvider = new GpsLocationProvider(rootView.getContext());
        UserLocationOverlay userLocationOverlay = new UserLocationOverlay(gpsLocationProvider, mRouteMap);
        userLocationOverlay.enableMyLocation();
        userLocationOverlay.setDrawAccuracyEnabled(true);
        mRouteMap.getOverlays().add(userLocationOverlay);


        // DRAW From-FirstStop WALKING ROUTE
        // From Marker
        Marker fromMarker = new Marker("Salida", mFrom.toString(), mFrom.getLatLng());
        fromMarker.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "pitch", "05A700"));
        mRouteMap.addMarker(fromMarker);
        // Draw From-FirstStop route lines
        for(int i=0; i<mRoute.getFirstWalkingRoute().getShapePoints().size()-1; i++){
            PathOverlay line = new PathOverlay(Color.parseColor("#0E508D"), 3);
            line.addPoint(mRoute.getFirstWalkingRoute().getShapePoints().get(i));
            line.addPoint(mRoute.getFirstWalkingRoute().getShapePoints().get(i + 1));
            mRouteMap.getOverlays().add(line);
        }

        if (mRoute.getBusRoute() != null) {
            // DRAW BUS ROUTE
            // Draw bus stop markers
            for (int i = 0; i < mRoute.getBusRoute().getNodes().size(); i++) {
                Marker busStopMarker;
                if (i == 0) {
                    busStopMarker = new Marker("Primera parada", mRoute.getBusRoute().getFirstNode().getName(), mRoute.getBusRoute().getFirstNode().getLatLng());
                    busStopMarker.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "bus", "05A700"));
                } else if (i == mRoute.getBusRoute().getNodes().size() - 1) {
                    busStopMarker = new Marker("Última parada", mRoute.getBusRoute().getLastNode().getName(), mRoute.getBusRoute().getLastNode().getLatLng());
                    busStopMarker.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "bus", "EE0000"));
                } else {
                    if (mRoute.getBusRoute().getNodes().get(i).isTransferNode()) {
                        busStopMarker = new Marker("Transbordo", mRoute.getBusRoute().getNodes().get(i).getName(), mRoute.getBusRoute().getNodes().get(i).getLatLng());
                        busStopMarker.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "embassy", "0E89D1"));
                    } else {
                        busStopMarker = new Marker("Nueva parada", mRoute.getBusRoute().getNodes().get(i).getName(), mRoute.getBusRoute().getNodes().get(i).getLatLng());
                        busStopMarker.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "embassy", "0E508D"));
                    }
                }
                mRouteMap.addMarker(busStopMarker);
            }
            // Draw bus route lines
            for (int i = 0; i < mRoute.getBusRoute().getWayPoints().size(); i++) {
                PathOverlay line = new PathOverlay(Color.parseColor("#0E508D"), 3);
                if (i == 0) {
//                    line.addPoint(mRoute.getBusRoute().getFirstNode().getLatLng());
//                    line.addPoint(mRoute.getBusRoute().getWayPoints().get(i).getLatLng());
                } else if (i == mRoute.getBusRoute().getWayPoints().size() - 1) {
//                    line.addPoint(mRoute.getBusRoute().getWayPoints().get(i).getLatLng());
//                    line.addPoint(mRoute.getBusRoute().getLastNode().getLatLng());
                } else {
                    line.addPoint(mRoute.getBusRoute().getWayPoints().get(i).getLatLng());
                    line.addPoint(mRoute.getBusRoute().getWayPoints().get(i + 1).getLatLng());
                }
                mRouteMap.getOverlays().add(line);
            }
        }


        // DRAW LastStop-To WALKING ROUTE
        // Draw Marker To
        Marker toMarker = new Marker("Llegada", mTo.toString(), mTo.getLatLng());
        toMarker.setIcon(new Icon(getActivity(), Icon.Size.MEDIUM, "pitch", "EE0000"));
        mRouteMap.addMarker(toMarker);
        if (mRoute.getLastWalkingRoute() != null) {
            // Draw LastStop-To walking route lines
            for (int i = 0; i < mRoute.getLastWalkingRoute().getShapePoints().size() - 1; i++) {
                PathOverlay line = new PathOverlay(Color.parseColor("#0E508D"), 3);
                line.addPoint(mRoute.getLastWalkingRoute().getShapePoints().get(i));
                line.addPoint(mRoute.getLastWalkingRoute().getShapePoints().get(i + 1));
                mRouteMap.getOverlays().add(line);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("mRoute", mRoute);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_new_route_map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_route_as_list:
                showAsList(2, mFrom, mTo, mRoute);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAsList(int position, GeocodingLocation from, GeocodingLocation to, Route route) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position, from, to, route);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activity.invalidateOptionsMenu();
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

}
