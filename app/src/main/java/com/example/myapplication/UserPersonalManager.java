package com.example.myapplication;import android.content.Context;
import android.content.SharedPreferences;

public class UserPersonalManager {
    private static final String SHARED_PREF_PREFIX = "UserPersonalData_";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_BMI = "bmi";
    private static final String KEY_BODY_FAT = "bodyFat";
    private static final String KEY_AGE = "age";
    private static final String KEY_GOAL_STEP = "goalStep";
    private static final String KEY_CALORIES = "calories";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String userId;

    public UserPersonalManager(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_PREFIX + userId, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setPersonalData(double height, int weight, int bmi, String bodyFat, double age) {
        editor.putFloat(KEY_HEIGHT, (float) height);
        editor.putInt(KEY_WEIGHT, weight);
        editor.putInt(KEY_BMI, bmi);
        editor.putString(KEY_BODY_FAT, bodyFat);
        editor.putFloat(KEY_AGE, (float) age);
        editor.apply();
    }

    public void setGoalStep(int goalStep) {
        editor.putInt(KEY_GOAL_STEP, goalStep);
        editor.apply();
    }

    public void setCalories(int calories) {
        editor.putInt(KEY_CALORIES, calories);
        editor.apply();
    }

    public double getHeight() {
        return sharedPreferences.getFloat(KEY_HEIGHT, 0);
    }

    public int getWeight() {
        return sharedPreferences.getInt(KEY_WEIGHT, 0);
    }

    public int getBMI() {
        return sharedPreferences.getInt(KEY_BMI, 0);
    }

    public String getBodyFat() {
        return sharedPreferences.getString(KEY_BODY_FAT, "");
    }

    public double getAge() {
        return sharedPreferences.getFloat(KEY_AGE, 0);
    }
    public int getGoalStep() {
        return sharedPreferences.getInt(KEY_GOAL_STEP, 0);
    }

    public int getCalories() {
        return sharedPreferences.getInt(KEY_CALORIES, 0);
    }
}


