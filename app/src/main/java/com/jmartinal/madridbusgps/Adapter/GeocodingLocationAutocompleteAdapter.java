package com.jmartinal.madridbusgps.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Fragments.NewRouteFragment;
import com.jmartinal.madridbusgps.WSMapQuest.JSONGeocodingParser;
import com.jmartinal.madridbusgps.R;
import com.jmartinal.madridbusgps.Utils.Constants;
import com.jmartinal.madridbusgps.Utils.Functions;


/**
 * Created by Jorge on 15/07/2015.
 * Adapter to filter user input text about the FROM and TO parameters from a route (NewRouteFragment)
 */
public class GeocodingLocationAutocompleteAdapter extends BaseAdapter implements Filterable {

    private Activity mActivity;
    private Context mContext;
    private List<GeocodingLocation> resultList = new ArrayList<GeocodingLocation>();
    private NewRouteFragment newRouteFragment;
    private GeocodingLocation location;


    public GeocodingLocationAutocompleteAdapter(Activity activity, NewRouteFragment fragment, GeocodingLocation location){
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
        this.newRouteFragment = fragment;
        this.location = location;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
                FilterResults filterResults = new FilterResults();
                if (constraint != null && location == null && (newRouteFragment.isStreetTyped() && !newRouteFragment.isGpsPressed())) {
                    List<GeocodingLocation> geocodingLocations = findLocationAddresses(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = geocodingLocations;
                    filterResults.count = geocodingLocations.size();
                }
                newRouteFragment.setGpsPressed(false);
                newRouteFragment.setStreetTyped(true);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<GeocodingLocation>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<GeocodingLocation> findLocationAddresses(Context context, String street) {
        if (Functions.isOnline(mActivity)){ // If connected, execute query. If not, inform the user
            try {
                resultList = new GeocodingTask().execute(street).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (resultList == null){
                Toast.makeText(mContext, "Se ha producido un error al buscar la dirección solicitada.", Toast.LENGTH_LONG).show();
            }
            return resultList;
        }else {
            Toast.makeText(context, "No se ha podido establecer conexion con el servidor", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private class GeocodingTask extends AsyncTask<String, Void, List<GeocodingLocation>> {

        private String query = "";
        private String street = "";

        @Override
        protected void onPreExecute() {
            newRouteFragment.setStreetTyped(true);
            super.onPreExecute();
        }

        @Override
        protected List<GeocodingLocation> doInBackground(String... params) {

            street = params[0].replace(" ", "+");
            HttpURLConnection httpURLConnection = null;
            JSONObject serverResponse = new JSONObject();
            List<GeocodingLocation> geocodingLocationList = null;

            try{
                query = Constants.GEOCODING_BASE_URL +
                        Constants.MAPQUEST_KEY +
                        Constants.GEOCODING_STREET +
                        street +
                        Constants.GEOCODING_COMMUNITY_FILTER +
                        Constants.GEOCODING_COUNTRY_FILTER +
                        Constants.GEOCODING_MAX_RESULTS;
                httpURLConnection = (HttpURLConnection) new URL(query).openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                serverResponse = JSONGeocodingParser.getJSONObject(inputStream);
                geocodingLocationList = JSONGeocodingParser.parseLocations(serverResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return geocodingLocationList;
        }

        @Override
        protected void onPostExecute(List<GeocodingLocation> s) {
            resultList = s;
            super.onPostExecute(s);
        }

    }

}
