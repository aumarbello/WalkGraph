package com.example.ahmed.walkgraph.presentation.map;

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

public class MapFragmentImpl extends Fragment implements MapFragment {
    public interface MapCallBack{

    }

    private MapCallBack callBack;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_map, parent, false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof MapCallBack){
            callBack = (MapCallBack) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement MapCallBack");
    }
}
