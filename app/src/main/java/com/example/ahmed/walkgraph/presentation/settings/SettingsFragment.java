package com.example.ahmed.walkgraph.presentation.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.local.prefs.WalkPreference;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Inject
    WalkPreference preference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        ((App)getActivity().getApplication()).getComponent().inject(this);

        //prefs
        TimePreference startPolling = (TimePreference) findPreference
                (getString(R.string.key_start_polling));
        TimePreference stopPolling = (TimePreference) findPreference
                (getString(R.string.key_stop_polling));
        TimePreference notificationTime = (TimePreference) findPreference
                (getString(R.string.key_notification_time));

        //calender for prefs
        long startLong = preference.startPollingTime();
        long stopLong = preference.stopPollingTime();
        long notificationLong = preference.notificationTime();

        //setting summary for prefs
        setPrefSummary(startPolling, startLong);
        setPrefSummary(stopPolling, stopLong);
        setPrefSummary(notificationTime, notificationLong);
    }

    private String timeString(int hour, int minute){
        return hour + ":" + minute;
    }

    private int[] getTimeFields(Calendar calendar){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new int[]{hour, minute};
    }

    private void setPrefSummary(TimePreference timePreference, long timeInMillSec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillSec);

        int[] timeFields = getTimeFields(calendar);
        timePreference.setSummary(timeString(timeFields[0], timeFields[1]));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_graph_keep))){
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(sharedPreferences.getInt(key, 3));
        }else if (key.equals(getString(R.string.key_polling_interval))){
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(sharedPreferences.getInt(key, 15));
        }else if (key.equals(getString(R.string.key_start_polling))){
            TimePreference preference = (TimePreference) findPreference(key);
            long prefLong = sharedPreferences.getLong(key,
                    Long.parseLong(getString(R.string.start_time_def_value)));
            setPrefSummary(preference, prefLong);
        }else if (key.equals(getString(R.string.key_stop_polling))){
            TimePreference preference = (TimePreference) findPreference(key);
            long prefLong = sharedPreferences.getLong(key,
                    Long.parseLong(getString(R.string.stop_time_def_value)));
            setPrefSummary(preference, prefLong);
        }else if (key.equals(getString(R.string.key_notification_time))){
            TimePreference preference = (TimePreference) findPreference(key);
            long prefLong = sharedPreferences.getLong(key,
                    Long.parseLong(getString(R.string.notification_time_def_value)));
            setPrefSummary(preference, prefLong);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
