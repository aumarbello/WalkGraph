package com.example.ahmed.walkgraph.presentation.map;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.example.ahmed.walkgraph.notifications.LocationService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class MapFragmentImpl extends SupportMapFragment implements MapFragment {
    public interface MapCallBack{
        void returnToList();
    }
    @Inject
    MapPresenterImpl mapPresenter;
    @Inject
    Preferences preferences;

    private MapCallBack callBack;
    private GoogleMap googleMap;
    private static final String TAG = "MapFragment";
    private static final int permissionCode = 1;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        getMapAsync(theMap -> {
            googleMap = theMap;
            updateMapArea();
        });
        startService();
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.map_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.return_to_list){
            callBack.returnToList();
            return true;
        }
        return false;
    }

    @Override
    public void startService() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionCode);
        }else {
            getActivity().startService(new Intent(getActivity(),
                    LocationService.class));
        }
    }

    @Override
    public void updateMapArea() {
        Graph graph = mapPresenter.getRecentGraph();
        if (googleMap == null){{
            Log.d(TAG, "Google map is null");
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
        drawGraph(graph);
    }

    @Override
    public void drawGraph(Graph graph) {
        PolylineOptions graphOptions = new PolylineOptions();
        List<Location> locationList = graph.getLocations();

        for (Location location: locationList){
            graphOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        Polyline graphPolygon = googleMap.addPolyline(graphOptions);
        graphPolygon.setColor(Color.BLACK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantedResults){
        switch (requestCode){
            case permissionCode:
                if (grantedResults.length > 0 && grantedResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    getActivity().startService(new Intent(getActivity(),
                            LocationService.class));
                }
                else
                    Log.d(TAG, "Access Denied");
        }
    }
}
