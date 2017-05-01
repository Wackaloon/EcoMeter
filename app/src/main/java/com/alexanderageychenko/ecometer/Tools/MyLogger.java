package com.alexanderageychenko.ecometer.Tools;

import android.util.Log;

import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.alexanderageychenko.ecometer.Tools.dagger2.Module.MainModule;

public class MyLogger {
    static public void d(String tag, String message) {
        if (Dagger.get().getType() == MainModule.Type.ANDROID) {
            Log.d(tag, message);
        } else {
            System.out.println(tag + ": " + message);
        }
    }

    static public void e(String tag, String message) {
        if (Dagger.get().getType() == MainModule.Type.ANDROID) {
            Log.e(tag, message);
        } else {
            System.out.println(tag + ": " + message);
        }
    }

    static public void i(String tag, String message) {
        if (Dagger.get().getType() == MainModule.Type.ANDROID) {
            Log.i(tag, message);
        } else {
            System.out.println(tag + ": " + message);
        }
    }
}
