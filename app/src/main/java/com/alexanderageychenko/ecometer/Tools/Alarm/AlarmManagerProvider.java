package com.alexanderageychenko.ecometer.Tools.Alarm;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by Alexander on 24.09.17.
 */

public class AlarmManagerProvider {
    private static AlarmManager sAlarmManager;

    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (sAlarmManager != null) {
            throw new IllegalStateException("Alarm Manager Already Set");
        }

        sAlarmManager = alarmManager;
    }

    /*package*/ static synchronized AlarmManager getAlarmManager(Context context) {
        if (sAlarmManager == null) {
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        return sAlarmManager;
    }
}
