package com.example.ahmed.walkgraph.presentation.settings;

/**
 * Created by ahmed on 8/9/17.
 */

public interface SettingsFragment {
    void saveAnimation();
    void saveGraphKeepTime();
    void saveGraphNotificationTime();
    void savePollingStartTime();
    void savePollingStopTime();
    void calculateAndSaveLocationFrequency();
    void pollingInterval();
    void updatePreferences();
    void setNavListener();
}
