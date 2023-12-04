package com.example.myapplication;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver{
    String status="";
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null)  {
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI) {
                status="WiFi enabled";
            }
            if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)  {
                status="Mobile enabled ";
            }
        }
        else {
            status = "No internet is available. pay attention directing to home page without authentication";
            Intent go = new Intent(context,HomePage.class);
            context.startActivity(go);
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
