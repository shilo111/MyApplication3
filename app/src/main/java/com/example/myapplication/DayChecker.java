package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;

public class DayChecker {

    private static final String PREF_NAME = "DayCheckerPrefs";
    private static final String LAST_CHECKED_DAY_KEY = "lastCheckedDay";
    private final SharedPreferences sharedPreferences;

    public DayChecker(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasDayChanged() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        int lastCheckedDay = sharedPreferences.getInt(LAST_CHECKED_DAY_KEY, -1);

        if (currentDay != lastCheckedDay) {
            // Update last checked day in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(LAST_CHECKED_DAY_KEY, currentDay);
            editor.apply();
            return true; // Day has changed
        } else {
            return false; // Day has not changed
        }
    }
}

