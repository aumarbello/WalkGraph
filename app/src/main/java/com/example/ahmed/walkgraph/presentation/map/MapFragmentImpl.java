package com.example.ahmed.walkgraph.presentation.map;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class MapFragmentImpl extends SupportMapFragment implements MapFragment {
    public interface MapCallBack{

    }
    @Inject
    MapPresenterImpl mapPresenter;
    @Inject
    Preferences preferences;

    private MapCallBack callBack;
    private GoogleApiClient client;
    private GoogleMap googleMap;
    private static final String TAG = "MapFragment";
    private static final int permissionCode = 1;
    private static final int locationJobInfoId = 100;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        client = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //call presenter method to telling it has connected and can call loadCurrentLocation()
                        loadCurrentLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        getMapAsync(theMap -> googleMap = theMap);
        JobScheduler scheduler = (JobScheduler) getActivity().
                getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo info = new JobInfo.Builder(locationJobInfoId,
                new ComponentName(getActivity().getPackageName(),
                        LocationScheduler.class.getName()))
                .setPeriodic(preferences.getNotificationTime())
//                .setOverrideDeadline(4000)
                //use for testing
                .build();
        int status = scheduler.schedule(info);
        if (status == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "Job scheduled successfully " + status);
        }else
            Log.d(TAG, "Job not scheduled" + status);
        updateMapArea();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof MapCallBack){
            callBack = (MapCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement MapCallBack");
    }

    @Override
    public void onStart(){
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop(){
        super.onStop();
        client.disconnect();
    }

    @Override
    public void loadCurrentLocation() {
        // TODO: 8/11/17 presenter calls this methods at intervals to get location
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                //calculate interval using start and stop
//                preferences.getLocationFreq()
                .setInterval(3000)
                .setNumUpdates(5);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionCode);
        }else {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(client, request,
                            //read up on method reference
                            mapPresenter::savedLocation);
            //call presenter method with location parameter
            Log.d(TAG, "Passed location to updateMapArea");
        }
    }

    @Override
    public void updateMapArea() {
        Graph graph = mapPresenter.getRecentGraph();
        // TODO: 8/12/17 updateMapShould receive a graph as a parameter
        // todo and zoom map according to the first and last locations
        if (googleMap == null){{
            return;
        }}
        Location first = graph.getLocations().get(0);
        Location last = graph.getLocations().get(graph.getLocations().size() - 1);
        LatLng firstPoint = new LatLng(first.getLatitude(), first.getLongitude());
        LatLng lastPoint = new LatLng(last.getLatitude(), last.getLongitude());


        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(firstPoint)
                .include(lastPoint)
                .build();
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        googleMap.animateCamera(update);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantedResults){
        switch (requestCode){
            case permissionCode:
                if (grantedResults.length > 0 && grantedResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    loadCurrentLocation();
                }
                else
                    Log.d(TAG, "Access Denied");
        }
    }
}
