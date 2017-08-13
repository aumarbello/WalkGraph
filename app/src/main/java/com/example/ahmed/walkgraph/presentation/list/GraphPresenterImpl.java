package com.example.ahmed.walkgraph.presentation.list;

import com.example.ahmed.walkgraph.data.local.GraphDAO;

/**
 * Created by ahmed on 8/9/17.
 */

public class GraphPresenterImpl implements GraphPresenter {
    private GraphListImpl graphList;
    private GraphDAO graphDAO;

    public GraphPresenterImpl(GraphListImpl graphList, GraphDAO graphDAO){
        this.graphList = graphList;
        this.graphDAO = graphDAO;
    }

    @Override
    public void getGraphList() {
        graphList.setGraphList(graphDAO.getAllGraphs());
        graphList.update();
    }
    // TODO: 8/10/17 perform call to db in different thread, consider using observable
}
