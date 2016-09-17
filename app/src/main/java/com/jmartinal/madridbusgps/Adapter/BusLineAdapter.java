package com.jmartinal.madridbusgps.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jmartinal.madridbusgps.Fragments.BusRoutesFragment;
import com.jmartinal.madridbusgps.Fragments.BusSchedulesFragment;
import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.R;

import java.util.ArrayList;

/**
 * Created by Jorge on 10/11/2015.
 */
public class BusLineAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private BusRoutesFragment mBusRoutesFragment;
    private BusSchedulesFragment mBusSchedulesFragment;
    private BusLine mBusLine;
    private ArrayList<BusLine> mBusLines;
    private ArrayList<BusLine> mResultList = new ArrayList<>();

    public BusLineAdapter(Context context, Fragment fragment, ArrayList<BusLine> busLines, BusLine busLine) {
        this.mContext = context;
        this.mBusRoutesFragment = fragment instanceof BusRoutesFragment ? (BusRoutesFragment) fragment : null;
        this.mBusSchedulesFragment = fragment instanceof BusSchedulesFragment ? (BusSchedulesFragment) fragment : null;
        this.mBusLines = busLines;
        this.mBusLine = busLine;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(mResultList.get(position).getLine());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.autocomplete_dropdown_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.autocompleteListItemText)).setText(getItem(position).toString());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                boolean isBusRouteCorrect = (mBusRoutesFragment != null && mBusRoutesFragment.isLineTyped());
                boolean isBusScheduleCorrect = (mBusSchedulesFragment != null && mBusSchedulesFragment.isLineTyped());
                if (constraint != null && mBusLine == null && (isBusRouteCorrect || isBusScheduleCorrect)){
                    ArrayList<BusLine> busLines = new ArrayList<>();
                    for (BusLine busLine : mBusLines){
                        String normalizedBusLineName = normalize(busLine.toString());
                        String normalizedConstraint = normalize(constraint.toString());
                        if (normalizedBusLineName.contains(normalizedConstraint)){
                            busLines.add(busLine);
                        }
                    }
                    results.values = busLines;
                    results.count = busLines.size();
                }
                if (mBusRoutesFragment != null){
                    mBusRoutesFragment.setLineTyped(true);
                }
                if (mBusSchedulesFragment != null){
                    mBusSchedulesFragment.setLineTyped(true);
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResultList = (ArrayList<BusLine>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private String normalize(String sentence){
        String result = sentence;
        result = result.toUpperCase();
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        for (int i=0; i<original.length(); i++) {
            result = result.replace(original.charAt(i), ascii.charAt(i));
        }
        return result;
    }

}
