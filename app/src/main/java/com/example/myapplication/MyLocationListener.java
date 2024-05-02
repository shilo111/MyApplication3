package com.example.myapplication;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.myapplication.ui.dashboard.DashboardFragment;

public class MyLocationListener implements LocationListener {

    private DashboardFragment dashboardFragment; // Reference to the DashboardFragment

    public MyLocationListener(DashboardFragment fragment) {
        this.dashboardFragment = fragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Call the handleLocationUpdates method of the DashboardFragment
        if (dashboardFragment != null) {
            dashboardFragment.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Implementation of onStatusChanged method (optional)
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Implementation of onProviderEnabled method (optional)
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Implementation of onProviderDisabled method (optional)
    }
}

