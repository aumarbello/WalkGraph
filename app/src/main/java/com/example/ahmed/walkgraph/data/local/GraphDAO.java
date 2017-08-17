package com.example.ahmed.walkgraph.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.List;

import static com.example.ahmed.walkgraph.utils.AppConstants.*;



/**
 * Created by ahmed on 8/10/17.
 */

public class GraphDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private static GraphDAO dao;

    private GraphDAO(Context context){
        helper = new DatabaseHelper(context);
    }

    public static GraphDAO getDao(Context context){
        if (dao == null){
            dao = new GraphDAO(context);
        }
        dao.open();
        return dao;
    }

    public void open(){
        database = helper.getWritableDatabase();
    }

    public void closeDb(){
        database.close();
    }

    public void addGraph(Graph graph){
        String date = graph.getGraphDate();

        for (Location location: graph.getLocations()){
            database.insert(locationTable, null, getLocationValues(location,
                    date));
        }
    }

    public Graph getGraph(int day, int month, int year){
        Graph graph = new Graph();
        graph.setGraphDate(day, month, year);
        List<Location> locations = getLocationsFromDate(graph.getGraphDate());
        if (locations.isEmpty()){
            return null;
        }
        graph.setLocations(locations);
        return graph;
    }

    private List<Location> getLocationsFromDate(String date){
        List<Location> locationList = new ArrayList<>();

        LocationWrapper locationWrapper = queryLocationTableForLocations
                (location_date + " == ?", new String[]{date});

        try {
            locationWrapper.moveToFirst();
            while (!locationWrapper.isAfterLast()){
                locationList.add(locationWrapper.getLocation());
                locationWrapper.moveToNext();
            }
        }finally {
            locationWrapper.close();
        }

        return locationList;
    }

    public List<Graph> getAllGraphs(){
        List<Graph> graphList = new ArrayList<>();
        LocationWrapper allGraphs = queryLocationTableForDates(null, null);

        int day, month, year;

        try {
            allGraphs.moveToFirst();
            while (!allGraphs.isAfterLast()){
                Graph graph = new Graph();
                String[] date = allGraphs.LocationDate().split("/");

                day = Integer.valueOf(date[0]);
                month = Integer.valueOf(date[1]);
                year = Integer.valueOf(date[2]);

                graph.setGraphDate(day, month, year);
                graphList.add(graph);
                allGraphs.moveToNext();
            }
        }finally {
            allGraphs.close();
        }

        for (Graph graph : graphList) {
            graph.setLocations(getLocationsFromDate(graph.getGraphDate()));
        }
        return graphList;
    }

    private LocationWrapper queryLocationTableForDates(String whereClause, String[] args){
        return new LocationWrapper(database.query(
                locationTable,
                new String[]{location_date},
                whereClause,
                args,
                null,
                null,
                null
        ));
    }

    private LocationWrapper queryLocationTableForLocations(String whereClause, String[] args){
        return new LocationWrapper(database.query(
                locationTable,
                new String[]{latitude, longitude},
                whereClause,
                args,
                null,
                null,
                null
        ));
    }

    private ContentValues getLocationValues(Location location, String date){
        ContentValues values = new ContentValues();
        values.put(location_date, date);
        values.put(latitude, location.getLatitude());
        values.put(longitude, location.getLongitude());
        return values;
    }
}
