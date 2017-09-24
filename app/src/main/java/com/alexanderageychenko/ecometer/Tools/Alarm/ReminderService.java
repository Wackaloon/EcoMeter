package com.alexanderageychenko.ecometer.Tools.Alarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Alexander on 24.09.17.
 */

public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create action intent
        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //TODO: Add data elements to quiz launch

        Intent action = new Intent(this, MainActivity.class);


        final PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] {backIntent, action}, PendingIntent.FLAG_ONE_SHOT);


        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.gas_image)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);

        AlarmReceiver.scheduleAlarm(this);

    }
}
