package com.example.ahmed.walkgraph.data.model;

import android.location.Location;

import java.util.Date;
import java.util.List;

/**
 * Created by ahmed on 8/9/17.
 */

public class Graph {
    private Date graphDate;
    private List<Location> locations;

    public Date getGraphDate() {
        return graphDate;
    }

    public void setGraphDate(Date graphDate) {
        this.graphDate = graphDate;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
