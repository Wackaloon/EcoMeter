package com.alexanderageychenko.ecometer;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.DaggerAppComponent;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.AppModule;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.DepositoryModule;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.UIModule;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by alexanderageychenko 13.09.16.
 */
public class MainApplication extends Application {

    //static
    static public IntentFilter GLOBAL_FILTER;
    static public String FILTER_ACTION_NAME;
    static public String SIGNAL_NAME = "SIGNAL";
    static public int noAnimId =  R.anim.emty_animation;
    static public int currentApiVersion;
    //
    static private MainApplication mainApplication;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SIGNAL_TYPE signal_type = (SIGNAL_TYPE) intent.getSerializableExtra(MainApplication.SIGNAL_NAME);
            if (signal_type == SIGNAL_TYPE.LOG) {
                Log.d("DEBUG", intent.getStringExtra("log"));
            }
        }
    };


    public MainApplication() {
        mainApplication = this;
    }

    static public MainApplication getInstance() {
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        FILTER_ACTION_NAME = getApplicationContext().getPackageName();
        GLOBAL_FILTER = new IntentFilter(FILTER_ACTION_NAME);

        //fontPath="fonts/Lucida-Grande-Regular.ttf"
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(com.alexanderageychenko.ecometer.R.attr.fontPath)
                .build()
        );

        apiCheck();

        registerReceiver(broadcastReceiver, GLOBAL_FILTER);

        Dagger.setAppComponent(DaggerAppComponent.builder()
                .depositoryModule(new DepositoryModule())
                .appModule(new AppModule(MainApplication.this))
                .uIModule(new UIModule(this))
                .build());
    }


    @Override
    public void onTerminate() {
        unregisterReceiver(broadcastReceiver);
        super.onTerminate();
    }

    private void apiCheck() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion <= Build.VERSION_CODES.KITKAT) {
            noAnimId = com.alexanderageychenko.ecometer.R.animator.hide;
        }
    }


    public enum SIGNAL_TYPE {
        LOG, OPEN_DETAILS, OPEN_EDIT_METER, OPEN_CREATE_METER, OPEN_ADD_VALUE,
    }

    public static class ACTIVITY_RESULT_REQUEST_KEY {
        private static int id = 0;
        public static int SOME_REQUEST = id++;
    }
}
