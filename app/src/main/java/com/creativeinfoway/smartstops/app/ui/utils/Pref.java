package com.creativeinfoway.smartstops.app.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    public final static String PREFS_NAME = "first_prefs";
    private static Context context;
    private SharedPreferences preference;

    public Pref(Context context) {
        this.context = context;
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }
}
