package com.example.ahmed.walkgraph.presentation.settings;

import com.example.ahmed.walkgraph.data.prefs.Preferences;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/9/17.
 */

public class SettingsPresenterImpl implements SettingsPresenter {
    private Preferences preferences;

    @Inject
    public SettingsPresenterImpl(Preferences preferences){
        this.preferences = preferences;
    }

    @Override
    public void saveAnimation(boolean isOn) {
        preferences.setAnimateGraph(isOn);
    }

    @Override
    public void saveGraphKeepTime(int days) {
        preferences.setGraphKeepTime(days);
    }

    @Override
    public void saveGraphNotificationTime(long timeForNotification) {
        preferences.setNotificationTime(timeForNotification);
    }

    @Override
    public void savePollingStartTime(long pollingStartTime) {
        preferences.setStartPolling(pollingStartTime);
    }

    @Override
    public void savePollingStopTime(long pollingStopTime) {
        preferences.setStopPolling(pollingStopTime);
    }

    @Override
    public void calculateAndSaveLocationFrequency() {
        int freq = 15;
        //do actual calculation using start and end polling time to know number of ours
        //then divide by interval
        preferences.setLocationFreq(freq);
    }

    @Override
    public void pollingInterval(int interval) {
        preferences.setPollingInterval(interval);
    }
    // TODO: 8/10/17 perform call to db in different thread, consider using observable
}
