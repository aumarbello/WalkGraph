package com.example.ahmed.walkgraph.presentation.map;

import com.example.ahmed.walkgraph.data.model.Graph;

/**
 * Created by ahmed on 8/9/17.
 */

public interface MapFragment {
    void startService();
    void updateMapArea();
    void drawGraph(Graph graph);
}
