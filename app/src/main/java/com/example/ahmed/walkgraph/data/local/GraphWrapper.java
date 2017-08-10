package com.example.ahmed.walkgraph.data.local;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.utils.AppConstants;

import java.util.Date;

/**
 * Created by ahmed on 8/10/17.
 */

public class GraphWrapper extends CursorWrapper {
    public GraphWrapper(Cursor cursor) {
        super(cursor);
    }

    public Graph getGraph(){
        long date = getLong(getColumnIndex(AppConstants.graphDate));
        Date graphDate = new Date(date);
        Graph graph = new Graph();
        graph.setGraphDate(graphDate);
        return graph;
    }
}
