package com.alexanderageychenko.ecometer.Tools;

import android.provider.Settings;
import android.support.annotation.NonNull;

import com.alexanderageychenko.ecometer.Model.Entity.GlobalCallback;
import com.alexanderageychenko.ecometer.Tools.dagger2.Dagger;
import com.crashlytics.android.Crashlytics;

/**
 * Created by alexanderageychenko on 12/8/16.
 */

public class FabricLoggingHandler {
    private static String errorMessage = "";


    public static void log(@NonNull String error) {
        errorMessage = error;
        String[] errors = error.split("url =");
        if (errors.length == 2) {
            errorMessage = errors[0];
            String url = errors[1];
            if (url == null) url = "Unknown";
            Crashlytics.setString("url", url);
        }
        try {
            String android_id = Settings.Secure.getString(Dagger.get().getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Crashlytics.setUserIdentifier(android_id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        handleError();
    }

    private static void handleError() {
        if (errorMessage.contains(GlobalCallback.ErrorCode.SERVER_ISSUE.name())) {
            log500();
            return;
        }
        if (errorMessage.contains(GlobalCallback.ErrorCode.NOT_FOUND.name())) {
            log404();
            return;
        }
        if (errorMessage.contains(GlobalCallback.ErrorCode.NULL_ITEMS_RESPONSE.name())) {
            logNullItems();
            return;
        }
        if (errorMessage.contains(GlobalCallback.ErrorCode.AUTHORIZATION.name())) {
            logAuthorizationError();
            return;
        }

        unknownError();
    }

    private static void logAuthorizationError() {
        Crashlytics.setString("error_type", GlobalCallback.ErrorCode.AUTHORIZATION.name());
        Crashlytics.logException(new Throwable(errorMessage));
    }

    private static void logNullItems() {
        Crashlytics.setString("error_type", GlobalCallback.ErrorCode.NULL_ITEMS_RESPONSE.name());
        Crashlytics.logException(new Throwable(errorMessage));
    }

    private static void unknownError() {
        {
            Crashlytics.setString("error_type", "Unknown");
            Crashlytics.logException(new Throwable(errorMessage));
        }
    }

    private static void log500() {
        {
            Crashlytics.setString("error_type", "500");
            Crashlytics.logException(new Throwable(errorMessage));
        }
    }

    private static void log404() {
        {
            Crashlytics.setString("error_type", "404");
            Crashlytics.logException(new Throwable(errorMessage));
        }
    }
}
