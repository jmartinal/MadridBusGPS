package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jmartinal.madridbusgps.Adapter.BusLineAdapter;
import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTOpenDataParser;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.Utils.Constants;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTRestClient;
import com.jmartinal.madridbusgps.Utils.Functions;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusRoutesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusRoutesFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_BUS_NETWORK = "bus_network";

    private int mSectionNumber;
    private BusNetwork mBusNetwork;

    private BusLine mSelectedBusLine = null;
    private boolean lineTyped;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter section_number.
     * @param busNetwork Parameter bus_lines.
     * @return A new instance of fragment BusRoutesFragment.
     */
    public static BusRoutesFragment newInstance(int sectionNumber, BusNetwork busNetwork) {
        BusRoutesFragment fragment = new BusRoutesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_BUS_NETWORK, busNetwork);
        fragment.setArguments(args);
        return fragment;
    }

    public BusRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            mBusNetwork = (BusNetwork) getArguments().getSerializable(ARG_BUS_NETWORK);
            setLineTyped(true);
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
        final View rootView = inflater.inflate(R.layout.fragment_bus_routes, container, false);

        final AutoCompleteTextView busLineText = (AutoCompleteTextView) rootView.findViewById(R.id.busRouteAutocomplete);

        busLineText.setHint(String.format(rootView.getResources().getString(R.string.bus_route_example), mBusNetwork.getBusLines().get(0).toString()));
        BusLineAdapter adapter = new BusLineAdapter(getActivity(), this, mBusNetwork.getBusLines(), mSelectedBusLine);
        busLineText.setAdapter(adapter);

        busLineText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isLineTyped()) {
                    mSelectedBusLine = null;
                }
            }
        });

        busLineText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setLineTyped(false);
                mSelectedBusLine = (BusLine) parent.getItemAtPosition(position);
                Functions.hideKeyboard(getActivity(), busLineText);
            }
        });

        busLineText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Functions.hideKeyboard(getActivity(), v);
                }
            }
        });

        final CheckBox showDeparture, showDetour, showStops;
        showDeparture = (CheckBox) rootView.findViewById(R.id.departureCheckBox);
        showDetour = (CheckBox) rootView.findViewById(R.id.detourCheckBox);
        showStops = (CheckBox) rootView.findViewById(R.id.stopsCheckBox);

        showDeparture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !showDetour.isChecked()){
                    showStops.setChecked(false);
                }
            }
        });

        showDetour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !showDeparture.isChecked()){
                    showStops.setChecked(false);
                }
            }
        });

        showStops.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && (!showDeparture.isChecked() && !showDetour.isChecked())){
                    showStops.setChecked(false);
                }
            }
        });

        FloatingActionButton showMapButton = (FloatingActionButton) rootView.findViewById(R.id.busRouteMapFab);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showDeparture.isChecked() && ! showDetour.isChecked() && !showStops.isChecked()){
                    Toast.makeText(rootView.getContext(), R.string.bus_route_nothing_to_show, Toast.LENGTH_LONG).show();
                } else {
                    if (mSelectedBusLine == null){
                        Toast.makeText(rootView.getContext(), R.string.bus_route_select_line, Toast.LENGTH_LONG).show();
                    } else {
                        new RouteLinesRouteTask(mSelectedBusLine, showDeparture.isChecked(), showDetour.isChecked(), showStops.isChecked()).execute();
                    }
                }
            }
        });

        return rootView;
    }

    public void onShowMapButtonPressed(BusLine busLine, final boolean showDeparture, final boolean showDetour, final boolean showStops) {
        if (mListener != null) {
            mListener.onFragmentInteraction(1, mSelectedBusLine, showDeparture, showDetour, showStops);
        }
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

    public boolean isLineTyped() {
        return lineTyped;
    }

    public void setLineTyped(boolean lineTyped) {
        this.lineTyped = lineTyped;
    }

    private class RouteLinesRouteTask extends AsyncTask<Void, Void, BusLine> {

        private BusLine line, parsedBusLine;
        private Dialog dialog;
        private boolean showDeparture, showDetour, showStops;

        public RouteLinesRouteTask(BusLine line, boolean showDeparture, boolean showDetour, boolean showStops) {
            this.line = line;
            this.showDeparture = showDeparture;
            this.showDetour = showDetour;
            this.showStops = showStops;
            this.dialog = new Dialog(getActivity(), R.style.LoadingDialogTheme);
        }

        @Override
        protected void onPreExecute() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            dialog.getWindow();
            dialog.setContentView(R.layout.loading_route_message);
            dialog.setTitle(R.string.bus_schedule_loading_title);
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BusLine busLine) {
            dialog.dismiss();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            if (parsedBusLine != null) {
                onShowMapButtonPressed(busLine, showDeparture, showDetour, showStops);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ErrorDialogTheme);
                builder.setTitle(R.string.bus_schedule_error_title);
                builder.setMessage(R.string.operation_error_message);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(busLine);
        }

        @Override
        protected BusLine doInBackground(Void... params) {
            // Get line's route from WS
            String getRouteLinesRouteURL = Constants.EMT_API_BUS_SERVICE_URL + "GetRouteLinesRoute.php";
            RequestParams getRouteLinesRouteParams = new RequestParams();
            getRouteLinesRouteParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
            getRouteLinesRouteParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
            getRouteLinesRouteParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            getRouteLinesRouteParams.add("Lines", String.valueOf(line.getLine()));
            EMTRestClient.post(getRouteLinesRouteURL, getRouteLinesRouteParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    line.setWayPoints(EMTOpenDataParser.parseRouteLinesRoute(response));
                    parsedBusLine = line;
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
            return parsedBusLine;
        }
    }
}
