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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar.
 * Fragment to show a select or initial Dummy 'walk' on the map.
 *
 * Also requests for location permission from the user and prompts the user
 * to turn GPS on if initially off.
 */

public class MapFragmentImpl extends MapFragment
        implements MapFragmentContract {

    /**
     * Callback interface to be implemented by containing activity.
     */
    public interface MapCallBack{
        /**
         * Switches the fragment back to the list of graphs.
         */
        void returnToList();
    }

    /**
     * Injected Field - MapPresenter.
     */
    @Inject
    MapPresenterImpl mapPresenter;

    /**
     * Class Fields.
     *
     * MapCallback - to assess callback interface's method when appropriate.
     * GoogleMap - instance of googleMap return from getAsynMap
     * and used to manipulate map on screen.
     * TAG - Class's tag for used for logging.
     * permissionCode - requestCode for enable location.
     * gpsStatus - current status of the device's gps.
     */
    private MapCallBack callBack;
    private GoogleMap googleMap;
    private static final String TAG = "MapFragmentContract";
    public static final int permissionCode = 1000;
    private static final int gpsReqCode = 1234;
    private int gpsStatus;

    /**
     * @param savedInstance Bundle saved with fragment's state.
     *
     * Call inject method to initialized injected fields before use - to avoid NPE.
     * get instance of google map in background and zoom in on the map.
     * calls checkPerm to initial permission checking/requesting.
     * gets current status of device's gps.
     */
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

    /**
     * @param context passed from containing activity.
     *
     * Instantiates MapCallBack field by casting context down.
     * and throwing an exception if the containing activity doesn't implements
     * the callback interface .
     */

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof MapCallBack){
            callBack = (MapCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement MapCallBack");
    }

    /**
     * @param menu to be passed along with menu resource.
     * @param inflater to be used to inflate menu resource.
     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.map_fragment, menu);
    }

    /**
     * @param item currently selected from the menu option.
     * @return true if selection was handled, return false otherwise.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.return_to_list){
            callBack.returnToList();
            return true;
        }
        return false;
    }

    /**
     * Checks for location permission, if not granted requests for it.
     * If granted starts the TimeService - which in turn starts location service.
     */

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

    /**
     * Zooms in on map based most recent or previous day's graph.
     *
     * Uses the first day's location as the first point and
     * the last location of graph as the last point for area to zoom
     * in on around the map.
     *
     * Calls drawGraph(graph) to show graph points.
     */
    @Override
    public void updateMapArea() {
        Graph graph = mapPresenter.getRecentGraph();
        if (googleMap == null){
            Log.d(TAG, "Google map is null");
            return;
        }

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

    /**
     * @param graph to use in drawing polyOptions on the graph.
     */

    @Override
    public void drawGraph(Graph graph) {
        PolylineOptions graphOptions = new PolylineOptions();
        List<Location> locationList = graph.getLocations();

        for (Location location: locationList){
            graphOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        Polyline graphPolygon = googleMap.addPolyline(graphOptions);

        graphPolygon.setGeodesic(true);
        graphPolygon.setColor(Color.BLACK);
    }

    /**
     *
     * @param requestCode associated with the originating request.
     * @param permissions asked of the user.
     * @param grantedResults of the permission asked.
     */

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

    /**
     * Method to check for the location settings of the device,
     * if it doesn't fit the current requirement of the app
     * open the settings for user to change them, if the settings are OK, starts the TimeService.
     */

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

    /**
     * Result of settings activity started.
     * @param requestCode added when starting activity.
     * @param resultCode result of the activity action - OK or NOT.
     * @param data associated with the result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case permissionCode:
                Log.d(TAG, "Result received in fragment from settings activity");
                if (resultCode == RESULT_OK)
                    locationScheduler();
                break;
            case gpsReqCode:
                Log.d(TAG, "Result received in from gps settings");
                if (resultCode == RESULT_OK)
                    locationScheduler();
                break;
        }
    }

    /**
     * Starts TimeService. But check firsts if the GPS is turned on,
     * if off attempts to turn it on.
     */
    public void locationScheduler() {
        if (gpsStatus == 0){
            onGPS();
            return;
        }
        TimeService.setTimeInterval(getActivity());
    }

    /**
     * Opens GPS settings to allow user turn GPS on.
     */

    @Override
    public void onGPS() {
        Intent gpsIntent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
        );
        startActivityForResult(gpsIntent, gpsReqCode);
    }
}
