package com.nhodev.service_power.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.nhodev.service_power.service.PlayMusicService;

/**
 * Được tạo bởi Phạm Nhớ ngày 01/11/2021.
 **/
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, PlayMusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        Log.d(TAG, "started");
    }
}
