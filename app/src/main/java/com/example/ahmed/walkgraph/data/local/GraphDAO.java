package com.example.ahmed.walkgraph.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.ahmed.walkgraph.utils.AppConstants.*;



/**
 * Created by ahmed on 8/10/17.
 */

public class GraphDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private static GraphDAO dao;
    private static final String TAG = "GraphDao";

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
        long date = graph.getGraphDate().getTime();
        String dateString = String.valueOf(date);
        ContentValues dateValue = getGraphDate(dateString);

        //add date first
        database.insert(graphTable, null, dateValue);

        //get all locations and corresponding content value for each location
        List<Location> locations = graph.getLocations();
        for (Location location: locations){
            database.insert(locationTable, null, getLocationValues(location, dateString));
        }
    }

    public Graph getGraph(long date){
        //retrieve graph date
        GraphWrapper wrapper = queryGraphTable(graphDate + " == ?",
                new String[]{String.valueOf(date)});
        Log.d("GraphDao", "wrapper size" + wrapper.getCount());
        Graph graph = wrapper.getGraph();

        //retrieve locations
        List<Location> locationList = new ArrayList<>();
        LocationWrapper locationWrapper = queryLocationTable(foreignKey + " == ?",
                new String[]{String.valueOf(date)});
        if (wrapper.getCount() == 0){
            Log.d(TAG, "Empty cursor returned for graph check whereClause");
            return null;
        }

        if (locationWrapper.getCount() == 0){
            Log.d(TAG, "Empty cursor returned for locations check whereClause");
            return null;
        }
        try {
            locationWrapper.moveToFirst();
            while (!locationWrapper.isAfterLast()){
                locationList.add(locationWrapper.getLocation());
                locationWrapper.moveToNext();
            }
        }finally {
            locationWrapper.close();
            wrapper.close();
        }

        //set graph locations
        graph.setLocations(locationList);
        return graph;
    }

    private Graph getGraphFromDate(long date){
        //current graph
        Graph graph = new Graph();

        //retrieve locations
        List<Location> locationList = new ArrayList<>();
        LocationWrapper locationWrapper = queryLocationTable(foreignKey + " == ?",
                new String[]{"" + date});
        try {
            locationWrapper.moveToFirst();
            while (!locationWrapper.isAfterLast()){
                locationList.add(locationWrapper.getLocation());
                locationWrapper.moveToNext();
            }
        }finally {
            locationWrapper.close();
        }

        //set graph locations
        graph.setLocations(locationList);
        return graph;
    }

    public List<Graph> getAllGraphs(){
        List<Graph> graphDates = new ArrayList<>();
        List<Graph> graphList = new ArrayList<>();
        GraphWrapper graphWrapper = queryGraphTable(null, null);

        //retrieve all dates
        try {
            graphWrapper.moveToFirst();
            while (!graphWrapper.isAfterLast()){
                graphDates.add(graphWrapper.getGraph());
                graphWrapper.moveToNext();
            }
        }finally {
            graphWrapper.close();
        }

        for (Graph graph : graphDates) {
            graphList.add(getGraphFromDate(graph.getGraphDate().getTime()));
        }
        return graphList;
    }

    private LocationWrapper queryLocationTable(String whereClause, String[] args){
        return new LocationWrapper(database.query(
                locationTable,
                new String[]{foreignKey},
                whereClause,
                args,
                null,
                null,
                null
        ));
    }

    private GraphWrapper queryGraphTable(String whereClause, String[] args){
        return new GraphWrapper(database.query(
                graphTable,
                null,
                whereClause,
                args,
                null,
                null,
                null
        ));
    }

    private ContentValues getLocationValues(Location location, String date){
        ContentValues values = new ContentValues();
        values.put(foreignKey, date);
        values.put(latitude, location.getLatitude());
        values.put(longitude, location.getLongitude());
        return values;
    }

    private ContentValues getGraphDate(String date){
        ContentValues values = new ContentValues();
        values.put(graphDate, date);
        return values;
    }
}
