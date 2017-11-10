package com.example.ahmed.walkgraph.dagger;

import com.example.ahmed.walkgraph.notifications.LocationService;
import com.example.ahmed.walkgraph.notifications.TimeService;
import com.example.ahmed.walkgraph.presentation.ContainerActivity;
import com.example.ahmed.walkgraph.presentation.list.GraphListImpl;
import com.example.ahmed.walkgraph.presentation.map.MapFragmentImpl;
import com.example.ahmed.walkgraph.presentation.settings.SettingsFragment;
import com.example.ahmed.walkgraph.presentation.splash.SplashFragmentImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ahmed on 8/9/17.
 */
@Singleton
@Component(modules = {FragmentModules.class, ApplicationModules.class})
public interface AppComponent {
    void inject(SplashFragmentImpl fragment);
    void inject(MapFragmentImpl fragment);
    void inject(GraphListImpl fragment);
    void inject(SettingsFragment fragment);
    void inject(ContainerActivity activity);
    void inject(LocationService service);
    void inject(TimeService timeService);
}
