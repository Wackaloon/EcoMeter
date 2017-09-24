package com.alexanderageychenko.ecometer.View.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexanderageychenko.ecometer.MainActivity;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;
import com.alexanderageychenko.ecometer.R;
import com.alexanderageychenko.ecometer.Tools.Alarm.AlarmReceiver;

/**
 * Created by Alexander on 24.09.17.
 */

public class SettingsExFragment extends ExFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_subframe, new SettingsFragment())
                .commit();
    }
}

