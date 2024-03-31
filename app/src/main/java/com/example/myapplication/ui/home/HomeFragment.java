package com.example.myapplication.ui.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NetworkChangeReceiver;
import com.example.myapplication.PersonalData;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.notification.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements SensorEventListener {
    private TextView stepCountTextView;


    private int goal;


    private DatabaseReference myRef, myRef2;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;

    private int weight;
    private FragmentHomeBinding binding;
    private TextView caloriesT;
    private TextView GoalT;
    private TextView Burn, DISTfANCE;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepsCount = 0;
    private boolean isCounting = false;
    private String currentDate ="";
    private static final double AVERAGE_STEP_LENGTH = 0.7; // meters
    private static final double AVERAGE_WALKING_SPEED_KM_PER_HOUR = 5.0; // km/h

    private double strideLength; // Stride length in meters

    // Height of the user in centimeters
    private double userHeightCm = 170;

    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "StepCounterChannel";
    private Switch otherFragmentSwitch;

    private static final String TAG = "YourFragment";
    private NetworkChangeReceiver receiver;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        receiver = new NetworkChangeReceiver();
//        getActivity().registerReceiver(receiver, filter);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef2 = database.getReference("dataUser");
        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, root.getContext());
        caloriesT = root.findViewById(R.id.CaloriesT);
        GoalT = root.findViewById(R.id.GoalT);
        Burn = root.findViewById(R.id.Burn);
        DISTfANCE = root.findViewById(R.id.DISTfANCE);
//        DinerT = root.findViewById(R.id.DinerT);
        strideLength = 0.415 * (userHeightCm / 100);


        try {
            showHomePageDesign(root);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

        myRef2.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PersonalData value = dataSnapshot.getValue(PersonalData.class);
                if (value != null) {
                    weight = value.getWeight();
//                    userHeightCm = value.getHeight();
//                    strideLength = 0.415 * (userHeightCm / 100);

                } else {


                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users value = dataSnapshot.getValue(Users.class);
                if (value != null) {
                    caloriesT.setText("" + value.getCalories());
                    GoalT.setText("" + value.getGoalStep());
                    goal = value.getGoalStep();

                } else {
                    caloriesT.setText("No data");
                }
            }


            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        stepCountTextView = root.findViewById(R.id.stepCountTextView1);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor == null) {
                Toast.makeText(requireActivity(), "Step counter sensor not available", Toast.LENGTH_SHORT).show();
            } else {
                startCounting();
            }
        }


        return root;


    }


    private double calculateWalkingDuration(int stepsCount) {
        // Implementation of walking duration calculation method
        double distanceMeters = stepsCount * 0.7; // Assuming average step length of 0.7 meters
        double walkingSpeedMetersPerMinute = 5000.0 * 1000 / 60; // Assuming average walking speed of 5 km/h
        return distanceMeters / walkingSpeedMetersPerMinute; // Walking duration in minutes
    }

    private double calculateCaloriesBurned(int stepsCount) {
        // Implementation of calories burned calculation method
        double walkingDurationMinutes = calculateWalkingDuration(stepsCount);
        double weightKg = 70.0; // Assuming user's weight is 70 kg
        double MET_WALKING = 3.5; // MET value for walking
        return 1000*(MET_WALKING * weightKg * walkingDurationMinutes / 60.0); // Calories burned during walking
    }



    private void showHomePageDesign(View view) throws IllegalAccessException, java.lang.InstantiationException {
        stepCountTextView = view.findViewById(R.id.stepCountTextView1);


        Toast.makeText(view.getContext(), "Works!", Toast.LENGTH_SHORT).show();


    }



    private void startCounting() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            isCounting = true;
            stepsCount = 0;
            stepCountTextView.setText(String.valueOf(stepsCount));
            if (stepSensor != null) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(requireActivity(), "Step counter sensor not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepsCount = (int) event.values[0];
        stepCountTextView.setText(String.valueOf(stepsCount));

        double caloriesBurned = calculateCaloriesBurned(stepsCount); // Calculate calories burned during walking
        String formattedCaloriesBurned = String.format("%.2f", caloriesBurned);
        Burn.setText(formattedCaloriesBurned);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(new Date());

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean switchState = sharedPreferences.getBoolean("notification_switch_state", false);
        Log.d(TAG, "Switch state: " + switchState);

        if (switchState == true) {
            Log.d(TAG, "step count: " + stepsCount);
            showNotification(stepsCount, goal);

        } else {
            Log.d("YourFragment", "OtherFragment not found");
        }


        Log.d(TAG, "Switch state: " + switchState);



        String newDate = "";
        // New day, save step count to Firebase
        newDate = dateFormat.format(new Date());
        saveStepCountToFirebase(currentDate, stepsCount);
        if (!newDate.equals(currentDate)) {
            stepCountTextView.setText("0");
            currentDate = newDate;
        }

        double distance = stepsCount * strideLength;
        DISTfANCE.setText(String.format(Locale.getDefault(), "%.2f km", distance / 1000)); // Display distance in kilometers


    }


    private int showNotification(int stepsCount, int goal) {
        if(stepsCount>=goal) {
            Intent notificationIntent = new Intent(getContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("Step Count Goal Reached!")
                    .setContentText("Congratulations! You've reached your step count goal.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
        return 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in step counting
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCounting();
        binding = null;
    }

    private void stopCounting() {
        isCounting = false;
        if (stepSensor != null) {
            sensorManager.unregisterListener(this, stepSensor);
        }
    }

    private void saveStepCountToFirebase(String date, int stepCount) {
        if (date != null) {
            // Implement Firebase logic to save step count for the given date
            FireBaseHandler.stepsDate(date,stepCount);

        } else {
            // Handle the case where date is null
            // You may choose to log an error or take other appropriate action
            Log.e("Firebase", "Date is null in saveStepCountToFirebase");
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        // Register the broadcast receiver here
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the broadcast receiver here
        getActivity().unregisterReceiver(receiver);
    }
}

