package com.alexanderageychenko.ecometer.Tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

/**
 * Created by alexanderageychenko on 9/17/17.
 */

public class ResTools {
    @Nullable
    public static Drawable getDrawable(Context c, int id) {
        if (c == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return c.getResources().getDrawable(id, c.getTheme());
        } else {
            //noinspection deprecation
            return c.getResources().getDrawable(id);
        }
    }

    public static int getColor(Context c, int id) {
        if (c == null) return Color.WHITE;
        return ContextCompat.getColor(c, id);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param resId A value in resources in dp (density independent pixels) unit.
     * Which we need to convert into dimension value multiplied by the appropriate
     * metric.
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static double getDimen(Context context, int resId) {
        return context.getResources().getDimension(resId);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param resId A value in resources in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int getDimenPixelSize(Context context, int resId) {
        return (int) context.getResources().getDimensionPixelSize(resId);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param designDpRes A value in design_dp_x (res) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertDesignDpToAndroidDp(int designDpRes, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = (float) getDimenPixelSize(context, designDpRes);
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
