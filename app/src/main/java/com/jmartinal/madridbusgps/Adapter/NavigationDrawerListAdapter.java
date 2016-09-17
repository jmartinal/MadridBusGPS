package com.jmartinal.madridbusgps.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.jmartinal.madridbusgps.Model.NavigationDrawerListSection;
import com.jmartinal.madridbusgps.R;

/**
 * Created by Jorge on 15/09/2015.
 * Adapter to show icons and text on every item belonging to the Navigation Drawer menu
 */
public class NavigationDrawerListAdapter extends ArrayAdapter<NavigationDrawerListSection> {

    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<NavigationDrawerListSection> mNavigationDrawerListSections;

    public NavigationDrawerListAdapter(Context context, int layoutResourceId, ArrayList<NavigationDrawerListSection> navigationDrawerListSections) {
        super(context, layoutResourceId, navigationDrawerListSections);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mNavigationDrawerListSections = navigationDrawerListSections;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IconListHolder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new IconListHolder();
//            holder.name = (TextView)row.findViewById(R.id.section_name);
//            holder.icon = (ImageView)row.findViewById(R.id.section_icon);

            row.setTag(holder);
        } else {
            holder = (IconListHolder) row.getTag();
        }

        final NavigationDrawerListSection navigationDrawerListSection = mNavigationDrawerListSections.get(position);
        holder.name.setText(navigationDrawerListSection.getName());
        holder.icon.setImageDrawable(navigationDrawerListSection.getIcon());

        final IconListHolder finalHolder = holder;

        return row;
    }

    static class IconListHolder {
        ImageView icon;
        TextView name;
    }
}
