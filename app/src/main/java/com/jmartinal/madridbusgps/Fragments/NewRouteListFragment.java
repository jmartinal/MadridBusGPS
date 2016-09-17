package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jmartinal.madridbusgps.Adapter.RouteListAdapter;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.Maneuver;
import com.jmartinal.madridbusgps.Model.Route;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRouteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRouteListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CHILD_NUMBER = "child_number";
    private static final String ARG_FROM = "from";
    private static final String ARG_TO = "to";
    private static final String ARG_ROUTE = "route";

    private int mSectionNumber, mChildNumber;
    private GeocodingLocation mFrom, mTo;
    private Route mRoute;

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
     * @return A new instance of fragment NewRouteListFragment.
     */
    public static NewRouteListFragment newInstance(int parentSectionNumber, int childNumber, GeocodingLocation from, GeocodingLocation to, Route route) {
        NewRouteListFragment fragment = new NewRouteListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, parentSectionNumber);
        args.putInt(ARG_CHILD_NUMBER, childNumber);
        args.putSerializable(ARG_FROM, from);
        args.putSerializable(ARG_TO, to);
        args.putSerializable(ARG_ROUTE, route);
        fragment.setArguments(args);
        return fragment;
    }

    public NewRouteListFragment() {
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
        if (savedInstanceState != null) {
            mFrom = (GeocodingLocation) savedInstanceState.getSerializable("mFrom");
            mTo = (GeocodingLocation) savedInstanceState.getSerializable("mTo");
            mRoute = (Route) savedInstanceState.getSerializable("mRoute");
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
        View rootView = inflater.inflate(R.layout.fragment_new_route_list, container, false);

        List<String> narratives = new ArrayList<>();
        ArrayList<Maneuver> maneuvers = new ArrayList<>();

        Maneuver startManeuver = new Maneuver(String.format(getResources().getString(R.string.route_start), mFrom.toString()), -20);
        narratives.add(startManeuver.getNarrative());
        maneuvers.add(startManeuver);

        for(int i=0; i<mRoute.getFirstWalkingRoute().getLegs().size(); i++){
            for(int j=0; j<mRoute.getFirstWalkingRoute().getLegs().get(i).getManeuvers().size()-1; j++){
                narratives.add(mRoute.getFirstWalkingRoute().getLegs().get(i).getManeuvers().get(j).getNarrative());
                maneuvers.add(mRoute.getFirstWalkingRoute().getLegs().get(i).getManeuvers().get(j));
            }
        }

        if (mRoute.getBusRoute() != null) {
            // Write Bus Narratives
            for (int i = 0; i < mRoute.getBusRoute().getManeuvers().size(); i++) {
                narratives.add(mRoute.getBusRoute().getManeuvers().get(i).getNarrative());
                maneuvers.add(mRoute.getBusRoute().getManeuvers().get(i));
            }
        }

        if (mRoute.getLastWalkingRoute() != null) {
            for (int i = 0; i < mRoute.getLastWalkingRoute().getLegs().size(); i++) {
                for (int j = 0; j < mRoute.getLastWalkingRoute().getLegs().get(i).getManeuvers().size() - 1; j++) {
                    narratives.add(mRoute.getLastWalkingRoute().getLegs().get(i).getManeuvers().get(j).getNarrative());
                    maneuvers.add(mRoute.getLastWalkingRoute().getLegs().get(i).getManeuvers().get(j));
                }
            }
        }

        Maneuver endManeuver = new Maneuver(String.format(getResources().getString(R.string.route_end), mTo.toString()), -10);
        narratives.add(endManeuver.getNarrative());
        maneuvers.add(endManeuver);

        ListView listView = (ListView) rootView.findViewById(R.id.maneuversListView);

        RouteListAdapter adapter = new RouteListAdapter(getActivity(), R.layout.route_list_adapter_item, maneuvers);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("mRoute", mRoute);
        outState.putSerializable("mFrom", mFrom);
        outState.putSerializable("mTo", mTo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_new_route_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_route_as_map:
                showAsMap(1, mFrom, mTo, mRoute);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAsMap(int position, GeocodingLocation from, GeocodingLocation to, Route route) {
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
