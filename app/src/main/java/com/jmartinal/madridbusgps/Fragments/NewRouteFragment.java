package com.jmartinal.madridbusgps.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jmartinal.madridbusgps.Adapter.GeocodingLocationAutocompleteAdapter;
import com.jmartinal.madridbusgps.Model.BusLineStop;
import com.jmartinal.madridbusgps.Model.BusLineWayPoint;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.Model.BusNetworkNode;
import com.jmartinal.madridbusgps.Model.BusNetworkRouteNode;
import com.jmartinal.madridbusgps.Model.BusRoute;
import com.jmartinal.madridbusgps.Model.DirectionsRoute;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.Route;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTOpenDataParser;
import com.jmartinal.madridbusgps.WSMapQuest.JSONDirectionsParser;
import com.jmartinal.madridbusgps.WSMapQuest.JSONGeocodingParser;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.Utils.Constants;
import com.jmartinal.madridbusgps.Utils.DelayAutoCompleteTextView;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTRestClient;
import com.jmartinal.madridbusgps.Utils.Functions;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRouteFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_BUS_NETWORK = "bus_network";

    private int mSectionNumber;
    private BusNetwork mBusNetwork;

    private OnFragmentInteractionListener mListener;

    private static final int THRESHOLD = 1;

    private DelayAutoCompleteTextView mAutocompleteFrom, mAutocompleteTo;
    private FloatingActionButton mUseGPS, mFindRoute;
    private GeocodingLocation mFrom, mTo;
    private boolean streetTyped, gpsPressed;
    private Route mRoute;
    private BusRoute bestRoute;
    private ArrayList<Integer> routeNodesLines;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter section_number.
     * @param busNetwork    Parameter bus_lines.
     * @return A new instance of fragment NewRouteFragment.
     */
    public static NewRouteFragment newInstance(int sectionNumber, BusNetwork busNetwork) {
        NewRouteFragment fragment = new NewRouteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_BUS_NETWORK, busNetwork);
        fragment.setArguments(args);
        return fragment;
    }

    public NewRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            mBusNetwork = (BusNetwork) getArguments().getSerializable(ARG_BUS_NETWORK);
        }

        if (savedInstanceState != null) {
            mFrom = (GeocodingLocation) savedInstanceState.getSerializable("mFrom");
            mTo = (GeocodingLocation) savedInstanceState.getSerializable("mTo");
            setGpsPressed(savedInstanceState.getBoolean("gpsPressed"));
            setStreetTyped(savedInstanceState.getBoolean("streetTyped"));
        } else {
            mFrom = null;
            mTo = null;
            setStreetTyped(true);
            setGpsPressed(false);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("mFrom", mFrom);
        outState.putSerializable("mTo", mTo);
        outState.putBoolean("gpsPressed", true);
        outState.putBoolean("streetTyped", false);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_route, container, false);

        mAutocompleteFrom = (DelayAutoCompleteTextView) rootView.findViewById(R.id.et_from);
        mAutocompleteFrom.setThreshold(THRESHOLD);
        mAutocompleteFrom.setAdapter(new GeocodingLocationAutocompleteAdapter(getActivity(), this, mFrom));
        mAutocompleteFrom.setLoadingIndicator((android.widget.ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_from));
        mAutocompleteFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mFrom = (GeocodingLocation) adapterView.getItemAtPosition(position);
                setStreetTyped(false);
                mAutocompleteFrom.setText(mFrom.toString());
                Functions.hideKeyboard(getActivity(), mAutocompleteFrom);
            }
        });
        mAutocompleteFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isStreetTyped()) {
                    mFrom = null;
                }
            }
        });
        mAutocompleteFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Functions.hideKeyboard(getActivity(), v);
                }
            }
        });

        mAutocompleteTo = (DelayAutoCompleteTextView) rootView.findViewById(R.id.et_to);
        mAutocompleteTo.setThreshold(THRESHOLD);
        mAutocompleteTo.setAdapter(new GeocodingLocationAutocompleteAdapter(getActivity(), this, mTo)); // 'this' is NewRouteFragment instance
        mAutocompleteTo.setLoadingIndicator((android.widget.ProgressBar) rootView.findViewById(R.id.pb_loading_indicator_to));
        mAutocompleteTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mTo = (GeocodingLocation) adapterView.getItemAtPosition(position);
                setStreetTyped(false);
                mAutocompleteTo.setText(mTo.toString());
                Functions.hideKeyboard(getActivity(), mAutocompleteTo);
            }
        });
        mAutocompleteTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isStreetTyped()) {
                    mTo = null;
                }
            }
        });
        mAutocompleteTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Functions.hideKeyboard(getActivity(), v);
                }
            }
        });

        mUseGPS = (FloatingActionButton) rootView.findViewById(R.id.newRouteGPSFab);

        mUseGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_REQUEST_LOCATION);
                    } else {
                        if (Functions.isGpsStatusOk(getActivity()) && Functions.isOnline(getActivity())) {
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_FINE);
                            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                            String bestProvider = locationManager.getBestProvider(criteria, true);
                            locationManager.requestLocationUpdates(bestProvider, 1000, 10, new MyLocationListener());
                            Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                            if (lastKnownLocation != null) {
                                new ReverseGeocodingTask().execute(lastKnownLocation);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "GPS no disponible temporalmente", Toast.LENGTH_SHORT).show();
                            }
                        } else if (!Functions.isGpsStatusOk(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el sistema de GPS", Toast.LENGTH_LONG).show();
                        } else if (!Functions.isOnline(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (Functions.isGpsStatusOk(getActivity()) && Functions.isOnline(getActivity())) {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                        String bestProvider = locationManager.getBestProvider(criteria, true);
                        locationManager.requestLocationUpdates(bestProvider, 1000, 10, new MyLocationListener());
                        Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
                        if (lastKnownLocation != null) {
                            new ReverseGeocodingTask().execute(lastKnownLocation);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "GPS no disponible temporalmente", Toast.LENGTH_SHORT).show();
                        }
                    } else if (!Functions.isGpsStatusOk(getActivity())) {
                        Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el sistema de GPS", Toast.LENGTH_LONG).show();
                    } else if (!Functions.isOnline(getActivity())) {
                        Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mFindRoute = (FloatingActionButton) rootView.findViewById(R.id.newRouteFab);
        mFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyboard(getActivity(), view);
                if (Functions.isOnline(getActivity())) {
                    if (mFrom == null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Selecciona un origen de la lista desplegable", Toast.LENGTH_LONG).show();
                    } else if (mTo == null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Selecciona un destino de la lista desplegable", Toast.LENGTH_LONG).show();
                    } else {
                        if (mFrom.equals(mTo)) {
                            Toast.makeText(getActivity().getApplicationContext(), "El origen y el destino coinciden", Toast.LENGTH_LONG).show();
                        } else {
                            CheckBox nightLines = (CheckBox) getActivity().findViewById(R.id.nightLinesCheckbox);
                            new RoutingTask(nightLines.isChecked()).execute(mFrom, mTo);
                        }
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
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

    public void displayRoute(int position, GeocodingLocation from, GeocodingLocation to, Route route) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position, from, to, route);
        }
    }

    public boolean isStreetTyped() {
        return streetTyped;
    }

    public void setStreetTyped(boolean typed) {
        this.streetTyped = typed;
    }

    public boolean isGpsPressed() {
        return gpsPressed;
    }

    public void setGpsPressed(boolean gpsPressed) {
        this.gpsPressed = gpsPressed;
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private class ReverseGeocodingTask extends AsyncTask<Location, Void, GeocodingLocation> {

        String query = "";
        Double lat = 0.0, lng = 0.0;

        @Override
        protected void onPreExecute() {
            setGpsPressed(true);
            super.onPreExecute();
        }

        @Override
        protected GeocodingLocation doInBackground(Location... params) {
            HttpURLConnection httpURLConnection = null;
            JSONObject serverResponse = new JSONObject();
            GeocodingLocation geocodingLocation = null;

            try {
                Location location = params[0];
                lat = location.getLatitude();
                lng = location.getLongitude();
                query = Constants.REVERSE_GEOCODING_BASE_URL +
                        Constants.MAPQUEST_KEY +
                        Constants.REVERSE_GEOCODING_LAT +
                        lat +
                        Constants.REVERSE_GEOCODING_LNG +
                        lng;
                httpURLConnection = (HttpURLConnection) new URL(query).openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                serverResponse = JSONGeocodingParser.getJSONObject(inputStream);
                geocodingLocation = JSONGeocodingParser.parseLocations(serverResponse).get(0);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return geocodingLocation;
        }

        @Override
        protected void onPostExecute(GeocodingLocation s) {
            mAutocompleteFrom.setText(s.toString());
            mFrom = s;
            super.onPostExecute(s);
        }
    }

    private class RoutingTask extends AsyncTask<GeocodingLocation, Void, Route> {

        private final int SIMPLE_DAY_ROUTE_NSTOPS = 20;
        private final int SIMPLE_NIGHT_ROUTE_NSTOPS = 20;
        private final int COMPLEX_DAY_ROUTE_NSTOPS = 3;
        private final int COMPLEX_NIGHT_ROUTE_NSTOPS = 10;
        private Dialog dialog;
        private int routeType;
        private boolean useNightLines;

        public RoutingTask(boolean useNightLines) {
            this.dialog = new Dialog(getActivity(), R.style.LoadingDialogTheme);
            this.useNightLines = useNightLines;
        }

        @Override
        protected void onPreExecute() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            dialog.getWindow();
            dialog.setContentView(R.layout.loading_route_message);
            dialog.setTitle(R.string.loading_route_message_title);
            dialog.setCancelable(false);
            dialog.show();
            RadioGroup routeTypeGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
            routeType = routeTypeGroup.getCheckedRadioButtonId();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Route route) {
            dialog.dismiss();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            if (route != null) {
                displayRoute(1, mFrom, mTo, route);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ErrorDialogTheme);
                builder.setTitle(R.string.route_error_title);
                builder.setMessage(R.string.route_error_message);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(route);
        }

        @Override
        protected Route doInBackground(GeocodingLocation... params) {

            GeocodingLocation begin = params[0];
            GeocodingLocation end = params[1];
            Route result = null;
            BusRoute busRoute = null;

            Location beginLocation = new Location("");
            beginLocation.setLatitude(begin.getLatitude());
            beginLocation.setLongitude(begin.getLongitude());

            Location endLocation = new Location("");
            endLocation.setLatitude(end.getLatitude());
            endLocation.setLongitude(end.getLongitude());

            double distanceBeginEnd = beginLocation.distanceTo(endLocation);

            ArrayList<BusNetworkNode> nodesNearBegin;
            ArrayList<BusNetworkNode> nodesNearEnd;

            if (!useNightLines) {
                nodesNearBegin = mBusNetwork.findNClosestBusNetworkNodes(SIMPLE_DAY_ROUTE_NSTOPS, begin, useNightLines);
                nodesNearEnd = mBusNetwork.findNClosestBusNetworkNodes(SIMPLE_DAY_ROUTE_NSTOPS, end, useNightLines);
            } else {
                nodesNearBegin = mBusNetwork.findNClosestBusNetworkNodes(SIMPLE_NIGHT_ROUTE_NSTOPS, begin, useNightLines);
                nodesNearEnd = mBusNetwork.findNClosestBusNetworkNodes(SIMPLE_NIGHT_ROUTE_NSTOPS, end, useNightLines);
            }

            int i = 0;
            boolean busNeeded = false;
            while (i < nodesNearBegin.size() && !busNeeded) {
                Location firstStopLocation = new Location("");
                firstStopLocation.setLatitude(nodesNearBegin.get(i).getLatitude());
                firstStopLocation.setLongitude(nodesNearBegin.get(i).getLongitude());
                double distanceBeginFirstStop = beginLocation.distanceTo(firstStopLocation);
                int j = 0;
                while (j < nodesNearEnd.size() && !busNeeded) {
                    Location lastStopLocation = new Location("");
                    lastStopLocation.setLatitude(nodesNearEnd.get(j).getLatitude());
                    lastStopLocation.setLongitude(nodesNearEnd.get(j).getLongitude());
                    double distanceLastStopEnd = lastStopLocation.distanceTo(endLocation);
                    if (distanceBeginEnd > (distanceBeginFirstStop + distanceLastStopEnd)) {
                        busNeeded = true;
                    } else {
                        j++;
                    }
                }
                if (!busNeeded) {
                    i++;
                }
            }

            // Check if a bus route is needed
            if (!busNeeded) {
                result = new Route(findWalkingRoute(mFrom.getLatLng(), mTo.getLatLng()), null, null);
            } else {
                if (!useNightLines) {
                    busRoute = mBusNetwork.findNoTransferBusRoute(nodesNearBegin, nodesNearEnd, useNightLines);
                } else {
                    busRoute = mBusNetwork.findNoTransferBusRoute(nodesNearBegin, nodesNearEnd, useNightLines);

                }
                // Search for lines that connect 1 stop near to BEGIN and 1 stop near to END.
                if (busRoute != null) {
                    // There are bus routes between BEGIN and END without transfers
                    DirectionsRoute firstWalkingRoute = findWalkingRoute(begin.getLatLng(), busRoute.getFirstNode().getLatLng());
                    DirectionsRoute lastWalkingRoute = findWalkingRoute(end.getLatLng(), busRoute.getLastNode().getLatLng());
                    busRoute.setWayPoints(requestNoTransferRouteWayPoints(busRoute.getLines(), busRoute.getWays(), busRoute.getFirstNode(), busRoute.getLastNode()));
                    result = new Route(firstWalkingRoute, busRoute, lastWalkingRoute);
                } else {
                    // There are not bus routes between BEGIN and END without transfers
                    // Search for bus routes between BEGIN and END with only 1 transfer
                    if (!useNightLines) {
                        busRoute = mBusNetwork.findOneTransferBusRoute(nodesNearBegin, nodesNearEnd, useNightLines);
                    } else {
                        busRoute = mBusNetwork.findOneTransferBusRoute(nodesNearBegin, nodesNearEnd, useNightLines);
                    }
                    if (busRoute != null) {
                        // There are bus routes between BEGIN and END with only one transfer
                        DirectionsRoute firstWalkingRoute = findWalkingRoute(begin.getLatLng(), busRoute.getFirstNode().getLatLng());
                        DirectionsRoute lastWalkingRoute = findWalkingRoute(end.getLatLng(), busRoute.getLastNode().getLatLng());
                        busRoute.setWayPoints(requestOneTransferRouteWayPoints(busRoute.getLines(), busRoute.getWays(), busRoute.getFirstNode(), busRoute.getTransfers().get(0), busRoute.getLastNode()));
                        result = new Route(firstWalkingRoute, busRoute, lastWalkingRoute);
                    } else {
                        // There are not bus routes between BEGIN and END with only one transfer
                        // Search for more complex bus routes
                        ArrayList<BusRoute> routes = new ArrayList<>();
                        Route route = null;

                        BusLineStop[] nearestStopsFrom;
                        BusLineStop[] nearestStopsTo;

                        if (!useNightLines) {
                            nodesNearBegin = mBusNetwork.findNClosestBusNetworkNodes(COMPLEX_DAY_ROUTE_NSTOPS, begin, useNightLines);
                            nodesNearEnd = mBusNetwork.findNClosestBusNetworkNodes(COMPLEX_DAY_ROUTE_NSTOPS, end, useNightLines);
                        } else {
                            nodesNearBegin = mBusNetwork.findNClosestBusNetworkNodes(COMPLEX_NIGHT_ROUTE_NSTOPS, begin, useNightLines);
                            nodesNearEnd = mBusNetwork.findNClosestBusNetworkNodes(COMPLEX_NIGHT_ROUTE_NSTOPS, end, useNightLines);
                        }

                        Location mFromLocation = new Location("");
                        mFromLocation.setLatitude(mFrom.getLatitude());
                        mFromLocation.setLongitude(mFrom.getLongitude());

                        Location mToLocation = new Location("");
                        mToLocation.setLatitude(mTo.getLatitude());
                        mToLocation.setLongitude(mTo.getLongitude());

                        for (int i1 = 0; i1 < nodesNearBegin.size(); i1++) {
                            Location firstStopLocation = new Location("");
                            firstStopLocation.setLatitude(nodesNearBegin.get(i1).getLatitude());
                            firstStopLocation.setLongitude(nodesNearBegin.get(i1).getLongitude());
                            for (int j = 0; j < nodesNearEnd.size(); j++) {
                                Location lastStopLocation = new Location("");
                                lastStopLocation.setLatitude(nodesNearEnd.get(j).getLatitude());
                                lastStopLocation.setLongitude(nodesNearEnd.get(j).getLongitude());
                                /* No need to add busRoute if:
                                   - distance(from, to) < distance(from, firstStop)
                                   - distance(from, to) < distance(lastStop, to)
                                 */
                                double distanceFromTo = mFromLocation.distanceTo(mToLocation);
                                double distanceFromFirstStop = mFromLocation.distanceTo(firstStopLocation);
                                double distanceLastStopTo = lastStopLocation.distanceTo(mToLocation);
                                if (distanceFromTo > distanceFromFirstStop + distanceLastStopTo) {
                                    routes.add(mBusNetwork.findNTransferBusRoute(nodesNearBegin.get(i1), nodesNearEnd.get(j), useNightLines));
                                }
                            }
                        }

                        if (routes != null && !routes.isEmpty()) {
                            BusRoute shortestRoute, lessStopsRoute, lessTransfersRoute;
                            shortestRoute = lessStopsRoute = lessTransfersRoute = routes.get(0);
                            for (BusRoute complexBusRoute : routes) {
                                if (complexBusRoute != null) {
                                    if (complexBusRoute.getDistance() < shortestRoute.getDistance()) {
                                        shortestRoute = complexBusRoute;
                                    }
                                    if (complexBusRoute.getNodes().size() < lessStopsRoute.getNodes().size()) {
                                        lessStopsRoute = complexBusRoute;
                                    }
                                    if (complexBusRoute.getnTransfers() < lessTransfersRoute.getnTransfers()) {
                                        lessTransfersRoute = complexBusRoute;
                                    }
                                }
                            }
                            bestRoute = null;
                            switch (routeType) {
                                case R.id.radioShortest:
                                    bestRoute = shortestRoute;
                                    break;
                                case R.id.radioLessStops:
                                    bestRoute = lessStopsRoute;
                                    break;
                                case R.id.radioLessTransfers:
                                    bestRoute = lessTransfersRoute;
                                    break;
                                default:
                                    bestRoute = shortestRoute;
                                    break;
                            }

                            DirectionsRoute firstWalkingRoute = findWalkingRoute(mFrom.getLatLng(), bestRoute.getFirstNode().getLatLng());
                            DirectionsRoute lastWalkingRoute = findWalkingRoute(bestRoute.getLastNode().getLatLng(), mTo.getLatLng());

                            String paramLines = "";
                            routeNodesLines = bestRoute.getLines();
                            for (int j = 0; j < routeNodesLines.size(); j++) {
                                paramLines = j == 0 ? String.valueOf(routeNodesLines.get(j)) : paramLines + "|" + String.valueOf(routeNodesLines.get(j));
                            }

                            String getRouteLinesRouteURL = Constants.EMT_API_BUS_SERVICE_URL + "GetRouteLinesRoute.php";
                            RequestParams getRouteLinesRouteParams = new RequestParams();
                            getRouteLinesRouteParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
                            getRouteLinesRouteParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
                            getRouteLinesRouteParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                            getRouteLinesRouteParams.add("Lines", paramLines);
                            EMTRestClient.post(getRouteLinesRouteURL, getRouteLinesRouteParams, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    ArrayList<BusLineWayPoint> parsedWayPoints = EMTOpenDataParser.parseRouteLinesRoute(response);
                                    int firstBusNetworkNodeIndex = 0, lastBusNetworkNodeIndex = 0;
                                    int it1 = 0; // iterator for routeNodes
                                    int it2 = 0; // iterator for routeNodesLines
                                    do {
                                        if (it1 == 0) {
                                            firstBusNetworkNodeIndex = 0;
                                            it2 = 0;
                                        } else {
                                            firstBusNetworkNodeIndex = lastBusNetworkNodeIndex;
                                            it2 = it2 + 1;
                                        }
                                        int it3 = firstBusNetworkNodeIndex + 1;
                                        boolean stop = false;
                                        do {
                                            if (bestRoute.getNodes().get(it3).isTransferNode() || it3 == bestRoute.getNodes().size() - 1) {
                                                lastBusNetworkNodeIndex = it3;
                                                stop = true;
                                            } else {
                                                it3++;
                                            }
                                        } while (it3 < bestRoute.getNodes().size() && !stop);
                                        BusNetworkRouteNode firstBusNetworkRouteNode = bestRoute.getNodes().get(firstBusNetworkNodeIndex);
                                        BusNetworkRouteNode lastBusNetworkRouteNode = bestRoute.getNodes().get(lastBusNetworkNodeIndex);
                                        int firstWayPoint = 0;
                                        int lastWayPoint = 0;
                                        int it4 = 0;
                                        do {
                                            if (Integer.valueOf(parsedWayPoints.get(it4).getNode()) == firstBusNetworkRouteNode.getNode() && parsedWayPoints.get(it4).getLine() == routeNodesLines.get(it2)) {
                                                firstWayPoint = it4;
                                            }
                                            if (Integer.valueOf(parsedWayPoints.get(it4).getNode()) == lastBusNetworkRouteNode.getNode() && parsedWayPoints.get(it4).getLine() == routeNodesLines.get(it2)) {
                                                lastWayPoint = it4;
                                            }
                                            it4++;
                                        }
                                        while (it4 < parsedWayPoints.size() && (firstWayPoint == 0 || lastWayPoint == 0));
                                        if (firstWayPoint < lastWayPoint) {
                                            if ((((parsedWayPoints.get(firstWayPoint).getSecDetail() == 10) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 10))
                                                    || ((parsedWayPoints.get(firstWayPoint).getSecDetail() == 10) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 20)))
                                                    || ((parsedWayPoints.get(firstWayPoint).getSecDetail() == 20) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 20))) {
                                                for (int a = firstWayPoint; a < lastWayPoint; a++) {
                                                    if (parsedWayPoints.get(a).getSecDetail() == 19 || parsedWayPoints.get(a).getSecDetail() == 29) {
                                                        bestRoute.getWayPoints().add(parsedWayPoints.get(a));
                                                    }
                                                }
                                            }
                                        } else if (firstWayPoint > lastWayPoint) {
                                            if ((((parsedWayPoints.get(firstWayPoint).getSecDetail() == 10) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 10))
                                                    || ((parsedWayPoints.get(firstWayPoint).getSecDetail() == 10) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 20)))
                                                    || ((parsedWayPoints.get(firstWayPoint).getSecDetail() == 20) && (parsedWayPoints.get(lastWayPoint).getSecDetail() == 20))) {
                                                for (int a = lastWayPoint; a < firstWayPoint; a++) {
                                                    if (parsedWayPoints.get(a).getSecDetail() == 19 || parsedWayPoints.get(a).getSecDetail() == 29) {
                                                        bestRoute.getWayPoints().add(parsedWayPoints.get(a));
                                                    }
                                                }
                                            }
                                        }
                                        it1 = lastBusNetworkNodeIndex;
                                    } while (it1 < bestRoute.getNodes().size());
                                    super.onSuccess(statusCode, headers, response);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });

                            result = new Route(firstWalkingRoute, bestRoute, lastWalkingRoute);

                        } else {
                            result = new Route(findWalkingRoute(mFrom.getLatLng(), mTo.getLatLng()), null, null);
                        }

                    }
                }
            }

            return result;
        }

        private DirectionsRoute findWalkingRoute(LatLng from, LatLng to) {
            DirectionsRoute result = null;

            HttpURLConnection httpURLConnection = null;
            JSONObject serverResponse = new JSONObject();
            DirectionsRoute firstWalkingRoute = null;
            DirectionsRoute lastWalkingRoute = null;
            String query = "";
            try {

                query = Constants.ROUTING_BASE_URL +
                        Constants.MAPQUEST_KEY +
                        Constants.ROUTING_ROUTE_TYPE +
                        Constants.ROUTING_LANGUAGE +
                        Constants.ROUTING_UNIT +
                        Constants.ROUTING_SHAPE_FORMAT +
                        Constants.ROUTING_GENERALIZE +
                        Constants.ROUTING_FROM +
                        from.getLatitude() + "," +
                        from.getLongitude() +
                        Constants.ROUTING_TO +
                        to.getLatitude() + "," +
                        to.getLongitude();

                httpURLConnection = (HttpURLConnection) new URL(query).openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                serverResponse = JSONDirectionsParser.getJSONObject(inputStream);
                result = JSONDirectionsParser.parseRoute(serverResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            result = result.getRouteErrErrorCode() != -400 ? null : result;

            return result;
        }

        private ArrayList<BusLineWayPoint> requestNoTransferRouteWayPoints(ArrayList<Integer> lines, ArrayList<Integer> ways, final BusNetworkNode firstNode, final BusNetworkNode lastNode) {
            final ArrayList<BusLineWayPoint> results = new ArrayList<>();
            String line = String.valueOf(lines.get(0));
            final int way = ways.get(0);
            String getRouteLinesRouteURL = Constants.EMT_API_BUS_SERVICE_URL + "GetRouteLinesRoute.php";
            RequestParams getRouteLinesRouteParams = new RequestParams();
            getRouteLinesRouteParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
            getRouteLinesRouteParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
            getRouteLinesRouteParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            getRouteLinesRouteParams.add("Lines", line);
            EMTRestClient.post(getRouteLinesRouteURL, getRouteLinesRouteParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    ArrayList<BusLineWayPoint> parsedWayPoints = EMTOpenDataParser.parseRouteLinesRoute(response);
                    ArrayList<BusLineWayPoint> currentWayWayPoints = new ArrayList<BusLineWayPoint>();
                    if (way == 1){
                        for(BusLineWayPoint wayPoint : parsedWayPoints){
                            if (wayPoint.getSecDetail() == 10 || wayPoint.getSecDetail() == 19){
                                currentWayWayPoints.add(wayPoint);
                            }
                        }
                    } else {
                        for(BusLineWayPoint wayPoint : parsedWayPoints){
                            if (wayPoint.getSecDetail() == 20 || wayPoint.getSecDetail() == 29){
                                currentWayWayPoints.add(wayPoint);
                            }
                        }
                    }
                    boolean indexFound = false;
                    int i = 0, firstIndex = 0, lastIndex = 0;

                    while (i<currentWayWayPoints.size() && !indexFound){
                        indexFound = Integer.valueOf(currentWayWayPoints.get(i).getNode()) == firstNode.getNode();
                        if (!indexFound){
                            i++;
                        }
                    }
                    if (indexFound){
                        firstIndex = i;
                    }
                    i = 0;
                    indexFound = false;
                    while (i<currentWayWayPoints.size() && !indexFound){
                        indexFound = Integer.valueOf(currentWayWayPoints.get(i).getNode()) == lastNode.getNode();
                        if (!indexFound){
                            i++;
                        }
                    }
                    if (indexFound){
                        lastIndex = i;
                    }
                    if (firstIndex < lastIndex) {
                        for (i = firstIndex; i <= lastIndex; i++) {
                            if (currentWayWayPoints.get(i).getSecDetail() == 19 ||currentWayWayPoints.get(i).getSecDetail() == 29){
                                results.add(currentWayWayPoints.get(i));
                            }
                        }
                    } else {
                        for (i = lastIndex; i <= firstIndex; i++) {
                            if (currentWayWayPoints.get(i).getSecDetail() == 19 ||currentWayWayPoints.get(i).getSecDetail() == 29){
                                results.add(currentWayWayPoints.get(i));
                            }                        }
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
            return results;
        }

        private ArrayList<BusLineWayPoint> requestOneTransferRouteWayPoints(ArrayList<Integer> lines, ArrayList<Integer> ways, final BusNetworkNode firstNode, final BusNetworkNode transferNode, final BusNetworkNode lastNode) {
            final ArrayList<BusLineWayPoint> results = new ArrayList<>();
            final int firstLine = lines.get(0);
            final int lastLine = lines.get(1);
            final int firstWay = ways.get(0);
            final int lastWay = ways.get(1);
            String line = "";
            if (firstLine == lastLine){
                line = String.valueOf(firstLine);
            } else {
                line = firstLine + "|" + lastLine;
            }
            String getRouteLinesRouteURL = Constants.EMT_API_BUS_SERVICE_URL + "GetRouteLinesRoute.php";
            RequestParams getRouteLinesRouteParams = new RequestParams();
            getRouteLinesRouteParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
            getRouteLinesRouteParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
            getRouteLinesRouteParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            getRouteLinesRouteParams.add("Lines", line);
            EMTRestClient.post(getRouteLinesRouteURL, getRouteLinesRouteParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    ArrayList<BusLineWayPoint> parsedWayPoints = EMTOpenDataParser.parseRouteLinesRoute(response);
                    ArrayList<BusLineWayPoint> firstRouteWayPoints = new ArrayList<BusLineWayPoint>();
                    ArrayList<BusLineWayPoint> lastRouteWayPoints = new ArrayList<BusLineWayPoint>();
                    for (BusLineWayPoint wayPoint : parsedWayPoints) {
                        if (wayPoint.getLine() == firstLine) {
                            firstRouteWayPoints.add(wayPoint);
                        } else if (wayPoint.getLine() == lastLine) {
                            lastRouteWayPoints.add(wayPoint);
                        }
                    }
                    ArrayList<BusLineWayPoint> firstRouteCurrentWayWayPoints = new ArrayList<BusLineWayPoint>();
                    if (firstWay == 1) {
                        for (BusLineWayPoint wayPoint : firstRouteWayPoints) {
                            if (wayPoint.getSecDetail() == 10 || wayPoint.getSecDetail() == 19) {
                                firstRouteCurrentWayWayPoints.add(wayPoint);
                            }
                        }
                    } else {
                        for (BusLineWayPoint wayPoint : firstRouteWayPoints) {
                            if (wayPoint.getSecDetail() == 20 || wayPoint.getSecDetail() == 29) {
                                firstRouteCurrentWayWayPoints.add(wayPoint);
                            }
                        }
                    }
                    ArrayList<BusLineWayPoint> lastRouteCurrentWayWayPoints = new ArrayList<BusLineWayPoint>();
                    if (lastWay == 1) {
                        for (BusLineWayPoint wayPoint : lastRouteWayPoints) {
                            if (wayPoint.getSecDetail() == 10 || wayPoint.getSecDetail() == 19) {
                                lastRouteCurrentWayWayPoints.add(wayPoint);
                            }
                        }
                    } else {
                        for (BusLineWayPoint wayPoint : lastRouteWayPoints) {
                            if (wayPoint.getSecDetail() == 20 || wayPoint.getSecDetail() == 29) {
                                lastRouteCurrentWayWayPoints.add(wayPoint);
                            }
                        }
                    }
                    int i = 0;
                    int firstIndex = 0, lastIndex = 0;
                    boolean indexFound = false;
                    while (i < firstRouteCurrentWayWayPoints.size() && !indexFound) {
                        indexFound = Integer.valueOf(firstRouteCurrentWayWayPoints.get(i).getNode()) == firstNode.getNode();
                        if (!indexFound) {
                            i++;
                        }
                    }
                    if (indexFound) {
                        firstIndex = i;
                    }
                    i = 0;
                    indexFound = false;
                    while (i < firstRouteCurrentWayWayPoints.size() && !indexFound) {
                        indexFound = Integer.valueOf(firstRouteCurrentWayWayPoints.get(i).getNode()) == transferNode.getNode() && i >= firstIndex;
                        if (!indexFound) {
                            i++;
                        }
                    }
                    if (indexFound) {
                        lastIndex = i;
                    }
                    i = 0;
                    indexFound = false;
                    for (i = firstIndex; i <= lastIndex; i++) {
                        if (firstRouteCurrentWayWayPoints.get(i).getSecDetail() == 19 || firstRouteCurrentWayWayPoints.get(i).getSecDetail() == 29) {
                            results.add(firstRouteCurrentWayWayPoints.get(i));
                        }
                    }
                    i = firstIndex = lastIndex = 0;
                    indexFound = false;
                    while (i < lastRouteCurrentWayWayPoints.size() && !indexFound) {
                        indexFound = Integer.valueOf(lastRouteCurrentWayWayPoints.get(i).getNode()) == transferNode.getNode();
                        if (!indexFound) {
                            i++;
                        }
                    }
                    if (indexFound) {
                        firstIndex = i;
                    }
                    i = 0;
                    indexFound = false;
                    while (i < lastRouteCurrentWayWayPoints.size() && !indexFound) {
                        indexFound = Integer.valueOf(lastRouteCurrentWayWayPoints.get(i).getNode()) == lastNode.getNode() && i >= firstIndex;
                        if (!indexFound) {
                            i++;
                        }
                    }
                    if (indexFound) {
                        lastIndex = i;
                    }
                    for (i = firstIndex; i <= lastIndex; i++) {
                        if (lastRouteCurrentWayWayPoints.get(i).getSecDetail() == 19 || lastRouteCurrentWayWayPoints.get(i).getSecDetail() == 29) {
                            results.add(lastRouteCurrentWayWayPoints.get(i));
                        }
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

            return results;
        }

    }

}
