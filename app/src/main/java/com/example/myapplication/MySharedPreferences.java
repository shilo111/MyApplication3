package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String BOOLEAN_KEY = "boolean_key";
    private static final String BOOLEAN_SET_KEY = "boolean_set_key";

    // Method to save a boolean value in SharedPreferences
    public static void saveBoolean(Context context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BOOLEAN_KEY, value);
        editor.putBoolean(BOOLEAN_SET_KEY, true); // Mark the boolean as set
        editor.apply();
    }

    // Method to retrieve a boolean value from SharedPreferences
    public static boolean getBoolean(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve the boolean value, provide a default value (false in this case) if the key is not found
        return sharedPreferences.getBoolean(BOOLEAN_KEY, false);
    }

    // Method to check if the boolean value has been set
    public static boolean isBooleanSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve the boolean set flag, default is false
        return sharedPreferences.getBoolean(BOOLEAN_SET_KEY, false);
    }
}


