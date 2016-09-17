package com.jmartinal.madridbusgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.jmartinal.madridbusgps.Fragments.AboutAppFragment;
import com.jmartinal.madridbusgps.Fragments.BusLinesRefreshFragment;
import com.jmartinal.madridbusgps.Fragments.BusRoutesFragment;
import com.jmartinal.madridbusgps.Fragments.BusRoutesMapFragment;
import com.jmartinal.madridbusgps.Fragments.BusScheduleTableFragment;
import com.jmartinal.madridbusgps.Fragments.BusSchedulesFragment;
import com.jmartinal.madridbusgps.Fragments.HomeFragment;
import com.jmartinal.madridbusgps.Fragments.NewRouteFragment;
import com.jmartinal.madridbusgps.Fragments.NewRouteListFragment;
import com.jmartinal.madridbusgps.Fragments.NewRouteMapFragment;
import com.jmartinal.madridbusgps.Model.BusLine;
import com.jmartinal.madridbusgps.Model.BusNetwork;
import com.jmartinal.madridbusgps.Model.BusSchedule;
import com.jmartinal.madridbusgps.Model.GeocodingLocation;
import com.jmartinal.madridbusgps.Model.Route;
import com.jmartinal.madridbusgps.Utils.Constants;
import com.jmartinal.madridbusgps.Utils.CreateBusNetworkTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private String mTitle;
    private Toolbar mToolbar;
    private boolean mIsBackFromChildFragment;
    private File mBusLinesFile;
    private BusNetwork mBusNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = getResources().getString(R.string.section1);
        mToolbar.setTitle(mTitle);
        navigationView.setCheckedItem(R.id.nav_home);
        onFragmentInteraction(1, mBusNetwork);

        mIsBackFromChildFragment = false;

        if (savedInstanceState == null || !savedInstanceState.containsKey("busLines")){
            mBusLinesFile = new File(getExternalFilesDir(Constants.BUS_LINES_FILE_DIR), Constants.BUS_LINES_FILE_NAME);
            if (mBusLinesFile.exists()){
                new LoadingFileTask(this).execute();
            } else {
                new CreateBusNetworkTask(this, mBusNetwork).execute();
            }
        } else {
            mBusNetwork = (BusNetwork) savedInstanceState.getSerializable("busNetwork");
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("busNetwork", mBusNetwork);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof HomeFragment) {
            RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                background.setBackgroundResource(R.drawable.emt_wallpaper_portrait);
            } else {
                background.setBackgroundResource(R.drawable.emt_wallpaper_landscape);
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
            int position = fragment.getArguments().getInt("section_number");
            boolean isChild = fragment.getArguments().getInt("child_number") != 0;
            if (position == 1){
                // Backing from HomeFragment
                // Starting the "Desktop" activity instead of calling finish() will avoid the file load until we exit this app
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            } else {
            /* Backing from other fragment.
                - If backing from any drawerList fragment, redirect to HomeFragment
                - If not backing from any drawerList fragment, redirect to it's parent fragment
             */
                if (!isChild) {
                    position = 1;
                } else {
                    mIsBackFromChildFragment = true;
                }
                getFragmentManager().beginTransaction().remove(fragment).commit();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                onFragmentInteraction(position, mBusNetwork);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            onFragmentInteraction(1, (BusNetwork) null);
        } else
        if (id == R.id.nav_new_route) {
            onFragmentInteraction(2, mBusNetwork);
        } else if (id == R.id.nav_routes) {
            onFragmentInteraction(3, mBusNetwork);
        } else if (id == R.id.nav_schedules) {
            onFragmentInteraction(4, mBusNetwork);
        } else if (id == R.id.nav_bus_refresh) {
            onFragmentInteraction(5, mBusNetwork);
        } else if (id == R.id.nav_about) {
            onFragmentInteraction(6, (BusNetwork) null);
        } else if (id == R.id.nav_exit) {
            onFragmentInteraction(7, mBusNetwork);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // onFragmentInteraction for app's side menu's options
    @Override
    public void onFragmentInteraction(int position, BusNetwork busNetwork) {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.container);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        switch (position){
            case 1: // Home
                mTitle = getResources().getString(R.string.section1);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_home);
                Fragment homeFragment = HomeFragment.newInstance(position);
                FragmentTransaction homeTransaction = getFragmentManager().beginTransaction();
                homeTransaction.replace(R.id.container, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commitAllowingStateLoss();
                break;
            case 2: // New route
                mTitle = getResources().getString(R.string.section2);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_new_route);
                if (!(currentFragment instanceof NewRouteFragment) || mIsBackFromChildFragment) {
                    Fragment newRouteFragment = NewRouteFragment.newInstance(position, busNetwork);
                    FragmentTransaction newRouteTransaction = getFragmentManager().beginTransaction();
                    newRouteTransaction.replace(R.id.container, newRouteFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                    mIsBackFromChildFragment = false;
                }
                break;
            case 3: // Bus routes
                mTitle = getResources().getString(R.string.section3);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_routes);
                Fragment busRoutesFragment = BusRoutesFragment.newInstance(position, busNetwork);
                FragmentTransaction busRoutesTransaction = getFragmentManager().beginTransaction();
                busRoutesTransaction.replace(R.id.container, busRoutesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4: // Bus schedules
                mTitle = getResources().getString(R.string.section4);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_schedules);
                Fragment schedulesFragment = BusSchedulesFragment.newInstance(position, busNetwork);
                FragmentTransaction schedulesTransaction = getFragmentManager().beginTransaction();
                schedulesTransaction.replace(R.id.container, schedulesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case 5: // Refresh bus lines' file
                mTitle = getResources().getString(R.string.section5);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_bus_refresh);
                Fragment refreshBusLinesFragment = BusLinesRefreshFragment.newInstance(position, busNetwork);
                FragmentTransaction refreshTransaction = getFragmentManager().beginTransaction();
                refreshTransaction.replace(R.id.container, refreshBusLinesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                break;
            case 6: // About the app
                mTitle = getResources().getString(R.string.section6);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_about);
                Fragment aboutAppFragment = AboutAppFragment.newInstance(position);
                FragmentTransaction aboutAppTransaction = getFragmentManager().beginTransaction();
                aboutAppTransaction.replace(R.id.container, aboutAppFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            case 7: // Exit
                navigationView.setCheckedItem(R.id.nav_exit);
                finish();
                break;
        }
    }

    // onFragmentInteraction for new route fragment's children
    @Override
    public void onFragmentInteraction(int childNumber, GeocodingLocation from, GeocodingLocation to, Route route) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        switch (childNumber){
            case 1: // Display route on map
                mTitle = getResources().getString(R.string.section2_1);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_new_route);
                Fragment newRouteMapFragment = NewRouteMapFragment.newInstance(2, childNumber, from, to, route);
                FragmentTransaction newRouteMapTransaction = getFragmentManager().beginTransaction();
                newRouteMapTransaction.replace(R.id.container, newRouteMapFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                break;
            case 2: // Show route as list
                mTitle = getResources().getString(R.string.section2_2);
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_new_route);
                Fragment newRouteListFragment = NewRouteListFragment.newInstance(2, childNumber, from, to, route);
                FragmentTransaction newRouteListTransaction = getFragmentManager().beginTransaction();
                newRouteListTransaction.replace(R.id.container, newRouteListFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    // onFragmentInteraction for bus routes fragment's children
    @Override
    public void onFragmentInteraction(int childNumber, BusLine busLine, boolean showDeparture, boolean showDetour, boolean showStops) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        switch (childNumber) {
            case 1: // Show bus route on map
                mTitle = busLine.toString();
                mToolbar.setTitle(mTitle);
                navigationView.setCheckedItem(R.id.nav_routes);
                Fragment busRoutesMapFragment = BusRoutesMapFragment.newInstance(3, childNumber, busLine, showDeparture, showDetour, showStops);
                FragmentTransaction busRoutesMapTransaction = getFragmentManager().beginTransaction();
                busRoutesMapTransaction.replace(R.id.container, busRoutesMapFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    // onFragmentInteraction for bus schedules fragment's children
    @Override
    public void onFragmentInteraction(int childNumber, BusSchedule busSchedule) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mTitle = "Línea " + busSchedule.getLineId() + ": " + busSchedule.getHeaderA() + " - " + busSchedule.getHeaderB();
        mToolbar.setTitle(mTitle);
        navigationView.setCheckedItem(R.id.nav_schedules);
        Fragment busScheduleTableFragment = BusScheduleTableFragment.newInstance(4, childNumber, busSchedule);
        FragmentTransaction busScheduleTableTransaction = getFragmentManager().beginTransaction();
        busScheduleTableTransaction.replace(R.id.container, busScheduleTableFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    private class LoadingFileTask extends AsyncTask<Void, Void, BusNetwork> {

        private Dialog dialog;
        private Activity activity;
        private ArrayList<Exception> exceptions;

        public LoadingFileTask(Activity activity) {
            this.dialog = new Dialog(activity, R.style.LoadingDialogTheme);
            this.activity = activity;
            this.exceptions = null;
        }

        @Override
        protected void onPreExecute() {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            dialog.getWindow();
            dialog.setContentView(R.layout.loading_route_message);
            dialog.setTitle(R.string.loading_file_title);
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BusNetwork loadedNetwork) {
            dialog.dismiss();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            if (this.exceptions != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.ErrorDialogTheme);
                builder.setTitle(R.string.loading_file_error_title);
                builder.setMessage(R.string.loading_file_error_message);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        activity.finish();
                    }
                });
                dialog = builder.create();
                dialog.show();
                mBusLinesFile.delete();
            } else {
                mBusNetwork = loadedNetwork;
            }
            super.onPostExecute(loadedNetwork);
        }

        @Override
        protected BusNetwork doInBackground(Void... params) {
            BusNetwork loadedNetwork;
            FileInputStream fis;
            try {
                fis = new FileInputStream(mBusLinesFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                loadedNetwork = (BusNetwork) ois.readObject();
                ois.close();
            } catch (Exception e) {
                this.exceptions = new ArrayList<>();
                exceptions.add(e);
                return null;
            }

            return loadedNetwork;
        }

    }

    public BusNetwork getmBusNetwork() {
        return mBusNetwork;
    }

    public void setmBusNetwork(BusNetwork mBusNetwork) {
        this.mBusNetwork = mBusNetwork;
    }
}
