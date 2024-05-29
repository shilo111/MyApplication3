package com.example.myapplication.ui.dashboard;

import android.Manifest; // Required for location permissions
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.PathActivity;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private MapView mapView; // View for displaying the map
    private GoogleMap googleMap; // Google map object
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001; // Request code for location permission
    private Polyline runningPathPolyline; // Polyline for the running path
    private List<LatLng> pathPoints = new ArrayList<>(); // List of points representing the path
    private LocationManager locationManager; // Manages location updates
    private float totalDistance = 0f; // Tracks total distance covered
    private Chronometer chronometer; // Chronometer for tracking time
    private TextView elapsedTimeTextView; // Displays elapsed time
    private boolean isTracking = false; // Flag to indicate if tracking is active
    private long timeWhenStopped = 0; // Keeps track of the chronometer time when stopped

    private CountDownTimer countDownTimer; // Timer for tracking duration

    private long elapsedRealtimeOffset = 0;
    private long timeElapsed = 0; // Keeps track of elapsed time
    private static final long TIMER_INTERVAL = 1000; // 1 second timer interval
    private static final long TRACKING_DURATION = 99999; // Duration of tracking

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false); // Inflate the layout

        mapView = root.findViewById(R.id.mapView); // Initialize the MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Set the callback for when the map is ready

        Button startButton = root.findViewById(R.id.startButton); // Start tracking button
        Button stopButton = root.findViewById(R.id.stopButton); // Stop tracking button
        chronometer = root.findViewById(R.id.chronometer); // Initialize the chronometer
        elapsedTimeTextView = root.findViewById(R.id.elapsedTimeTextView); // Initialize the elapsed time TextView

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking(); // Start tracking when the button is clicked
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Good job! See your stats on the button below!!", Toast.LENGTH_SHORT).show();
                stopTracking(); // Stop tracking when the button is clicked
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeWhenStopped", timeWhenStopped); // Save the time when the chronometer was stopped
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            timeWhenStopped = savedInstanceState.getLong("timeWhenStopped"); // Restore the saved time when the chronometer was stopped
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase(); // Calculate elapsed time
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis % 3600000) / 60000;
        int seconds = (int) (elapsedMillis % 60000) / 1000;
        elapsedTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds)); // Update the TextView
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map; // Initialize the GoogleMap object
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true); // Enable location layer on the map
            zoomToCurrentLocation(); // Zoom to the current location
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE); // Request location permissions
        }
    }

    private void zoomToCurrentLocation() {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permissions not granted, request them
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            // Check if GPS provider is enabled
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS provider not enabled, show message or prompt user to enable it
                Toast.makeText(requireContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get last known location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                // Zoom to current location
                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            } else {
                // Last known location not available, request location updates to get current location
                Toast.makeText(requireContext(), "Failed to get current location", Toast.LENGTH_SHORT).show();
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        // Zoom to current location when location is updated
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    }
                }, null);
            }
        } else {
            // Location manager not available, show error message
            Toast.makeText(requireContext(), "Location manager not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTracking() {
        pathPoints.clear(); // Clear previous path points
        totalDistance = 0f; // Reset total distance

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this); // Request location updates
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE); // Request location permissions
        }
        if (!isTracking) {
            countDownTimer = new CountDownTimer(TRACKING_DURATION - timeElapsed, TIMER_INTERVAL) { // Initialize the countdown timer
                @Override
                public void onTick(long millisUntilFinished) {
                    timeElapsed = TRACKING_DURATION - millisUntilFinished; // Update elapsed time
                    updateTimer(timeElapsed); // Update the timer TextView
                }

                @Override
                public void onFinish() {
                    stopTracking(); // Stop tracking when the timer finishes
                    Toast.makeText(requireContext(), "Tracking finished", Toast.LENGTH_SHORT).show();
                }
            }.start();
            isTracking = true; // Set tracking flag to true
        }
    }

    private void updateTimer(long elapsedTime) {
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime % 3600000) / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        elapsedTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds)); // Update the timer TextView
    }

    private void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(this); // Remove location updates
        }
        if (isTracking) {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Cancel the countdown timer
            }
            isTracking = false; // Set tracking flag to false
            timeElapsed = 0; // Reset elapsed time
            updateTimer(timeElapsed); // Update the timer TextView
        }

        // Navigate to PathDetailsActivity and pass data
        Intent intent = new Intent(requireContext(), PathActivity.class);
        intent.putExtra("pathPoints", pathPoints.toArray(new LatLng[0])); // Pass path points
        intent.putExtra("elapsedTime", timeElapsed); // Pass elapsed time
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); // Get the new location
        pathPoints.add(latLng); // Add new point to path

        if (pathPoints.size() > 1) {
            Location lastLocation = new Location("");
            lastLocation.setLatitude(pathPoints.get(pathPoints.size() - 2).latitude);
            lastLocation.setLongitude(pathPoints.get(pathPoints.size() - 2).longitude);
            totalDistance += lastLocation.distanceTo(location); // Calculate distance between points
        }

        if (runningPathPolyline == null) {
            runningPathPolyline = googleMap.addPolyline(new PolylineOptions()
                    .color(ContextCompat.getColor(requireContext(), R.color.path_color))
                    .width(10)
                    .addAll(pathPoints)); // Create a new polyline
        } else {
            runningPathPolyline.setPoints(pathPoints); // Update the polyline with new points
        }
    }

    // Other methods of LocationListener (onStatusChanged, onProviderEnabled, onProviderDisabled) can be implemented if needed

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause(); // Pause the MapView
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume(); // Resume the MapView
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy(); // Destroy the MapView
        if (locationManager != null) {
            locationManager.removeUpdates(this); // Remove location updates
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory(); // Handle low memory
    }
}
