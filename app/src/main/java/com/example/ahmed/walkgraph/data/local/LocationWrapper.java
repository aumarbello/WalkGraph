package com.example.ahmed.walkgraph.data.local;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;

import com.example.ahmed.walkgraph.utils.AppConstants;

/**
 * Created by ahmed on 8/10/17.
 */

public class LocationWrapper extends CursorWrapper {
    public LocationWrapper(Cursor cursor) {
        super(cursor);
    }

    public Location getLocation(){
        double longitude = getDouble(getColumnIndex(AppConstants.longitude));
        double latitude = getDouble(getColumnIndex(AppConstants.latitude));
        Location location = new Location(AppConstants.locationProvider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
