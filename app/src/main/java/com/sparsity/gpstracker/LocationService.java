package com.sparsity.gpstracker;

/**
 * Created by qiaoruixiang on 30/01/2018.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.android.gms.location.LocationCallback;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private static final String CHANNEL_ID = "channel_01";
    private static final int NOTIFICATION_ID = 339922;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
            startForeground(NOTIFICATION_ID, getNotification());
        } else {
            startForeground(1,new Notification());
        }
    }


    private Notification getNotification() {
        CharSequence text = "We are staking you";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentText(text)
                .setContentTitle("Gps Tracker")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        startTracking();

        return START_STICKY;
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(30*1000); // milliseconds
        locationRequest.setFastestInterval(10*1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    onLocationChanged(locationResult.getLastLocation());
                }
            }, Looper.myLooper());
        } catch (SecurityException se) {
            Log.e(TAG, se.getMessage());
            Log.e(TAG, "Go into settings and find Gps Tracker app and enable Location.");
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: " + location.getAccuracy());

            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location uodates
            if (location.getAccuracy() < 500.0f) {
                //stopLocationUpdates();

                //TODO: send locations
            }
        }
    }

    private void stopLocationUpdates() {
        Log.d(TAG, "Disconnect location update");

        stopSelf();
    }
}