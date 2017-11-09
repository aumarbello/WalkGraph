package com.example.ahmed.walkgraph.presentation.map;

import com.example.ahmed.walkgraph.data.model.Graph;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar MVP Presenter Contract.
 */

public interface MapFragment {
    /**
     * Checks location settings and attempts to change it.
     */
    void startLocationService();

    /**
     * Checks if the user has granted necessary permissions.
     */
    void checkPerm();

    /**
     * Zooms in on Map to show relevant map area.
     */
    void updateMapArea();

    /**
     * Draws graph on map.
     * @param graph to draw on the map.
     */
    void drawGraph(Graph graph);

    /**
     * Starts The Location Service.
     */
    void locationScheduler();

    /**
     * Starts activity to enable user to turn GPS on.
     */
    void onGPS();
}
