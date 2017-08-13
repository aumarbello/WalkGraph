package com.example.ahmed.walkgraph.presentation;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.presentation.list.GraphListImpl;
import com.example.ahmed.walkgraph.presentation.map.MapFragmentImpl;
import com.example.ahmed.walkgraph.presentation.settings.SettingsFragmentImpl;
import com.example.ahmed.walkgraph.presentation.splash.SplashFragmentImpl;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class ContainerActivity extends AppCompatActivity implements
        //splash fragment
        SplashFragmentImpl.SplashCallback,
        //settings fragment
        SettingsFragmentImpl.SettingsCallBack,
        //map fragment
        MapFragmentImpl.MapCallBack,
        //graph fragment
        GraphListImpl.GraphListCallBack{

    @Inject
    SplashFragmentImpl splashFragment;

    @Inject
    MapFragmentImpl mapFragment;

    @Inject
    SettingsFragmentImpl settingsFragment;

    @Inject
    GraphListImpl graphList;

    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App)getApplication()).getComponent().inject(this);

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragment_container, splashFragment)
                .commit();
    }

    //splash fragment callback
    @Override
    public void changeToMap() {
        manager.beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }

    @Override
    public void onBackPressed(){

    }

    // settings fragment callback
    @Override
    public void switchToList() {
       manager.beginTransaction()
               .replace(R.id.fragment_container, graphList)
               .commit();
    }

    @Override
    public void switchToMap() {
        manager.beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }
}
