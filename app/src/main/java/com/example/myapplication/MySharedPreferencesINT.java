package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
public class MySharedPreferencesINT {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String INTEGER_KEY = "integer_key";
    private static final String INTEGER_SET_KEY = "integer_set_key";

    // Method to save an integer value in SharedPreferences
    public static void saveInteger(Context context, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(INTEGER_KEY, value);
        editor.putBoolean(INTEGER_SET_KEY, true); // Mark the integer as set
        editor.apply();
    }

    // Method to retrieve an integer value from SharedPreferences
    public static int getInteger(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve the integer value, provide a default value (0 in this case) if the key is not found
        return sharedPreferences.getInt(INTEGER_KEY, 0);
    }

    // Method to check if the integer value has been set
    public static boolean isIntegerSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Retrieve the integer set flag, default is false
        return sharedPreferences.getBoolean(INTEGER_SET_KEY, false);
    }
}
