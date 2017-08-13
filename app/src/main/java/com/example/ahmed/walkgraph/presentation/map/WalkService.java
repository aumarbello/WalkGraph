package com.example.ahmed.walkgraph.presentation.map;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by ahmed on 8/12/17.
 */

public class WalkService extends IntentService {
    public WalkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //use service to determine when to start/stop saving locations and show notification
    }
}
