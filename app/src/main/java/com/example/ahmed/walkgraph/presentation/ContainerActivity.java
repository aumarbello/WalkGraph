package com.example.ahmed.walkgraph.presentation;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.presentation.list.GraphListImpl;
import com.example.ahmed.walkgraph.presentation.map.MapFragmentImpl;
import com.example.ahmed.walkgraph.presentation.settings.SettingsFragment;
import com.example.ahmed.walkgraph.presentation.splash.SplashFragmentImpl;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 *
 * Containing activity hosting all the fragments of the application
 *
 * implements CallBacks from Splash, Map, and GraphList
 */

public class ContainerActivity extends AppCompatActivity implements
        //splash fragment
        SplashFragmentImpl.SplashCallback,
        //map fragment
        MapFragmentImpl.MapCallBack,
        //graph fragment
        GraphListImpl.GraphListCallBack{

    /**
     * Inject Fields - Fragments
     */

    @Inject
    SplashFragmentImpl splashFragment;

    @Inject
    MapFragmentImpl mapFragment;

    @Inject
    SettingsFragment settingsFragment;

    @Inject
    GraphListImpl graphList;

    private FragmentManager manager;

    /**
     * setContentView.
     *
     * initializes injected fields to prevent NPE.
     *
     * initializes FragmentManager.
     *
     * adds splashFragment as the first fragment.
     *
     * @param savedInstanceState of the activity.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App)getApplication()).getComponent().inject(this);

        manager = getFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragment_container, splashFragment)
                .commit();
    }

    /**
     * splash fragment callback.
     */
    @Override
    public void changeToMap() {
        manager.beginTransaction()
                .replace(R.id.fragment_container, graphList)
                .commit();
    }

    /**
     * Switch to list fragment.
     */
    @Override
    public void onBackPressed(){
        if (mapFragment.isAdded() || settingsFragment.isAdded()){
            switchToList();
        }else
            super.onBackPressed();
    }

    /**
     * Method to switch back to fragment list.
     */
    private void switchToList() {
       manager.beginTransaction()
               .replace(R.id.fragment_container, graphList)
               .commit();
    }

    /**
     * GraphList fragment's specific callback, to switch to map.
     */

    @Override
    public void switchToMap() {
        manager.beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }

    /**
     * GraphList fragment's specific callback, to switch to settings.
     */
    @Override
    public void switchToSettings() {
        manager.beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .commit();
    }

    /**
     * Map fragment's callback.
     */

    @Override
    public void returnToList() {
        switchToList();
    }

    /**
     * Passing activity result to fragment, is cases where its not called.
     * @param requestCode of the request.
     * @param resultCode of the request's result- OK or NOT.
     * @param data of the request.
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == MapFragmentImpl.permissionCode){
            mapFragment.onActivityResult(requestCode, resultCode, data);
            Log.d("Activity Container", "Passing result to fragment");
        }
    }
}
