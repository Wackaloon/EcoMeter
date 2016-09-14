package com.alexanderageychenko.ecometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.alexanderageychenko.ecometer.Data.Depository;
import com.alexanderageychenko.ecometer.Fragments.add.AddFragment;
import com.alexanderageychenko.ecometer.Fragments.details.DetailsFragment;
import com.alexanderageychenko.ecometer.Fragments.home.HomeFragment;
import com.alexanderageychenko.ecometer.Logic.DialogBuilder;
import com.alexanderageychenko.ecometer.Logic.FragmentCroupier;
import com.alexanderageychenko.ecometer.Model.ExActivity;

public class MainActivity extends ExActivity implements View.OnClickListener {

    private FragmentCroupier fragmentCroupier = new FragmentCroupier();
    private HomeFragment homeFragment = new HomeFragment();
    private AddFragment addFragment = new AddFragment();
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setOnClickListener(this);
        initFragments();
    }
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            MainApplication.SIGNAL_TYPE signal_type = (MainApplication.SIGNAL_TYPE) intent.getSerializableExtra(MainApplication.SIGNAL_NAME);
            switch (signal_type) {
                case OPEN_DETAILS:{
                    DetailsFragment meterDetails = DetailsFragment.newInstance(intent.getIntExtra("id", -1));
                    fragmentCroupier.addFragment("main", meterDetails, true, R.animator.in_right, MainApplication.noAnimId, R.animator.no_anim, R.animator.out_right);
                    fab.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };
    

    private void initFragments(){
        fragmentCroupier.init(getFragmentManager(), R.id.content_frame);
        fragmentCroupier.addFragment("main", homeFragment);
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == fab){
            if (fragmentCroupier.getLastLoadTag().equals("main")){
                fab.setImageResource(R.drawable.ic_done_white_48dp);
                fab.setVisibility(View.GONE);
                fragmentCroupier.addFragment("main", addFragment, true, R.animator.alpha_in, MainApplication.noAnimId, R.animator.no_anim, R.animator.alpha_out);

            }

        }
    }

    @Override
    public void onBackPressed() {
        if (!fragmentCroupier.popBackStack() && fragmentCroupier.getLastLoadTag().equals("main")) {
            DialogBuilder.getExitDialog(this).show();
        }else{
            fab.setImageResource(android.R.drawable.ic_input_add);
            fragmentCroupier.loadBackStack("main");
            fab.setVisibility(View.VISIBLE);
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
