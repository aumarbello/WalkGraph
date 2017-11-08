package com.example.ahmed.walkgraph.notifications;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ahmed.walkgraph.App;
import com.example.ahmed.walkgraph.data.local.prefs.WalkPreference;
import com.example.ahmed.walkgraph.utils.NetworkUtil;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/23/17.
 */

public class TimeService extends IntentService {
    private static final String TAG = "TimeService";
    private static final int REPEAT_TIME = 1000;

    @Inject
    LocationService service;

    @Inject
    WalkPreference preference;

    public TimeService() {
        super(TAG);
    }

    public static Intent getIntent(Context context){
        return new Intent(context, TimeService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Time service started");
        ((App)getApplicationContext()).getComponent().inject(this);
        if (!NetworkUtil.isNetworkConnected(this)){
            return;
        }

        Calendar startCalender = Calendar.getInstance();
        long startDateLong = preference.startPollingTime();
        startCalender.setTimeInMillis(startDateLong);

        Calendar stopCalender = Calendar.getInstance();
        long stopDateLong = preference.stopPollingTime();
        stopCalender.setTimeInMillis(stopDateLong);

        Calendar notificationCalender = Calendar.getInstance();
        long notificationDateLong = preference.notificationTime();
        notificationCalender.setTimeInMillis(notificationDateLong);

        Calendar currentTime = Calendar.getInstance();

        if (currentTime.get(Calendar.HOUR_OF_DAY) ==
                startCalender.get(Calendar.HOUR_OF_DAY)){
            Intent locationService = new Intent(this, LocationService.class);
            startService(locationService);
        }

        if (currentTime.get(Calendar.HOUR_OF_DAY) ==
                stopCalender.get(Calendar.HOUR_OF_DAY)){
            Intent locationService = new Intent(this, LocationService.class);
            stopService(locationService);
        }

        if (currentTime.get(Calendar.HOUR_OF_DAY) ==
                notificationCalender.get(Calendar.HOUR_OF_DAY)){
            service.showNotification();
        }
    }

    public static void setTimeInterval(Context context){
        Intent selfIntent = getIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                selfIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService
                (Context.ALARM_SERVICE);

        if (isTimeAlarmOn(context)){
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), REPEAT_TIME,
                    pendingIntent);
        }else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static boolean isTimeAlarmOn(Context context) {
        Intent intent = TimeService.getIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
