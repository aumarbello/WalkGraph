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
 *
 * PreferenceFragment class to allow user customization.
 */


public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{
    /**
     * Field
     */
    @Inject
    WalkPreference preference;

    /**
     * add preference from xml..
     *
     * Inject fields.
     *
     * Locate preferences at runtime that need updated summary displayed and update.
     * @param savedInstanceState of the fragment.
     */

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

        //timeInMilliSeconds for prefs
        long startLong = preference.startPollingTime();
        long stopLong = preference.stopPollingTime();
        long notificationLong = preference.notificationTime();

        //setting summary for prefs
        setPrefSummary(startPolling, startLong);
        setPrefSummary(stopPolling, stopLong);
        setPrefSummary(notificationTime, notificationLong);

        ListPreference numberOfKeepDays = (ListPreference) findPreference
                (getString(R.string.key_graph_keep));
        int number = preference.keyDays();
        numberOfKeepDays.setSummary(number + " Days");

        ListPreference intervalPref = (ListPreference) findPreference
                (getString(R.string.key_polling_interval));
        int interval = preference.pollingInterval();
        intervalPref.setSummary(interval + " Minutes");
    }

    /**
     * Generate String for a time Preference.
     * @param hour of the preference.
     * @param minute of the preference.
     * @return String representation on based on the parameters passed.
     */
    private String timeString(int hour, int minute){
        return hour + ":" + minute;
    }

    /**
     * Extract the relevant fields from a calender object.
     * @param calendar to used in getting fields.
     * @return int array contain the hour and minute in the calender.
     */
    private int[] getTimeFields(Calendar calendar){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new int[]{hour, minute};
    }

    /**
     * Updates the summary of a preference to a human readable string.
     * @param timePreference to update.
     * @param timeInMillSec of the preference's calender.
     */
    private void setPrefSummary(TimePreference timePreference, long timeInMillSec){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillSec);

        int[] timeFields = getTimeFields(calendar);
        timePreference.setSummary(timeString(timeFields[0], timeFields[1]));
    }

    /**
     * Listener for sharedPrefs, checks if the pref's summary shld be updated and does so.
     * @param sharedPreferences to get the set value from.
     * @param key of the sharedPreference that was changed.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_graph_keep))){
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(sharedPreferences.getString(key, "3") + " Days");
        }else if (key.equals(getString(R.string.key_polling_interval))){
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(sharedPreferences.getString(key, "15") + " Minutes");
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

    /**
     * Registering listener.
     */
    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    /**
     * UnRegistering listener.
     */
    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
