package com.example.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if network is not available
        if (!isNetworkAvailable(context)) {
            // Notify the user about the lack of internet connection using a dialog
            showNoInternetDialog(context);
        }
    }

    // Method to check if network is available
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Method to show dialog for no internet connection
    private void showNoInternetDialog(Context context) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set title and message for the dialog
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check your internet connection and try again.");

        // Create the dialog
        AlertDialog dialog = builder.create();
        // Show the dialog
        dialog.show();
    }
}


