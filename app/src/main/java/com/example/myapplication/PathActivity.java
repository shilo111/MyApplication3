package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class PathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        // Retrieve path data and elapsed time from intent
        // Inside onCreate() method
        Intent intent = getIntent();
        ArrayList<LatLng> pathPoints = intent.getParcelableArrayListExtra("pathPoints");
        long elapsedTime = intent.getLongExtra("elapsedTime", 0);



        // Display path and timer in the activity UI
        displayPath(pathPoints);
        displayTimer(elapsedTime);


// Inside onCreate() method of PathActivity
        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (pathPoints != null && !pathPoints.isEmpty()) { // Check if pathPoints is not null or empty
                    // Add polyline to the map
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(pathPoints)
                            .color(Color.RED)
                            .width(5);
                    googleMap.addPolyline(polylineOptions);

                    // Optionally, you can adjust the camera position to fit the polyline
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng point : pathPoints) {
                        builder.include(point);
                    }
                    LatLngBounds bounds = builder.build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                } else {
                    Toast.makeText(PathActivity.this, "No path points available", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    private void displayPath(ArrayList<LatLng> pathPoints) {
        // Implement code to display path on map (e.g., using Google Maps)
    }

    private void displayTimer(long elapsedTime) {
        TextView timerTextView = findViewById(R.id.timerTextView);
        long minutes = elapsedTime / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
