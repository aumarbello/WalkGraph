package com.example.ahmed.walkgraph.data.local;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.ahmed.walkgraph.data.model.Graph;
import com.example.ahmed.walkgraph.utils.AppConstants;

import java.util.Date;

/**
 * Created by ahmed on 8/10/17.
 */
class GraphWrapper extends CursorWrapper {
    GraphWrapper(Cursor cursor) {
        super(cursor);
    }

    Graph getGraph(){
//        String dateString = getString(getColumnIndex(AppConstants.graphDate));
//        long dateLong = Long.parseLong(dateString);
        Date graphDate = new Date();
        Graph graph = new Graph();
        graph.setGraphDate(graphDate);
        return graph;
    }
}
