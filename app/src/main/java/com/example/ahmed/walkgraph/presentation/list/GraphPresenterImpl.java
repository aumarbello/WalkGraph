package com.example.ahmed.walkgraph.presentation.list;

import android.util.Log;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 8/9/17.
 */

public class GraphPresenterImpl implements GraphPresenter {
    private GraphListImpl graphList;
    private GraphDAO graphDAO;
    private List<Graph> graphs;

    public GraphPresenterImpl(GraphListImpl graphList, GraphDAO graphDAO){
        this.graphList = graphList;
        this.graphDAO = graphDAO;
        graphs = new ArrayList<>();
    }

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
    // TODO: 8/10/17 perform call to db in different thread, consider using observable
}
