package com.example.ahmed.walkgraph.dagger;

import com.example.ahmed.walkgraph.data.local.GraphDAO;
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.example.ahmed.walkgraph.presentation.list.GraphListImpl;
import com.example.ahmed.walkgraph.presentation.list.GraphPresenterImpl;
import com.example.ahmed.walkgraph.presentation.map.MapPresenterImpl;
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
class PresenterModules {
    @Provides
    @Singleton
    SplashPresenterImpl providesSplashPresenter(SplashFragmentImpl splashFragment){
        return new SplashPresenterImpl(splashFragment);
    }

    @Provides
    @Singleton
    MapPresenterImpl providesMapPresenter(GraphDAO graphDAO){
        return new MapPresenterImpl(graphDAO);
    }

    @Provides
    @Singleton
    GraphPresenterImpl providesGraphPresenter(GraphListImpl graphList, GraphDAO graphDAO){
        return new GraphPresenterImpl(graphList, graphDAO);
    }

    @Provides
    @Singleton

    SettingsPresenterImpl providesSettingsPresenter(Preferences preferences){
        return new SettingsPresenterImpl(preferences);
    }
}
