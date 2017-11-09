package com.example.ahmed.walkgraph.presentation.map;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

/**
 * Created by ahmed on 8/12/17.
 *
 * @author Ahmed Umar
 * TODO consider using this service to schedule jobs: START TRACKING,
 * TODO STOP TRACKING AND SHOW NOTIFICATIO.
 */

public class LocationScheduler extends JobService {
    private static final String TAG = "LocationScheduler";
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "OnStartJob called");
        //inject dependecies
        //inject(this)
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "OnStopJob called");
        return false;
    }
}
