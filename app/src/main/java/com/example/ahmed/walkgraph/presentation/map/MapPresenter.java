package com.example.ahmed.walkgraph.presentation.map;

import com.example.ahmed.walkgraph.data.model.Graph;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar
 *
 * MVP View Contract.
 */

public interface MapPresenter {
    /**
     * Gets the recent day's graph.
     * @return most recent graph.
     */
    Graph getRecentGraph();

    /**
     * Test graph object when there's no graph in the Database.
     * @return dummy graph.
     */
    Graph testGraph();
}
