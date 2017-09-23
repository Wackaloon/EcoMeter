package com.alexanderageychenko.ecometer;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;
import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.Navigator.HomeFragmentTransactor;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType;
import com.alexanderageychenko.ecometer.Tools.Navigator.Navigator;
import com.alexanderageychenko.ecometer.View.settings.SettingsActivity;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Inject
    public MainNavigator homeNavigator;

    final HomeFragmentTransactor homeFragmentTransactor = new HomeFragmentTransactor(this);
    private String[] mMenuItemsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private boolean isHomeAsUp = false;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        //Set the custom toolbar
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // overwrite Navigation OnClickListener that is set by ActionBarDrawerToggle
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else if (isHomeAsUp){
                    onBackPressed();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ExFragment homeFragment = (ExFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (homeFragment != null && homeFragment.popBackStack())
                return;
            DialogBuilder.getExitDialog(this).show();
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.SettingsScreen, null), R.anim.in_right_accel_decel, R.anim.out_left_accel_decel);
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showBackButtonOnBurger(false);
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.MetersListScreen, null), R.anim.in_right_accel_decel, R.anim.out_left_accel_decel);

        } else if (id == R.id.nav_meters_settings) {
            showBackButtonOnBurger(true);
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.MetersSettingsScreen, null), R.anim.in_right_accel_decel, R.anim.out_left_accel_decel);

        } else if (id == R.id.nav_settings) {
            Intent startSetting = new Intent(this, SettingsActivity.class);
            startActivity(startSetting);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * To be semantically or contextually correct, maybe change the name
     * and signature of this function to something like:
     *
     * private void showBackButton(boolean show)
     * Just a suggestion.
     */
    public void showBackButtonOnBurger(boolean enable) {
        setHomeAsUp(enable);
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
//        if(enable) {
//            // Remove hamburger
//            mDrawerToggle.setDrawerIndicatorEnabled(false);
//            // Show back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
//            // clicks are disabled i.e. the UP button will not work.
//            // We need to add a listener, as in below, so DrawerToggle will forward
//            // click events to this listener.
//            if(!mToolBarNavigationListenerIsRegistered) {
//                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Doesn't have to be onBackPressed
//                        onBackPressed();
//                    }
//                });
//
//                mToolBarNavigationListenerIsRegistered = true;
//            }
//
//        } else {
//            // Remove back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            // Show hamburger
//            mDrawerToggle.setDrawerIndicatorEnabled(true);
//            // Remove the/any drawer toggle listener
//            mDrawerToggle.setToolbarNavigationClickListener(null);
//            mToolBarNavigationListenerIsRegistered = false;
//        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }
    // call this method for animation between hamburged and arrow
    protected void setHomeAsUp(boolean isHomeAsUp){
        if (this.isHomeAsUp != isHomeAsUp) {
            this.isHomeAsUp = isHomeAsUp;

            ValueAnimator anim = isHomeAsUp ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            // You can change this duration to more closely match that of the default animation.
            anim.setDuration(400);
            anim.start();
        }
    }
}
