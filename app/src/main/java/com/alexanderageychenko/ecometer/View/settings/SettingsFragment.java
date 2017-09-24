package com.alexanderageychenko.ecometer.View.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.Alarm.AlarmReceiver;

/**
 * Created by Alexander on 24.09.17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //this will immediately reschedule or cancel the reminder
        AlarmReceiver.scheduleAlarm(getActivity());
    }
}
