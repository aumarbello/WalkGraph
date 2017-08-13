package com.example.ahmed.walkgraph.presentation.settings;

/**
 * Created by ahmed on 8/9/17.
 */

public interface SettingsPresenter {
    void saveAnimation(boolean isOn);
    void saveGraphKeepTime(int days);
    void saveGraphNotificationTime(long timeForNotification);
    void savePollingStartTime(long pollingStartTime);
    void savePollingStopTime(long pollingStopTime);
    void calculateAndSaveLocationFrequency();
    void pollingInterval(int interval);
}
