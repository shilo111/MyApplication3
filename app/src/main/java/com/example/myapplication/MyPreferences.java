package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private static final String PREF_NAME = "MyPrefsFile";
    private static final String KEY_HAS_LOGGED_IN = "has_logged_in";

    public static boolean hasUserEverLoggedIn(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(KEY_HAS_LOGGED_IN, false);
    }

    public static void setUserLoggedIn(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_HAS_LOGGED_IN, true);
        editor.apply();
    }
}

