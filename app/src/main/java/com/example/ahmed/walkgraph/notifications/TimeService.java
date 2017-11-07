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
import com.example.ahmed.walkgraph.data.prefs.Preferences;
import com.example.ahmed.walkgraph.utils.NetworkUtil;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by ahmed on 8/23/17.
 */

public class TimeService extends IntentService {
    private static final String TAG = "TimeService";

    @Inject
    LocationService service;
    @Inject
    Preferences preferences;

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
        long startDateLong = preferences.getStartPolling();
        Date startDate = new Date(startDateLong);
        startCalender.setTime(startDate);

        Calendar stopCalender = Calendar.getInstance();
        long stopDateLong = preferences.getStopPolling();
        Date stopDate = new Date(stopDateLong);
        stopCalender.setTime(stopDate);

        Calendar notificationCalender = Calendar.getInstance();
        long notificationDateLong = preferences.getStartPolling();
        Date notificationDate = new Date(notificationDateLong);
        notificationCalender.setTime(notificationDate);

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
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR,
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
