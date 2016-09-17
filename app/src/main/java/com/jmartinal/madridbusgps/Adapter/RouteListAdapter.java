package com.jmartinal.madridbusgps.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.jmartinal.madridbusgps.Model.DirectionsManeuver;
import com.jmartinal.madridbusgps.Model.Maneuver;
import com.jmartinal.madridbusgps.R;

/**
 * Created by Jorge on 15/09/2015.
 * Adapter to show icons and text on every item belonging to the Navigation Drawer menu
 */
public class RouteListAdapter extends ArrayAdapter<Maneuver> {

    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<Maneuver> mManeuvers;

    public RouteListAdapter(Context context, int layoutResourceId, ArrayList<Maneuver> maneuvers) {
        super(context, layoutResourceId, maneuvers);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mManeuvers = maneuvers;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IconListHolder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new IconListHolder();
            holder.name = (TextView)row.findViewById(R.id.maneuver_navigation);
            holder.icon = (ImageView)row.findViewById(R.id.maneuver_icon);

            row.setTag(holder);
        } else {
            holder = (IconListHolder) row.getTag();
        }

        final Maneuver maneuver= mManeuvers.get(position);
        Drawable icon = null;
        /**
         * Mapquest  offers 23 turn types, but only some of them are used on pedestrian routes since
         * pedestrian routes ignore turn restrictions.
         */
        switch (maneuver.getTurnType()){
            case -20://route start
                icon = null;
                break;
            case -10://route end
                icon = null;
                break;
            case 0: //straight
                icon = mContext.getResources().getDrawable(R.mipmap.ic_straight);
                break;
            case 1: //slight right
                icon = mContext.getResources().getDrawable(R.mipmap.ic_slight_right);
                break;
            case 2: //right
                icon = mContext.getResources().getDrawable(R.mipmap.ic_right);
                break;
            case 3: //sharp right
                icon = mContext.getResources().getDrawable(R.mipmap.ic_sharp_right);
                break;
            case 5: //sharp left
                icon = mContext.getResources().getDrawable(R.mipmap.ic_sharp_left);
                break;
            case 6: //left
                icon = mContext.getResources().getDrawable(R.mipmap.ic_left);
                break;
            case 7://slight left
                icon = mContext.getResources().getDrawable(R.mipmap.ic_slight_left);
                break;
            case 16://fork right
                icon = mContext.getResources().getDrawable(R.mipmap.ic_fork_right);
                break;
            case 17://fork left
                icon = mContext.getResources().getDrawable(R.mipmap.ic_fork_left);
                break;
            case 100://bus entry
                icon = mContext.getResources().getDrawable(R.mipmap.ic_bus_entry);
                break;
            case 110://bus transfer
                icon = mContext.getResources().getDrawable(R.mipmap.ic_bus_transfer);
                break;
            case 120://bus exit
                icon = mContext.getResources().getDrawable(R.mipmap.ic_bus_exit);
                break;

        }
        holder.name.setText(maneuver.getNarrative());
        holder.icon.setImageDrawable(icon);

        return row;
    }

    static class IconListHolder {
        ImageView icon;
        TextView name;
    }
}
