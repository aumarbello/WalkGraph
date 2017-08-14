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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 8/14/17.
 */
public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private LocationManager manager;
    private static final int locationInterval = 1000;
    private static final int locationDistance = 0;
    private static Location theLocation;
    private static List<Location> locationList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Entered onCreate");
        initializeManager();

        try {
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    locationInterval, locationDistance,
                    locationListeners[1]
            );
        } catch (SecurityException sec) {
            Log.d(TAG, "Security exception while saving theLocation", sec);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "IllegalArgumentException" + ex.getMessage(), ex);
        }

        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, locationInterval, locationDistance,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());

        }
    }

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

    private void initializeManager(){
        Log.d(TAG, "Initializing manager");
        if (manager == null){
            manager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startIds){
        Log.d(TAG, "On start command started");
        super.onStartCommand(intent, flags, startIds);
        return START_STICKY;
    }

    private class GraphListener implements LocationListener{

        GraphListener(String provider){
            theLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location) {
            LocationService.theLocation = location;
            Log.d(TAG, "Got a fix" + location);
//            locationList.add(theLocation);

            //check if list size is equals to and write to db
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
