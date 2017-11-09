package com.example.ahmed.walkgraph.presentation.list;

import android.util.Log;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar.
 *
 * Class that implements the GraphPresenter contract.
 */

public class GraphPresenterImpl implements GraphPresenter {

    /**
     * Fields.
     */
    private GraphListImpl graphList;
    private GraphDAO graphDAO;
    private List<Graph> graphs;

    /**
     * Constructor.
     * @param graphList fragment to interact with.
     * @param graphDAO to retrieve graphs from.
     */
    @Inject
    GraphPresenterImpl(GraphListImpl graphList, GraphDAO graphDAO){
        this.graphList = graphList;
        this.graphDAO = graphDAO;
        graphs = new ArrayList<>();
    }

    /**
     * Calls database to get all graphs and updates the recyclerView.
     */
    @Override
    public void getGraphList() {
        graphs = graphDAO.getAllGraphs();
        if (graphs != null){
            Log.d("Graph Presenter", "Graphs not null " + graphs.size());
        }else
            Log.d("Graph Presenter", "Graphs is null");
        graphs = new ArrayList<>();
        graphList.setGraphList(graphs);
        graphList.update();
    }
}
