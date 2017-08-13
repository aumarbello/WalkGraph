package com.example.ahmed.walkgraph.presentation.map;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.model.Graph;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ahmed on 8/12/17.
 */

public class LocationScheduler extends JobService {
//    private
    //private field of graph, set no new date and instantiate new list of locations
    //to be updated in onStartJob()
    private static final String TAG = "LocationScheduler";
    private MapPresenterImpl mapPresenter = new MapPresenterImpl(GraphDAO.getDao(this), new MapFragmentImpl());
//    private Graph graph = new Graph();
//    private List<Location> locationList = new ArrayList<>();
//    private GoogleApiClient client = new GoogleApiClient.Builder(this)
//            .addApi(LocationServices.API)
//            .build();
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "OnStartJob called");
//        mapPresenter.loadCurrentLocation();
        //call presenter to call fragment's loadCurrentLocation
        //loadCurrentLocationShould call another presenter method
        //when it gets the location and add it to a list of locations
//        graph.setGraphDate(new Date());
//        client.connect();
//        LocationRequest request = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(0)
//                .setNumUpdates(1);
//        if (client.isConnected()){
//            try {
//                LocationServices.FusedLocationApi
//                        .requestLocationUpdates(client, request,
//                                //read up on method reference
//                                location -> locationList.add(location));
//                //call presenter method with location parameter
//                Log.d("TAG", "got a hit" + locationList.get(locationList.size() - 1));
//            }catch (SecurityException sec){
//                Log.e(TAG, "Location Error", sec);
//            }
//        }
//        //getting location every x minutes
//        //use constants returned from schedule to log if successful or not in scheduling job
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
//        Log.d(TAG, "Job stopped");
//        client.disconnect();
        Log.d(TAG, "OnStopJob called");
        return false;
    }

    public void setUp(){

    }
}
