package com.example.ahmed.walkgraph.notifications;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.local.prefs.WalkPreference;
import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/14/17.
 *
 * LocationService Class to get user location updates in background
 * and save to DB when appropriate
 */
public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private LocationManager manager;
    private static final int locationDistance = 0;
    private static Location theLocation;
    private static List<Location> locationList = new ArrayList<>();

    @Inject
    GraphDAO graphDAO;

    @Inject
    WalkPreference preference;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * inject dependencies into service
     * before using injected fields to prevent NPE
     *
     * call to initializeManager, also prevents NPE
     *
     * Preference returns the location interval in minutes,
     * each minute is equal to 60,000 milliseconds
     *
     * request location updates from manager;
     * param String-Constant Provider - COARSE - NetworkProvide/CellTowers,
     * FINE - GPS Provider
     * param long minimum time between updates
     * param float minimum distance in metres between updates,
     * set here to zero to disregard distance in location updates
     * param - locationListener whose on location changed method
     * will be called with every change in location
     *
     * Both COARSE and FINE are used, when GPS is not enabled or
     * location is tuned down to 'device only', location service falls back to COARSE
     *
     * Both try blocks catch for securityException - mostly cos of denied permission
     * and IllegalArgumentException
     */

    @Override
    public void onCreate() {
        ((App)getApplicationContext()).getComponent().inject(this);
        Log.d(TAG, "Entered onCreate");
        initializeManager();

        long locationInterval = preference.pollingInterval() * 60000;

        try {
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    locationInterval,
                    locationDistance,
                    locationListeners[1]
            );
        } catch (SecurityException sec) {
            Log.d(TAG, "Permission possibly denied", sec);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "IllegalArgumentException" + ex.getMessage(), ex);
        }

        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    locationInterval,
                    locationDistance,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "Permission possibly denied", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "IllegalArgumentException" + ex.getMessage());

        }
    }

    /**
     * Location listeners are removed from manager before the service is destroyed
     */

    @Override
    public void onDestroy(){
        super.onDestroy();

        Log.d(TAG, "Entered onDestroy");

        if (manager != null){
            for (LocationListener listener : locationListeners) {
                manager.removeUpdates(listener);
            }
        }
    }

    /**
     * Initialized the LocationManager fields,
     * only when the field has'nt been initialized before i.e is not null
     */

    private void initializeManager(){
        Log.d(TAG, "Initializing manager");
        if (manager == null){
            manager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
        }
    }
    /**
     * @return int START_STICKY to allow the service to be
     * recreated when the process had been killed
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startIds){
        Log.d(TAG, "On start command started");
        super.onStartCommand(intent, flags, startIds);
        return START_STICKY;
    }

    /**
     * Show the user notification about the day's location history
     */
    public void showNotification() {
        //todo build notification and send
    }

    /**
     * Location Listener that receives new location as they become available,
     * add them to a list of locations and when the list reaches
     * the expected number of location updates, adds the list and the graph
     * to the database
     */

    private class GraphListener implements LocationListener{

        GraphListener(String provider){
            theLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location) {
            LocationService.theLocation = location;
            Log.d(TAG, "Got a fix" + location);
            locationList.add(theLocation);

            if (locationList.size() == preference.getLocationFreq()){
                Graph graph = new Graph();

                int day , month, year;

                Calendar calendar = Calendar.getInstance();

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                graph.setGraphDate(day, month, year);
                graph.setLocations(locationList);
                graphDAO.addGraph(graph);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d(TAG, "Got a new provider" + s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d(TAG, "Provider " + s + "enabled");
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d(TAG, "Provider " + s + "disEnabled");
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new GraphListener(LocationManager.GPS_PROVIDER),
            new GraphListener(LocationManager.NETWORK_PROVIDER)
    };
}
