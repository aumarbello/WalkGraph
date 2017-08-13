package com.example.ahmed.walkgraph.presentation.map;

import android.location.Location;

import com.example.ahmed.walkgraph.data.model.Graph;

/**
 * Created by ahmed on 8/9/17.
 */

public interface MapPresenter {
    void loadCurrentLocation();
    void savedLocation(Location location);
    Graph getRecentGraph();
    void writeGraph();
}
