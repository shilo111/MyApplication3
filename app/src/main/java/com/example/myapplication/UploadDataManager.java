package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;

public class UploadDataManager {
    private static final String SHARED_PREF_NAME = "UploadData";
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_FILE_PATH = "filePath";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UploadDataManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUploadData(String caption, String filePath) {
        editor.putString(KEY_CAPTION, caption);
        editor.putString(KEY_FILE_PATH, filePath);
        editor.apply();
    }

    public String getCaption() {
        return sharedPreferences.getString(KEY_CAPTION, "");
    }

    public String getFilePath() {
        return sharedPreferences.getString(KEY_FILE_PATH, "");
    }
}

