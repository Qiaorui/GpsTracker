package com.sparsity.gpstracker;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class GpsTrackerBootReceiver extends BroadcastReceiver {
    private static final String TAG = "GpsTrackerBootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootReceive");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            context.startForegroundService(new Intent(context, LocationService.class));
        } else {
            context.startService(new Intent(context, LocationService.class));
        }

    }
}