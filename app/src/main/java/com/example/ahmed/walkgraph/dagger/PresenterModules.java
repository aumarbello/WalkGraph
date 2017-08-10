package com.example.ahmed.walkgraph.dagger;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.example.ahmed.walkgraph.presentation.list.GraphListImpl;
import com.example.ahmed.walkgraph.presentation.list.GraphPresenterImpl;
import com.example.ahmed.walkgraph.presentation.map.MapFragmentImpl;
import com.example.ahmed.walkgraph.presentation.map.MapPresenterImpl;
import com.example.ahmed.walkgraph.presentation.settings.SettingsFragmentImpl;
import com.example.ahmed.walkgraph.presentation.settings.SettingsPresenterImpl;


import com.example.ahmed.walkgraph.presentation.splash.SplashFragmentImpl;
import com.example.ahmed.walkgraph.presentation.splash.SplashPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ahmed on 8/9/17.
 */
@Module
public class PresenterModules {
    @Provides
    @Singleton
    public SplashPresenterImpl providesSplashPresenter(SplashFragmentImpl splashFragment){
        return new SplashPresenterImpl(splashFragment);
    }

    @Provides
    @Singleton
    public MapPresenterImpl providesMapPresenter(MapFragmentImpl mapFragment){
        return new MapPresenterImpl();
    }

    @Provides
    @Singleton
    public GraphPresenterImpl providesGraphPresenter(GraphListImpl graphList, GraphDAO graphDAO){
        return new GraphPresenterImpl();
    }

    @Provides
    @Singleton
    public SettingsPresenterImpl providesSettingsPresenter(Preferences preferences, SettingsFragmentImpl fragment){
        return new SettingsPresenterImpl();
    }
}
