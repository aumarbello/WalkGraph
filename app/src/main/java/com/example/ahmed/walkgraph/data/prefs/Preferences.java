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

    @Inject
    public Preferences(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setNotificationTime(long notificationTime){
        preferences.edit()
                .putLong(NOTIFICATION_TIME, notificationTime)
                .apply();
    }

    public static void setLocationFreq(int freq){
        preferences.edit()
                .putInt(LOCATION_FREQ, freq)
                .apply();
    }

    public static void setGraphKeepTime(int numberOfDays){
        preferences.edit()
                .putInt(GRAPH_KEEP_TIME, numberOfDays)
                .apply();
    }

    public static void setAnimateGraph(boolean flipAnimation){
        preferences.edit()
                .putBoolean(ANIMATE_GRAPH, flipAnimation)
                .apply();
    }

    public static long getNotificationTime() {
        return preferences.getLong(NOTIFICATION_TIME, 0);
    }

    public static int getLocationFreq() {
        return preferences.getInt(LOCATION_FREQ, 0);
    }

    public static int getGraphKeepTime() {
        return preferences.getInt(GRAPH_KEEP_TIME, 0);
    }

    public static boolean getAnimateGraph() {
        return preferences.getBoolean(ANIMATE_GRAPH, false);
    }
}
