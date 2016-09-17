package com.jmartinal.madridbusgps.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.Utils.Constants;
import com.jmartinal.madridbusgps.Utils.CreateBusNetworkTask;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusLinesRefreshFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusLinesRefreshFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_BUS_NETWORK = "bus_lines";

    private int mSectionNumber;
    private BusNetwork mBusNetwork;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @param busNetwork Parameter 2.
     * @return A new instance of fragment RefreshBusLinesFragment.
     */
    public static BusLinesRefreshFragment newInstance(int sectionNumber, BusNetwork busNetwork) {
        BusLinesRefreshFragment fragment = new BusLinesRefreshFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_BUS_NETWORK, busNetwork);
        fragment.setArguments(args);
        return fragment;
    }

    public BusLinesRefreshFragment() {
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
            mSectionNumber = savedInstanceState.getInt(ARG_SECTION_NUMBER);
            mBusNetwork = (BusNetwork) savedInstanceState.getSerializable(ARG_BUS_NETWORK);
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
        outState.putInt(ARG_SECTION_NUMBER, mSectionNumber);
        outState.putSerializable(ARG_BUS_NETWORK, mBusNetwork);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_refresh_bus_lines, container, false);

        TextView errorText = (TextView) rootView.findViewById(R.id.lastRefreshError);
        final File busLinesFile = new File(getActivity().getExternalFilesDir(Constants.BUS_LINES_FILE_DIR), Constants.BUS_LINES_FILE_NAME);
        GregorianCalendar gregorianDate = new GregorianCalendar();
        Date date = new Date();
        if (busLinesFile.exists()){
            date = new Date(busLinesFile.lastModified());
            errorText.setVisibility(View.INVISIBLE);
        } else {
            errorText.setVisibility(View.VISIBLE);
        }
        gregorianDate.setTime(date);
        DatePicker lastRefreshDate = (DatePicker) rootView.findViewById(R.id.lastRefreshDate);
        lastRefreshDate.setMinDate(date.getTime());
        lastRefreshDate.setMaxDate(date.getTime());
        lastRefreshDate.updateDate(gregorianDate.get(Calendar.YEAR), gregorianDate.get(Calendar.MONTH), gregorianDate.get(Calendar.DAY_OF_MONTH));

        FloatingActionButton refreshFAB = (FloatingActionButton) rootView.findViewById(R.id.refreshBusLinesFab);
        refreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busLinesFile.delete();
                new CreateBusNetworkTask(getActivity(), mBusNetwork).execute();
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

}
