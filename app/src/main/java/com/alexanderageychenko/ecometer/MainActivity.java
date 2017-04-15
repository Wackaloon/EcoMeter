package com.alexanderageychenko.ecometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alexanderageychenko.ecometer.Fragments.add.AddValueFragment;
import com.alexanderageychenko.ecometer.Fragments.details.DetailsFragment;
import com.alexanderageychenko.ecometer.Fragments.edit.EditMeterFragment;
import com.alexanderageychenko.ecometer.Fragments.home.HomeFragment;
import com.alexanderageychenko.ecometer.Fragments.settings.SettingsFragment;
import com.alexanderageychenko.ecometer.Logic.DialogBuilder;
import com.alexanderageychenko.ecometer.Logic.FragmentCroupier;
import com.alexanderageychenko.ecometer.Model.ExContainers.ExActivity;

public class MainActivity extends ExActivity{

    private FragmentCroupier fragmentCroupier = new FragmentCroupier();
    private HomeFragment homeFragment = new HomeFragment();
    private AddValueFragment addValueFragment = new AddValueFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragments();
    }
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            MainApplication.SIGNAL_TYPE signal_type = (MainApplication.SIGNAL_TYPE) intent.getSerializableExtra(MainApplication.SIGNAL_NAME);
            switch (signal_type) {
                case OPEN_DETAILS:{
                    DetailsFragment meterDetails = DetailsFragment.newInstance();
                    fragmentCroupier.addFragment("main", meterDetails, true, R.animator.in_right, MainApplication.noAnimId, R.animator.no_anim, R.animator.out_right);
                    break;
                }
                case OPEN_EDIT_METER:
                case OPEN_CREATE_METER:{
                    EditMeterFragment editMeterFragment = EditMeterFragment.newInstance();
                    fragmentCroupier.addFragment("main", editMeterFragment, true, R.animator.in_right, MainApplication.noAnimId, R.animator.no_anim, R.animator.out_right);
                    break;
                }
                case OPEN_ADD_VALUE:{
                    fragmentCroupier.addFragment("main", addValueFragment, true, R.animator.alpha_in, MainApplication.noAnimId, R.animator.no_anim, R.animator.alpha_out);
                    break;
                }
            }
        }
    };
    

    private void initFragments(){
        fragmentCroupier.init(getFragmentManager(), R.id.content_frame);
        fragmentCroupier.addFragment("main", homeFragment);
        fragmentCroupier.addFragment("settings", settingsFragment);
        fragmentCroupier.loadBackStack("main");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentCroupier.loadBackStack("settings", R.animator.in_right, MainApplication.noAnimId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!fragmentCroupier.popBackStack() && fragmentCroupier.getLastLoadTag().equals("main")) {
            DialogBuilder.getExitDialog(this).show();
        }else{
            fragmentCroupier.loadBackStack("main", MainApplication.noAnimId, R.animator.out_right);
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
            unregisterReceiver(broadcastReceiver);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.onStop();
    }
}
