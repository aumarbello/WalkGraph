package com.example.ahmed.walkgraph.presentation.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.example.ahmed.walkgraph.notifications.TimeService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

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
    public static final int permissionCode = 1000;
    private int gpsStatus;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        getMapAsync(theMap -> {
            googleMap = theMap;
            updateMapArea();
        });
        checkPerm();
        setHasOptionsMenu(true);
        try{
            gpsStatus = Settings.Secure.getInt(getActivity().getContentResolver(),
                    Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Settings not found", e);
        }
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
    public void checkPerm() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionCode);
        }else {
            startLocationService();
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
                    startLocationService();
                }
                else
                    Log.d(TAG, "Access Denied");
        }
    }

    @Override
    public void startLocationService() {
        LocationRequest request = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder settingsRequest = new LocationSettingsRequest.Builder();
        settingsRequest.addLocationRequest(request);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> responseTask = client.checkLocationSettings
                (settingsRequest.build());

        responseTask.addOnSuccessListener(getActivity(), locationSettingsResponse ->
                locationScheduler());

        responseTask.addOnFailureListener(getActivity(), e -> {
            int statusCode = ((ApiException) e).getStatusCode();

            switch (statusCode){
                case CommonStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        ResolvableApiException apiException = ((ResolvableApiException)e);
                        apiException.startResolutionForResult(getActivity(), permissionCode);
                        Log.d(TAG, "Dialog displayed");
                    }catch (IntentSender.SendIntentException sendIntentException){
                        Log.d(TAG, "Error displaying dialogBox", sendIntentException);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.d(TAG, "Unable to turn on location service", e);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == permissionCode){
            Log.d(TAG, "Result received in fragment");
            locationScheduler();
        }
    }


    public void locationScheduler() {
        if (gpsStatus == 0){
            onGPS();
            //todo return
        }

        //todo start time service instead
        TimeService.setTimeInterval(getActivity());
    }

    @Override
    public void onGPS() {
        Intent gpsIntent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
        );
        //todo start activity for result and check for corresponding
        // todo code in onActivityResult
        startActivity(gpsIntent);
    }
}
