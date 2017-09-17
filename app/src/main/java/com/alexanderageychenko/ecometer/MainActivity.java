package com.alexanderageychenko.ecometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.Navigator.HomeFragmentTransactor;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType;
import com.alexanderageychenko.ecometer.Tools.Navigator.Navigator;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.View.ExFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {


    @Inject
    public MainNavigator homeNavigator;

    final HomeFragmentTransactor homeFragmentTransactor = new HomeFragmentTransactor(this);

    public MainActivity() {
        Dagger.get().getInjector().inject(this);
        if (homeNavigator.getActiveScreen().type == MainScreenType.Null) {
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.MetersListScreen, null), 0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        ExFragment homeFragment = (ExFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (homeFragment != null && homeFragment.popBackStack())
            return;
        DialogBuilder.getExitDialog(this).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        homeFragmentTransactor.start();
    }

    @Override
    protected void onStop() {
        try {
            Dagger.get().getGetter().depository().getIMetersDepository().saveMeters();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        homeFragmentTransactor.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeFragmentTransactor.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        homeFragmentTransactor.unsubscribe();
    }
}
