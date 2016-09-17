package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jmartinal.madridbusgps.Adapter.BusLineAdapter;
import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.Model.BusSchedule;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusSchedulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusSchedulesFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_BUS_NETWORK = "bus__network";

    private int mSectionNumber;
    private BusNetwork mBusNetwork;

    private BusLine mSelectedBusLine = null;
    private String mDate = "";
    private boolean lineTyped;

    private AutoCompleteTextView busLineText;
    private TextView tableTitle;
    private TextView tableFrom, tableWorkablesFrom, tableSaturdaysFrom, tableFestivesFrom;
    private TextView tableTo, tableWorkablesTo, tableSaturdaysTo, tableFestivesTo;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter section_number.
     * @param busNetwork Parameter bus_lines.
     * @return A new instance of fragment SchedulesFragment.
     */
    public static BusSchedulesFragment newInstance(int sectionNumber, BusNetwork busNetwork) {
        BusSchedulesFragment fragment = new BusSchedulesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_BUS_NETWORK, busNetwork);
        fragment.setArguments(args);
        return fragment;
    }

    public BusSchedulesFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_bus_schedules, container, false);

        busLineText = (AutoCompleteTextView) rootView.findViewById(R.id.busScheduleAutocomplete);
        busLineText.setHint(String.format(rootView.getResources().getString(R.string.bus_route_example), mBusNetwork.getBusLines().get(0).toString()));
        BusLineAdapter adapter = new BusLineAdapter(getActivity(), this, mBusNetwork.getBusLines(), mSelectedBusLine);
        busLineText.setAdapter(adapter);

        busLineText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isLineTyped()) {
                    mSelectedBusLine = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                if (!hasFocus) {
                    Functions.hideKeyboard(getActivity(), v);
                }
            }
        });

        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.busScheduleDatePicker);
        datePicker.setEnabled(true);
        Date currentDate = new Date();
        GregorianCalendar gregorianCurrent = new GregorianCalendar();
        gregorianCurrent.setTime(currentDate);
        int currentYear = gregorianCurrent.get(Calendar.YEAR);
        int currentMonth = gregorianCurrent.get(Calendar.MONTH);
        int currentDay = gregorianCurrent.get(Calendar.DAY_OF_MONTH);

        mDate = new SimpleDateFormat("dd/MM/yy").format(new Date(currentYear, currentMonth, currentDay));

        datePicker.setMinDate(new GregorianCalendar(currentYear, currentMonth-1, currentDay).getTimeInMillis());
        datePicker.setMaxDate(new GregorianCalendar(currentYear, currentMonth+1, currentDay).getTimeInMillis());

        datePicker.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new SimpleDateFormat("dd/MM/yy").format(new Date(year, monthOfYear, dayOfMonth));
            }
        });

        FloatingActionButton searchFAB = (FloatingActionButton) rootView.findViewById(R.id.busScheduleFab);
        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedBusLine != null){
                    new ScheduleTask(mSelectedBusLine, mDate).execute();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.bus_schedule_select_line, Toast.LENGTH_LONG).show();
                }
            }
        });

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

    public void showBusSchedule(int position, BusSchedule busSchedule) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position, busSchedule);
        }
    }

    public boolean isLineTyped() {
        return lineTyped;
    }

    public void setLineTyped(boolean lineTyped) {
        this.lineTyped = lineTyped;
    }

    private class ScheduleTask extends AsyncTask<Void, Void, BusSchedule>{

        private BusLine busLine;
        private String date;
        private BusSchedule parsedSchedule;
        private Dialog dialog;

        public ScheduleTask(BusLine busLine, String date) {
            this.busLine = busLine;
            this.date = date;
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
        protected void onPostExecute(BusSchedule busSchedule) {
            dialog.dismiss();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            if (parsedSchedule != null) {
                showBusSchedule(1, busSchedule);
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
            }            super.onPostExecute(busSchedule);
        }

        @Override
        protected BusSchedule doInBackground(Void... params) {
            final RequestParams requestParams = new RequestParams();
            requestParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
            requestParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
            requestParams.add("fecha", this.date);
            requestParams.add("line", String.valueOf(this.busLine.getLine()));
            String scheduleURL = Constants.EMT_API_GEO_SERVICE_URL + "GetInfoLineExtend.php";
            EMTRestClient.post(scheduleURL, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    parsedSchedule = EMTOpenDataParser.parseInfoLineExtend(response);
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
            return parsedSchedule;
        }
    }

}
