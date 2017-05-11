package com.alexanderageychenko.ecometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.FragmentCroupier;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.View.add.AddValueFragment;
import com.alexanderageychenko.ecometer.View.details.DetailsFragment;
import com.alexanderageychenko.ecometer.View.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.View.home.HomeFragment;
import com.alexanderageychenko.ecometer.View.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentCroupier fragmentCroupier = new FragmentCroupier();
    private HomeFragment homeFragment = new HomeFragment();
    private AddValueFragment addValueFragment = new AddValueFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
    }

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            MainApplication.SIGNAL_TYPE signal_type = (MainApplication.SIGNAL_TYPE) intent.getSerializableExtra(MainApplication.SIGNAL_NAME);
            switch (signal_type) {
                case OPEN_DETAILS: {
                    fragmentCroupier.addFragment("main", DetailsFragment.newInstance(), true, android.R.anim.fade_in, MainApplication.noAnimId, MainApplication.noAnimId, android.R.anim.fade_out);
                    break;
                }
                case OPEN_EDIT_METER:
                case OPEN_CREATE_METER: {
                    fragmentCroupier.addFragment("main", EditMeterFragment.newInstance(), true, android.R.anim.fade_in, MainApplication.noAnimId, MainApplication.noAnimId, android.R.anim.fade_out);
                    break;
                }
                case OPEN_ADD_VALUE: {
                    fragmentCroupier.addFragment("main", addValueFragment, true, android.R.anim.fade_in, MainApplication.noAnimId, MainApplication.noAnimId, android.R.anim.fade_out);
                    break;
                }
                case OPEN_SETTINGS: {
                    fragmentCroupier.loadBackStack("settings", android.R.anim.fade_in, MainApplication.noAnimId);
                    break;
                }
            }
        }
    };


    private void initFragments() {
        fragmentCroupier.init(getSupportFragmentManager(), R.id.content_frame);
        fragmentCroupier.addFragment("main", homeFragment);
        fragmentCroupier.addFragment("settings", settingsFragment);
        fragmentCroupier.loadBackStack("main");
    }

    @Override
    public void onBackPressed() {
        if (!fragmentCroupier.popBackStack() && fragmentCroupier.getLastLoadTag().equals("main")) {
            DialogBuilder.getExitDialog(this).show();
        } else {
            fragmentCroupier.loadBackStack("main", MainApplication.noAnimId, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(MainApplication.FILTER_ACTION_NAME));
    }

    @Override
    protected void onStop() {
        try {
            Dagger.get().getGetter().depository().getIMetersDepository().saveMeters();
            unregisterReceiver(broadcastReceiver);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.onStop();
    }
}
