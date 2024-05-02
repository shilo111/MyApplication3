package com.example.myapplication.ui.dashboard;

import android.Manifest;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Polyline runningPathPolyline;
    private List<LatLng> pathPoints = new ArrayList<>();
    private LocationManager locationManager;
    private float totalDistance = 0f;
    private Chronometer chronometer;
    private TextView elapsedTimeTextView;
    private CountDownTimer countDownTimer;
    private boolean isTracking = false;
    private long elapsedRealtimeOffset = 0;
    private long timeElapsed = 0;
    private static final long TIMER_INTERVAL = 1000; // 1 second
    private static final long TRACKING_DURATION = 60000; // 1 minute
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button startButton = root.findViewById(R.id.startButton);
        Button stopButton = root.findViewById(R.id.stopButton);
        chronometer = root.findViewById(R.id.chronometer);
        elapsedTimeTextView = root.findViewById(R.id.elapsedTimeTextView);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateElapsedTime();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();


            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "good job see your stats on the button below!!", Toast.LENGTH_SHORT).show();
                stopTracking();
            }
        });

        // Button to navigate to PathActivity
        Button viewPathButton = root.findViewById(R.id.viewPathButton);
        viewPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PathActivity.class);
                // Pass path data and elapsed time to PathActivity
                intent.putExtra("pathPoints", pathPoints.toArray(new LatLng[0]));
                intent.putExtra("elapsedTime", timeElapsed);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("elapsedRealtimeOffset", elapsedRealtimeOffset);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            elapsedRealtimeOffset = savedInstanceState.getLong("elapsedRealtimeOffset");
        }
    }
    @SuppressLint("DefaultLocale")
    private void updateElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        elapsedTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            zoomToCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
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
        pathPoints.clear();
        totalDistance = 0f;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this);
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        if (!isTracking) {
            countDownTimer = new CountDownTimer(TRACKING_DURATION - timeElapsed, TIMER_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeElapsed = TRACKING_DURATION - millisUntilFinished;
                    updateTimer(timeElapsed);
                }

                @Override
                public void onFinish() {
                    stopTracking();
                    Toast.makeText(requireContext(), "Tracking finished", Toast.LENGTH_SHORT).show();
                }
            }.start();
            isTracking = true;
        }
    }

    private void stopTracking() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        if (isTracking) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            isTracking = false;
            timeElapsed = 0;
            updateTimer(timeElapsed);
        }

        // Navigate to PathDetailsActivity and pass data
        Intent intent = new Intent(requireContext(), PathActivity.class);
        intent.putExtra("pathPoints", pathPoints.toArray(new LatLng[0]));
        intent.putExtra("elapsedTime", timeElapsed);

    }
    private void updateTimer(long elapsedTime) {
        long minutes = elapsedTime / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        elapsedTimeTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        pathPoints.add(latLng);

        if (pathPoints.size() > 1) {
            Location lastLocation = new Location("");
            lastLocation.setLatitude(pathPoints.get(pathPoints.size() - 2).latitude);
            lastLocation.setLongitude(pathPoints.get(pathPoints.size() - 2).longitude);
            totalDistance += lastLocation.distanceTo(location);
        }

        if (runningPathPolyline == null) {
            runningPathPolyline = googleMap.addPolyline(new PolylineOptions()
                    .color(ContextCompat.getColor(requireContext(), R.color.path_color))
                    .width(10)
                    .addAll(pathPoints));
        } else {
            runningPathPolyline.setPoints(pathPoints);
        }
    }

    // Other methods of LocationListener (onStatusChanged, onProviderEnabled, onProviderDisabled) can be implemented if needed

    // Ensure to add onDestroy, onPause, onResume, and onLowMemory methods as you had in your original code
}
