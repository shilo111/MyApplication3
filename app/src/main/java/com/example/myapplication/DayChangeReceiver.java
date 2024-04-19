package com.example.myapplication;import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayChangeReceiver extends BroadcastReceiver {
    private static final String LAST_SAVED_DAY_KEY = "last_saved_day";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = MySharedPreferencesSteps.getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get current date
        Date currentDate = new Date();

// Define a date formatter for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

// Format the current date using the formatter
        String formattedDate2 = formatter.format(currentDate);

        // Get last saved day from SharedPreferences
        int lastSavedDay = sharedPreferences.getInt(LAST_SAVED_DAY_KEY, -1);
        Log.d("getUserDataForDate", "Date parameter value2: " + lastSavedDay);

        // Check if day has changed
        if (lastSavedDay != Integer.parseInt(formattedDate2)) {
            // Perform action for day change
            // For example, you can show a notification or update some data
            // Here, we'll just log the day change

            // Save boolean value to SharedPreferences using MySharedPreferencesSteps
            MySharedPreferences.saveBoolean(context, true);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = dateFormat.format(currentDate);
            System.out.println("Day changed to: " + formattedDate);

            // Save current day to SharedPreferences for future comparison
            editor.putInt(LAST_SAVED_DAY_KEY, Integer.parseInt(formattedDate2));
            editor.apply();



        }
    }
}


