package com.example.ahmed.walkgraph.presentation.map;

import android.location.Location;
import android.util.Log;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.utils.AppConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ahmed on 8/9/17.
 */

public class MapPresenterImpl implements MapPresenter {
    private GraphDAO graphDAO;
    private MapFragmentImpl mapFragment;
    private List<Location> locations;
    private static final String TAG = "Map Presenter";

    public MapPresenterImpl(GraphDAO graphDAO, MapFragmentImpl mapFragment){
        this.graphDAO = graphDAO;
        this.mapFragment = mapFragment;
        locations = new ArrayList<>();
    }

    //trigger loading new location
    @Override
    public void loadCurrentLocation() {
        mapFragment.loadCurrentLocation();
    }

    //add location received to list of locations
    @Override
    public void savedLocation(Location location) {
        if (locations.size() == 5){
            locations.add(location);
            Log.d(TAG, "Location received" + locations.size() + "Saving to db");
            writeGraph();
        }else{
            locations.add(location);
            Log.d(TAG, "Location received" + locations.size() + location);
        }

        //before adding check to see if .size == numOfUpdates
        //if true add and call writeGraph
        //else just add
    }

    //to be called by mapFragment
    @Override
    public Graph getRecentGraph() {
        //return stub graph to allow code to compile when there's no graph in db
        Graph dbGraph = graphDAO.getGraph(new Date().getTime());
        List<Graph> graphList = graphDAO.getAllGraphs();
        if (dbGraph == null){
            Log.d(TAG, "Error loading from db");
            dbGraph = testGraph();
            Log.d(TAG, "Graph loading from db " + dbGraph.getLocations().size()
                    + " all graphs " + graphList.size());
        }
        return dbGraph;
    }

    @Override
    public void writeGraph() {
        Graph graph = new Graph();
        graph.setGraphDate(new Date());
        graph.setLocations(locations);

        graphDAO.addGraph(graph);
        //create new graph, set to today's date and set list of location
    }
    // TODO: 8/10/17 perform call to db in different thread, consider using observable
    // TODO: 8/12/17 get graph of previous date and pass to view

    @Override
    public Graph testGraph() {
        Graph graph = new Graph();

        List<Location> locationList = new ArrayList<>();
        Location location = new Location(AppConstants.locationProvider);

        location.setLatitude(6.5229296);
        location.setLongitude(3.3792057);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5480747);
        location.setLongitude(3.3975005);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5243793);
        location.setLongitude(3.3975005);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5229296);
        location.setLongitude(3.3792057);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5229296);
        location.setLongitude(3.3792057);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5229296);
        location.setLongitude(3.3792057);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5229296);
        location.setLongitude(3.3792057);
        locationList.add(location);

        location = new Location(AppConstants.locationProvider);
        location.setLatitude(6.5020794);
        location.setLongitude(3.3737125);
        locationList.add(location);

        graph.setGraphDate(new Date());
        graph.setLocations(locationList);
        return graph;
    }
}
