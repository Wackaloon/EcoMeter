package com.alexanderageychenko.ecometer;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import com.alexanderageychenko.ecometer.Model.ExContainers.ExFragment;
import com.alexanderageychenko.ecometer.Tools.DialogBuilder;
import com.alexanderageychenko.ecometer.Tools.Navigator.HomeFragmentTransactor;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainNavigator;
import com.alexanderageychenko.ecometer.Tools.Navigator.MainScreenType;
import com.alexanderageychenko.ecometer.Tools.Navigator.Navigator;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.View.settings.SettingsFragment;

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

        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // overwrite Navigation OnClickListener that is set by ActionBarDrawerToggle
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else if (isHomeAsUp) {
                    onBackPressed();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.MetersListScreen, null), R.anim.no_anim, R.anim.out_right);

        } else if (id == R.id.nav_meters_settings) {
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.MetersSettingsScreen, null), R.anim.in_right, R.anim.no_anim);

        } else if (id == R.id.nav_settings) {
            homeNavigator.openScreen(new Navigator.Screen<>(MainScreenType.SettingsScreen, null),R.anim.in_right, R.anim.no_anim);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send_feedback) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
