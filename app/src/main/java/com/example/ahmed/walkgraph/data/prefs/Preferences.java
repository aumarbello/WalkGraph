package com.example.ahmed.walkgraph.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ahmed on 8/9/17.
 */
@Singleton
public class Preferences {
    private static SharedPreferences preferences;

    private static final String NOTIFICATION_TIME = "TimeOfNotification";
    private static final String LOCATION_FREQ = "FrequencyOfLocation";
    private static final String GRAPH_KEEP_TIME = "GraphKeepTime";
    private static final String ANIMATE_GRAPH = "AnimateGraph";
    private static final String START_POLLING = "StartPollingLocation";
    private static final String STOP_POLLING = "StopPollingLocation";
    private static final String POLLING_INTERVAL = "PollingInterval";

    @Inject
    public Preferences(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // TODO: 8/13/17 return more reasonable default values
    public void setNotificationTime(long notificationTime){
        preferences.edit()
                .putLong(NOTIFICATION_TIME, notificationTime)
                .apply();
    }

    public void setLocationFreq(int freq){
        preferences.edit()
                .putInt(LOCATION_FREQ, freq)
                .apply();
    }

    public void setGraphKeepTime(int numberOfDays){
        preferences.edit()
                .putInt(GRAPH_KEEP_TIME, numberOfDays)
                .apply();
    }

    public void setAnimateGraph(boolean flipAnimation){
        preferences.edit()
                .putBoolean(ANIMATE_GRAPH, flipAnimation)
                .apply();
    }

    public void setStartPolling(long startPolling){
        preferences.edit()
                .putLong(START_POLLING, startPolling)
                .apply();
    }

    public void setStopPolling(long stopPolling){
        preferences.edit()
                .putLong(STOP_POLLING, stopPolling)
                .apply();
    }

    public void setPollingInterval(int interval){
        preferences.edit()
                .putInt(POLLING_INTERVAL, interval)
                .apply();
    }

    public int getPollingInterval(){
        return preferences.getInt(POLLING_INTERVAL, 0);
    }

    public long getNotificationTime() {
        return preferences.getLong(NOTIFICATION_TIME, 0);
    }

    public int getLocationFreq() {
        return preferences.getInt(LOCATION_FREQ, 0);
    }

    public int getGraphKeepTime() {
        return preferences.getInt(GRAPH_KEEP_TIME, 0);
    }

    public boolean getAnimateGraph() {
        return preferences.getBoolean(ANIMATE_GRAPH, false);
    }

    public long getStartPolling(){
        return preferences.getLong(START_POLLING, 0);
    }

    public long getStopPolling(){
        return preferences.getLong(STOP_POLLING, 0);
    }
}
