package com.example.ahmed.walkgraph.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.ahmed.walkgraph.R;

import javax.inject.Inject;

/**
 * Created by ahmed on 11/8/17.
 */

public class WalkPreference {
    private SharedPreferences preferences;
    private Resources resources;
    private static final String LOCATION_FREQ = "FrequencyOfLocation";

    @Inject
    public WalkPreference(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
    }

    public boolean animateGraph(){
        return preferences.getBoolean(resources.getString(R.string.key_animate_graph),
                false);
    }

    public int keyDays(){
        return preferences.getInt(resources.getString(R.string.key_graph_keep), 3);
    }

    public int pollingInterval(){
        return preferences.getInt(resources.getString(R.string.key_polling_interval), 15);
    }

    public long startPollingTime(){
        return preferences.getLong(resources.getString(R.string.key_start_polling),
                Long.parseLong(resources.getString(R.string.start_time_def_value)));
    }

    public long stopPollingTime(){
        return preferences.getLong(resources.getString(R.string.key_stop_polling),
                Long.parseLong(resources.getString(R.string.stop_time_def_value)));
    }

    public long notificationTime(){
        return preferences.getLong(resources.getString(R.string.key_notification_time),
                Long.parseLong(resources.getString(R.string.notification_time_def_value)));
    }

    public void setLocationFreq(int startHour, int stopHour, int pollingInterval){
        int totalHours = stopHour - startHour;
        int totalMinutes = totalHours * 60;

        int locationFrequency = totalMinutes / pollingInterval;

        preferences.edit().putInt(LOCATION_FREQ, locationFrequency).apply();
    }

    public int getLocationFreq(){
        return preferences.getInt(LOCATION_FREQ, 40);
    }
}
