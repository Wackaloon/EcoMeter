package com.alexanderageychenko.ecometer;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by alexanderageychenko 13.09.16.
 */
public class MainApplication extends Application {

    //static
    static public IntentFilter GLOBAL_FILTER;
    static public String FILTER_ACTION_NAME;
    static public String SIGNAL_NAME = "SIGNAL";
    static public int noAnimId = com.alexanderageychenko.ecometer.R.animator.no_anim;
    static public int currentApiVersion;
    //
    static private MainApplication mainApplication;
    public String android_id;
    private SharedPreferences settings;
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

        FILTER_ACTION_NAME = getApplicationContext().getPackageName();
        GLOBAL_FILTER = new IntentFilter(FILTER_ACTION_NAME);


        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("DEBUG", "android_id:" + android_id);

        //fontPath="fonts/Lucida-Grande-Regular.ttf"
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(com.alexanderageychenko.ecometer.R.attr.fontPath)
                .build()
        );

        apiCheck();

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);



        registerReceiver(broadcastReceiver, GLOBAL_FILTER);


        Log.d("DEBUG_CACHE", "cacheDir: " + getCacheDir().getPath() + " externalCacheDir: " + getExternalCacheDir());

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("DEBUG", AdvertisingIdClient.getAdvertisingIdInfo(MainApplication.this).getId());
                } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/


    }


    @Override
    public void onTerminate() {
        unregisterReceiver(broadcastReceiver);
        super.onTerminate();
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    private void apiCheck() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion <= Build.VERSION_CODES.KITKAT) {
            noAnimId = com.alexanderageychenko.ecometer.R.animator.hide;
        }
    }


    public enum SIGNAL_TYPE {
        LOG, OPEN_DETAILS,
    }

    public static class ACTIVITY_RESULT_REQUEST_KEY {
        private static int id = 0;
        public static int TAKE_A_PICTURE = id++;
        public static int CHOOSE_A_PICTURE = id++;
        public static int GOOGLE_PLUS_AUTH = id++;
        public static int GOOGLE_PLUS_GET_PROFILE = id++;
        public static int IN_APP_BILLING_PURHASING_RESPONSE = id++;
        public static int IN_APP_BILLING_REMOVE_ADS = id++;
    }
}
