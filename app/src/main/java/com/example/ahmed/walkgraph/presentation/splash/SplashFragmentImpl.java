package com.example.ahmed.walkgraph.presentation.splash;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class SplashFragmentImpl extends Fragment implements SplashFragment{
    public interface SplashCallback {
        void changeToMap();
    }

    @Inject
    SplashPresenterImpl splashPresenter;
    private SplashCallback callback;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ((App)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        splashPresenter.openMapFragment();
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof SplashCallback){
            callback = (SplashCallback) context;
        }else
            throw new RuntimeException(context.toString() + "Must implement SplashCallback");
    }

    @Override
    public void openMapFragment() {
        callback.changeToMap();
    }

}
