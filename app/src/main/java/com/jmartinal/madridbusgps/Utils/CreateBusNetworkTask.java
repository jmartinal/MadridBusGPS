package com.jmartinal.madridbusgps.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jmartinal.madridbusgps.Fragments.BusLinesRefreshFragment;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTRestClient;
import com.jmartinal.madridbusgps.Fragments.HomeFragment;
import com.jmartinal.madridbusgps.MainActivity;
import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusLineStop;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.Model.BusNetworkNode;
import com.jmartinal.madridbusgps.WSEMTOpenData.EMTOpenDataParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import com.jmartinal.madridbusgps.R;

/**
 * Created by Jorge on 24/10/2015.
 */
public class CreateBusNetworkTask extends AsyncTask<Void, Integer, BusNetwork> {


    private Activity activity;
    private Dialog dialog;
    private BusNetwork busNetwork;
    private ArrayList<BusLine> parsedLines;
    private ArrayList<BusLineStop> parsedStops;
    private ArrayList<BusNetworkNode> parsedNodes;
    private ArrayList<Exception> exceptions;
    private File directory, file;

    public CreateBusNetworkTask(Activity activity, BusNetwork busNetwork) {
        this.activity = activity;
        this.dialog = new Dialog(activity, R.style.LoadingDialogTheme);
        this.busNetwork = busNetwork;
        this.directory = activity.getExternalFilesDir(Constants.BUS_LINES_FILE_DIR);
        this.file = new File(directory, Constants.BUS_LINES_FILE_NAME);
    }

    @Override
    protected void onPreExecute() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        dialog.getWindow();
        dialog.setContentView(R.layout.loading_bus_message);
        dialog.setTitle(R.string.loading_bus_message_title);
        dialog.setCancelable(false);
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected BusNetwork doInBackground(Void... params) {

        publishProgress(1, 0, 0);
        requestListLines(0);

        publishProgress(2, 0, 0);
        requestRouteLines(0);
        requestNodesLines(0);

        // When every bus line's info has been downloaded, save to file
        try {
            publishProgress(3, 0, 0);
            busNetwork = new BusNetwork(parsedLines, parsedStops, parsedNodes);
            publishProgress(4, 0, 0);
            file.createNewFile();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(busNetwork);
            outputStream.close();
        } catch (Exception e){
            exceptions = new ArrayList<Exception>();
            e.printStackTrace();
            exceptions.add(e);
        }
        return busNetwork;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressStep = values[0];
        TextView loadingMessage = (TextView) dialog.findViewById(R.id.loading_bus_message_text);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.loading_bus_message_progressBar);
        switch (progressStep){
            case 1: // Downloading list lines
                loadingMessage.setText(activity.getString(R.string.loading_bus_message_lines));
                break;
            case 2: // Downloading lines' routes
                loadingMessage.setText(activity.getResources().getString(R.string.loading_bus_message_stops));
                break;
            case 3: // Creating bus network
                loadingMessage.setText(activity.getString(R.string.loading_bus_message_network));
                break;
            case 4: // Saving data to file
                loadingMessage.setText(activity.getString(R.string.loading_bus_message_file));
                break;
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(BusNetwork busNetwork) {
        dialog.dismiss();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        if (exceptions != null) {
            file.delete();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.ErrorDialogTheme);
            builder.setCancelable(false);
            builder.setTitle(R.string.loading_bus_error_title);
            builder.setMessage(R.string.loading_bus_error_message);
            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    activity.finish();
                }
            });
            dialog = builder.create();
            dialog.show();
        } else {
            Fragment currentFragment = activity.getFragmentManager().findFragmentById(R.id.container);
            if (currentFragment instanceof HomeFragment){
                ((MainActivity)activity).setmBusNetwork(busNetwork);
                ((MainActivity) activity).onFragmentInteraction(1, busNetwork);
            } else if (currentFragment instanceof BusLinesRefreshFragment){
                ((MainActivity)activity).setmBusNetwork(busNetwork);
                ((MainActivity) activity).onFragmentInteraction(5, busNetwork);
            }
        }
        super.onPostExecute(busNetwork);
    }

    private void requestListLines(final int nTries) {
        RequestParams getListLinesParams = new RequestParams();
        getListLinesParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
        getListLinesParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
        getListLinesParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        String getListLinesURL = Constants.EMT_API_BUS_SERVICE_URL + "GetListLines.php";
        EMTRestClient.post(getListLinesURL, getListLinesParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parsedLines = EMTOpenDataParser.parseListLines(response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (nTries == 0) {
                    requestListLines(nTries + 1);
                }
            }
        });

    }

    private void requestRouteLines(final int nTries){
        RequestParams getRouteLinesParams = new RequestParams();
        getRouteLinesParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
        getRouteLinesParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
        getRouteLinesParams.add("SelectDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        String getRouteLinesURL = Constants.EMT_API_BUS_SERVICE_URL + "GetRouteLines.php";
        EMTRestClient.post(getRouteLinesURL, getRouteLinesParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parsedStops = EMTOpenDataParser.parseRouteLines(response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (nTries == 0){
                    requestRouteLines(nTries + 1);
                }
            }
        });
    }

    private void requestNodesLines(final int nTries){
        String getNodesLinesURL = Constants.EMT_API_BUS_SERVICE_URL + "GetNodesLines.php";
        RequestParams getNodesLinesParams = new RequestParams();
        getNodesLinesParams.add(Constants.EMT_API_ID_CLIENT_KEY, Constants.EMT_API_ID_CLIENT_VALUE);
        getNodesLinesParams.add(Constants.EMT_API_PASSKEY_KEY, Constants.EMT_API_PASSKEY_VALUE);
        EMTRestClient.post(getNodesLinesURL, getNodesLinesParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parsedNodes = EMTOpenDataParser.parseNodesLines(response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (nTries == 0){
                    requestNodesLines(nTries+1);
                }
            }
        });

    }

}
