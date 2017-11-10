package com.example.ahmed.walkgraph.presentation.list;

import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.List;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar
 *
 * MVP View Contract.
 */

public interface GraphList {
    /**
     * Sets the recyclerView with a list graphs.
     * @param allGraphs list of all graphs from database.
     */
    void setGraphList(List<Graph> allGraphs);

    /**
     * Updates the recyclerView
     */
    void update();

    /**
     * Sets up the bottom navigation bar
     */
    void setUpBottomNav();
}
