package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class StepCountResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar midnightCalendar = Calendar.getInstance();
        int hour = midnightCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = midnightCalendar.get(Calendar.MINUTE);
        int seconds = midnightCalendar.get(Calendar.SECOND);

        if (hour == 00 && minutes == 00 && seconds == 00) {
            Toast.makeText(context, "asd", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "asd");

        }
    }
}
