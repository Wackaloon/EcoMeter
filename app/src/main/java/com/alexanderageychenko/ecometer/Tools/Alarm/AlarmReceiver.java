package com.alexanderageychenko.ecometer.Tools.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alexanderageychenko.ecometer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander on 24.09.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //Schedule alarm on BOOT_COMPLETED
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            scheduleAlarm(context);
        }
    }

    /* Schedule the alarm based on user preferences */
    public static void scheduleAlarm(Context context) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        String keyReminder = context.getString(R.string.pref_key_reminder);
        String keyAlarm = context.getString(R.string.pref_key_alarm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean enabled = preferences.getBoolean(keyReminder, false);

        //Intent to trigger
        Intent intent = new Intent(context, ReminderService.class);
        PendingIntent operation = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SimpleDateFormat logForamt = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        if (enabled) {
            //Gather the time preference
            Calendar startTime = Calendar.getInstance();

            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            startTime.set(Calendar.MILLISECOND, 0);
            startTime.set(Calendar.DAY_OF_MONTH, 1);
            startTime.add(Calendar.MONTH, 1);
            try {
                String alarmPref = preferences.getString(keyAlarm, "12:00");
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date date = format.parse(alarmPref);
                Calendar oldGoodDays = Calendar.getInstance();
                oldGoodDays.setTime(date);
                int targetHour = oldGoodDays.get(Calendar.HOUR_OF_DAY);
                int targetMinute = oldGoodDays.get(Calendar.MINUTE);

                startTime.set(Calendar.HOUR_OF_DAY, targetHour);
                startTime.set(Calendar.MINUTE, targetMinute);
            } catch (ParseException e) {
                Log.w(TAG, "Unable to determine alarm start time", e);
                return;
            }

            //Start at the preferred time
            //If that time has passed today, set for tomorrow
            if (Calendar.getInstance().after(startTime)) {
                startTime.add(Calendar.DATE, 1);
            }

            Log.d(TAG, "Scheduling quiz reminder alarm" + logForamt.format(startTime.getTime()));
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                manager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), operation);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), operation);
            }
        } else {
            Log.d(TAG, "Disabling quiz reminder alarm");
            manager.cancel(operation);
        }
    }

}
