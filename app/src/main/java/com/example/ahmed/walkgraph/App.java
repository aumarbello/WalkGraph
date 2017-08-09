package com.example.ahmed.walkgraph;

import android.app.Application;

import com.example.ahmed.walkgraph.dagger.AppComponent;
import com.example.ahmed.walkgraph.dagger.ApplicationModules;
import com.example.ahmed.walkgraph.dagger.DaggerAppComponent;

/**
 * Created by ahmed on 8/9/17.
 */

public class App extends Application {
    private AppComponent component;

    public AppComponent getComponent(){
        return component;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        component = init(this);
    }

    protected AppComponent init(App app){
        return DaggerAppComponent.builder()
                .applicationModules(new ApplicationModules(app))
                .build();
    }
}
