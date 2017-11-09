package com.example.ahmed.walkgraph.presentation.list;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.model.Graph;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ahmed on 8/9/17.
 *
 * @author Ahmed Umar
 * Class that Implements the MVP view contract.
 */

public class GraphListImpl extends Fragment implements GraphList {
    /**
     * Callback interface that must be implemented by the containing activity.
     */
    public interface GraphListCallBack{
        /**
         * Changes to mapFragment.
         */
        void switchToMap();

        /**
         * Changes to settingsFragment.
         */
        void switchToSettings();
    }

    /**
     * Fields
     */
    private GraphListCallBack callBack;
    private Unbinder unbinder;

    @BindView(R.id.graph_list)
    RecyclerView graphListView;

    @BindView(R.id.empty_list)
    RelativeLayout emptyList;

    @BindView(R.id.list_bottom_nav)
    BottomNavigationView navigationView;

    private List<Graph> graphList;
    private GraphAdapter adapter;

    @Inject
    GraphPresenterImpl graphPresenter;

    /**
     * initializes inject fields and List of graphs.
     * @param savedInstance of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
        graphList = new ArrayList<>();
    }

    /**
     * Inflates the view of this fragment, makes emptyView visible if necessary,
     * and binds view to butterKnife.
     * @param inflater to inflate layout resource with.
     * @param parent view's parent.
     * @param savedInstance of the fragment.
     * @return inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        View view = inflater.inflate(R.layout.graph_list, parent, false);
        unbinder = ButterKnife.bind(this, view);

        graphPresenter.getGraphList();

        if (graphList.isEmpty()){
            emptyList.setVisibility(View.VISIBLE);
        }
        graphListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        update();
        setUpBottomNav();
        return view;
    }

    /**
     * Initializes the CallBack field from the context passed by the containing activity.
     * @param context from the containing activity.
     * @throws RuntimeException when the containing activity
     * doesn't implement the callback interface.
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof GraphListCallBack){
            callBack = (GraphListCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement GraphListCall");
    }

    /**
     * Unbinds the view when the fragment is being destroyed.
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Adds graphs from database to list of graphs.
     * @param allGraphs list of all graphs from database.
     */
    @Override
    public void setGraphList(List<Graph> allGraphs) {
        graphList = allGraphs;
    }

    /**
     * Updates the recyclerView when graphs have been added.
     */
    @Override
    public void update() {
        if (adapter == null) {
            adapter = new GraphAdapter(graphList);
            adapter.setGraphList(graphList);
            graphListView.setAdapter(adapter);
        } else {
            adapter.setGraphList(graphList);
            graphListView.setAdapter(adapter);
        }

        if (!graphList.isEmpty()){
            emptyList.setVisibility(View.GONE);
        }
    }

    /**
     * Sets up view's bottom navigation bar.
     */
    @Override
    public void setUpBottomNav() {
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.map:
                    callBack.switchToMap();
                    break;
                case R.id.settings:
                    callBack.switchToSettings();
                    break;
            }
            return false;
        });
    }

    /**
     * GraphViewHolder for use with GraphAdapter.
     */
    class GraphHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.graph_date)
        TextView graphDate;

        @BindView(R.id.graph_locations)
        TextView graphLocations;
        GraphHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void BindGraph(Graph graph){
            String date = graph.getGraphDate();
            String locationCount = graph.getLocations().size() + " Locations";

            graphDate.setText(getString(R.string.graph_date, date));
            graphLocations.setText(getString(R.string.graph_locations, locationCount));
        }
    }

    /**
     * GraphAdapter for use with recyclerView.
     */
    private class GraphAdapter extends RecyclerView.Adapter<GraphHolder>{
        private List<Graph> graphList;
        GraphAdapter(List<Graph> graphList) {
            this.graphList = graphList;
        }

        @Override
        public GraphHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.graph_list_item, parent, false);
            return new GraphHolder(view);
        }

        @Override
        public void onBindViewHolder(GraphHolder holder, int position) {
            Graph graph = graphList.get(position);
            holder.BindGraph(graph);
        }

        @Override
        public int getItemCount() {
            return graphList.size();
        }

        void setGraphList(List<Graph> graphList) {
            this.graphList = graphList;
        }
    }
}
