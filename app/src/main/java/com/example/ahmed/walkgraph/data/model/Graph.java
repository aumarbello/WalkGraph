package com.example.ahmed.walkgraph.data.model;

import android.location.Location;
import java.util.List;

/**
 * Created by ahmed on 8/9/17.
 */

public class Graph {
    private String graphDate;
    private List<Location> locations;

    public String  getGraphDate() {
        return graphDate;
    }

    public void setGraphDate(int day, int month, int year) {
        this.graphDate = day + "/" + month + "/" + year;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
