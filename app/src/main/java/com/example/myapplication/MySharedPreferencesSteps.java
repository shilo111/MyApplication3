package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MySharedPreferencesSteps {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String USER_DATA_KEY = "user_data_key";
    private static final Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

    // Method to save user-specific data in SharedPreferences
    public static void saveUserData(Context context, String userId, String date, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve existing user data map or create a new one
        Map<String, Map<String, Integer>> userDataMap = getUserDataMap(context);

        // Retrieve user's date-value map or create a new one
        Map<String, Integer> dateValueMap = userDataMap.getOrDefault(userId, new HashMap<>());

        // Update the user's date-value map with the new entry
        dateValueMap.put(date, value);

        // Update the user data map with the modified date-value map
        userDataMap.put(userId, dateValueMap);

        // Serialize the updated user data map to JSON
        String userDataJson = gson.toJson(userDataMap);

        // Save the serialized user data map to SharedPreferences
        editor.putString(USER_DATA_KEY, userDataJson);
        editor.apply();
    }

    // Method to retrieve user-specific data from SharedPreferences
    public static Map<String, Integer> getUserData(Context context, String userId) {
        // Retrieve user data map
        Map<String, Map<String, Integer>> userDataMap = getUserDataMap(context);

        // Retrieve user's date-value map from the map based on userId
        return userDataMap.get(userId);
    }
    // Method to retrieve user-specific data for a specific date from SharedPreferences
    // Method to retrieve user-specific data for a specific user from SharedPreferences
    public static Map<String, Integer> getUserDataForDate(Context context, String userId, String date) {
        Log.d("getUserDataForDate", "Date parameter value: " + date);
        // Retrieve user data map
        Map<String, Map<String, Integer>> userDataMap = getUserDataMap(context);

        // Retrieve user's date-value map from the map based on userId
        Map<String, Integer> dateValueMap = userDataMap.get(userId);

        // Return the date-value map for the specific user and date
        return dateValueMap;
    }

    // Helper method to retrieve the user data map from SharedPreferences
    private static Map<String, Map<String, Integer>> getUserDataMap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userDataJson = sharedPreferences.getString(USER_DATA_KEY, null);

        if (userDataJson != null) {
            // Deserialize JSON to retrieve the user data map
            Type type = new TypeToken<HashMap<String, Map<String, Integer>>>() {}.getType();
            return gson.fromJson(userDataJson, type);
        } else {
            // If no data exists, return a new empty map
            return new HashMap<>();
        }
    }

    // Method to check if a value has been set for a specific user and date
    public static boolean isValueSet(Context context, String userId, String date) {
        // Retrieve user's date-value map
        Map<String, Integer> userData = getUserData(context, userId);
        return userData != null && userData.containsKey(date);
    }

    // Method to obtain SharedPreferences object
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }
}

