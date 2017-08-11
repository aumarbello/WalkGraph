package com.example.ahmed.walkgraph.presentation.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;

/**
 * Created by ahmed on 8/9/17.
 */

public class GraphListImpl extends Fragment implements GraphList {
    public interface GraphListCallBack{

    }

    private GraphListCallBack callBack;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.graph_list, parent, false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof GraphListCallBack){
            callBack = (GraphListCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement GraphListCall");
    }
}
