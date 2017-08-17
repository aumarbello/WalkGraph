package com.example.ahmed.walkgraph.data.local;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;

import com.example.ahmed.walkgraph.utils.AppConstants;

/**
 * Created by ahmed on 8/10/17.
 */

class LocationWrapper extends CursorWrapper {
    LocationWrapper(Cursor cursor) {
        super(cursor);
    }

    Location getLocation(){
        double latitude = getDouble(getColumnIndex(AppConstants.latitude));
        double longitude = getDouble(getColumnIndex(AppConstants.longitude));
        Location location = new Location(AppConstants.locationProvider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    String LocationDate(){
        return getString(getColumnIndex(AppConstants.location_date));
    }
}
