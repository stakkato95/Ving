package com.github.stakkato95.ving.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Artyom on 05.02.2015.
 */
public class MetricsUtils {

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float pxToSp(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static int spToPx(int sp, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * (int)scaledDensity;
    }

}
