package com.jmartinal.madridbusgps.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusSchedule;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.OnFragmentInteractionListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusScheduleTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusScheduleTableFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CHILD_NUMBER = "child_number";
    private static final String ARG_BUS_SCHEDULE = "bus_schedule";

    private int mSectionNumber, mChildNumber;
    private BusSchedule mBusSchedule;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @param childNumber Parameter 2.
     * @param busSchedule Parameter 3.
     * @return A new instance of fragment BusScheduleTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusScheduleTableFragment newInstance(int position, int childNumber, BusSchedule busSchedule) {
        BusScheduleTableFragment fragment = new BusScheduleTableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        args.putInt(ARG_CHILD_NUMBER, childNumber);
        args.putSerializable(ARG_BUS_SCHEDULE, busSchedule);
        fragment.setArguments(args);
        return fragment;
    }

    public BusScheduleTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            mChildNumber = getArguments().getInt(ARG_CHILD_NUMBER);
            mBusSchedule = (BusSchedule) getArguments().getSerializable(ARG_BUS_SCHEDULE);
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
        View rootView = inflater.inflate(R.layout.fragment_bus_schedule_table, container, false);

        // WORKABLE DAYS TABLE
        TextView workableTitle = (TextView) rootView.findViewById(R.id.workableTitle);
        workableTitle.setText(mBusSchedule.getTypeDays().get(1).getDayTypeId());
        TextView workableHeaderACaption = (TextView) rootView.findViewById(R.id.workableHeaderACaption);
        workableHeaderACaption.setText(mBusSchedule.getHeaderA());
        TextView workableHeaderBCaption = (TextView) rootView.findViewById(R.id.workableHeaderBCaption);
        workableHeaderBCaption.setText(mBusSchedule.getHeaderB());
        TextView workableHeaderA = (TextView) rootView.findViewById(R.id.workableHeaderA);
        workableHeaderA.setText(mBusSchedule.getTypeDays().get(1).getDirection1().getFrequencyDescription().replace("./", "."));
        TextView workableHeaderB = (TextView) rootView.findViewById(R.id.workableHeaderB);
        workableHeaderB.setText(mBusSchedule.getTypeDays().get(1).getDirection2().getFrequencyDescription().replace("./", "."));

        // SATURDAY DAYS TABLE
        TextView saturdayTitle = (TextView) rootView.findViewById(R.id.saturdayTitle);
        saturdayTitle.setText(mBusSchedule.getTypeDays().get(2).getDayTypeId());
        TextView saturdayHeaderACaption = (TextView) rootView.findViewById(R.id.saturdayHeaderACaption);
        saturdayHeaderACaption.setText(mBusSchedule.getHeaderA());
        TextView saturdayHeaderBCaption = (TextView) rootView.findViewById(R.id.saturdayHeaderBCaption);
        saturdayHeaderBCaption.setText(mBusSchedule.getHeaderB());
        TextView saturdayHeaderA = (TextView) rootView.findViewById(R.id.saturdayHeaderA);
        saturdayHeaderA.setText(mBusSchedule.getTypeDays().get(2).getDirection1().getFrequencyDescription().replace("./", "."));
        TextView saturdayHeaderB = (TextView) rootView.findViewById(R.id.saturdayHeaderB);
        saturdayHeaderB.setText(mBusSchedule.getTypeDays().get(2).getDirection2().getFrequencyDescription().replace("./", "."));

        // FESTIVE DAYS TABLE
        TextView festiveTitle = (TextView) rootView.findViewById(R.id.festiveTitle);
        festiveTitle.setText(mBusSchedule.getTypeDays().get(0).getDayTypeId());
        TextView festiveHeaderACaption = (TextView) rootView.findViewById(R.id.festiveHeaderACaption);
        festiveHeaderACaption.setText(mBusSchedule.getHeaderA());
        TextView festiveHeaderBCaption = (TextView) rootView.findViewById(R.id.festiveHeaderBCaption);
        festiveHeaderBCaption.setText(mBusSchedule.getHeaderB());
        TextView festiveHeaderA = (TextView) rootView.findViewById(R.id.festiveHeaderA);
        festiveHeaderA.setText(mBusSchedule.getTypeDays().get(0).getDirection1().getFrequencyDescription().replace("./", "."));
        TextView festiveHeaderB = (TextView) rootView.findViewById(R.id.festiveHeaderB);
        festiveHeaderB.setText(mBusSchedule.getTypeDays().get(0).getDirection2().getFrequencyDescription().replace("./", "."));

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

    private void restoreTableValues(){
//        tableTitle.setText(R.string.bus_schedule_select_line);
//        tableWorkablesFrom.setText(R.string.bus_schedule_empty_cell);
//        tableSaturdaysFrom.setText(R.string.bus_schedule_empty_cell);
//        tableFestivesFrom.setText(R.string.bus_schedule_empty_cell);
//        tableWorkablesTo.setText(R.string.bus_schedule_empty_cell);
//        tableSaturdaysTo.setText(R.string.bus_schedule_empty_cell);
//        tableFestivesTo.setText(R.string.bus_schedule_empty_cell);
    }

    private void refreshTableValues(BusLine busLine, ArrayList<BusSchedule> busSchedules){
//        tableTitle.setText(busLine.toString());
//        for(BusSchedule schedule : busSchedules){
//            String timeFirstA = schedule.getTimeFirstA().split(" ")[1].replaceFirst(":00", "");
//            String timeEndA = schedule.getTimeEndA().split(" ")[1].replaceFirst(":00", "");
//            String timeFirstB = schedule.getTimeFirstB().split(" ")[1].replaceFirst(":00", "");
//            String timeEndB = schedule.getTimeEndB().split(" ")[1].replaceFirst(":00", "");
//            if (schedule.getTypeDay().equals("LA")){
//                tableWorkablesFrom.setText(timeFirstA + " - " + timeEndA);
//                tableWorkablesTo.setText(timeFirstB + " - " + timeEndB);
//            } else if (schedule.getTypeDay().equals("SA")){
//                tableSaturdaysFrom.setText(timeFirstA + " - " + timeEndA);
//                tableSaturdaysTo.setText(timeFirstB + " - " + timeEndB);
//            } else if (schedule.getTypeDay().equals("FE")){
//                tableFestivesFrom.setText(timeFirstA + " - " + timeEndA);
//                tableFestivesTo.setText(timeFirstB + " - " + timeEndB);
//            }
//        }
    }


}
