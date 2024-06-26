package com.example.myapplication.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MySharedPreferences;
import com.example.myapplication.MySharedPreferencesINT;
import com.example.myapplication.MySharedPreferencesSteps;
import com.example.myapplication.NetworkChangeReceiver;
import com.example.myapplication.PersonalData;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModelStepsFire;
import com.example.myapplication.UserPersonalManager;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements SensorEventListener {

    // Views
    private TextView stepCountTextView;
    private TextView caloriesT;
    private TextView GoalT;
    private TextView Burn;
    private TextView DISTfANCE;

    // Firebase
    private DatabaseReference myRef, myRef2, myRef3;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;

    // Sensor
    private SensorManager sensorManager;
    private Sensor stepSensor;

    // Step counting variables
    private int stepsCount = 0;
    private boolean isCounting = false;
    private int goal; // User's step count goal
    private double strideLength = 0.75; // Stride length in meters
    private static final double AVERAGE_STEP_LENGTH = 0.7; // meters
    private static final double AVERAGE_WALKING_SPEED_KM_PER_HOUR = 5.0; // km/h

    // Constants
    private static final String CHANNEL_ID = "step_goal_channel";
    private static final int NOTIFICATION_ID = 1;

    // User data
    private int weight;
    private double userHeightCm = 170; // Height of the user in centimeters

    // Other UI elements
    private Switch otherFragmentSwitch;
    private static final String TAG = "YourFragment"; // Log tag for debugging

    // Network state change receiver
    private NetworkChangeReceiver receiver;
    private int stepsCount2 = 0;

    // Date format for tracking steps per day
    private String currentDate = "";

    // Fragment binding
    private FragmentHomeBinding binding;

    // ViewModel for shared steps
    private SharedViewModelStepsFire viewModel;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "DayCheckerPrefs";
    private static final String LAST_CHECKED_DAY_KEY = "lastCheckedDay";

    public HomeFragment() {
    }

    // Initialize the fragment's view
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize UI components
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeViews(root);

        // Initialize Firebase
        initializeFirebase();

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Start counting steps
        startCounting();

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModelStepsFire.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get the current date
        Date currentDate2 = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = formatter.format(currentDate2);

        // Check if the boolean value has been set before
        if (!MySharedPreferences.isBooleanSet(getContext())) {
            MySharedPreferences.saveBoolean(getContext(), true);
        }

        if (user != null) {
            String email = user.getEmail();
            String userId = email;
            boolean valueSet = MySharedPreferencesSteps.isValueSet(getContext(), userId, formattedDate);
            if (!valueSet) {
                MySharedPreferencesSteps.saveUserData(getContext(), userId, formattedDate, 0);
            }
        }

        // Check if the day has changed
        String email3 = user.getEmail();
        if (hasDayChanged()) {
            // Do something when the day changes
            Log.e(TAG, "day changed33");
            MySharedPreferencesSteps.saveUserData(getContext(), email3, formattedDate, stepsCount);
            Log.e(TAG, "savedValue22: " + stepsCount);
            MySharedPreferences.saveBoolean(getContext(), true);
        } else {
            stepCountTextView.setText("no data");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(new Date());
        myRef3 = database.getReference("dataSteps");

        myRef2.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PersonalData value = dataSnapshot.getValue(PersonalData.class);
                if (value != null) {
                    weight = value.getWeight();
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
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
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            stepCountTextView.setText("0");
        }
        return root;
    }

    // Initialize UI components
    private void initializeViews(View root) {
        stepCountTextView = root.findViewById(R.id.stepCountTextView1);
        caloriesT = root.findViewById(R.id.CaloriesT);
        GoalT = root.findViewById(R.id.GoalT);
        Burn = root.findViewById(R.id.Burn);
        DISTfANCE = root.findViewById(R.id.DISTfANCE);
    }

    // Initialize Firebase components
    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef2 = database.getReference("dataUser");
        myRef3 = database.getReference("dataSteps");
        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, getContext());
    }

    // Start counting steps using the step counter sensor
    private void startCounting() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            isCounting = true;
            stepsCount = 0;
            stepCountTextView.setText(String.valueOf(stepsCount));
            registerStepSensor();
        }
    }

    // Register the step counter sensor listener
    private void registerStepSensor() {
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor == null) {
                Toast.makeText(requireActivity(), "Step counter sensor not available", Toast.LENGTH_SHORT).show();
            } else {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    private double calculateWalkingDuration(int stepsCount) {
        // Implementation of walking duration calculation method
        double distanceMeters = stepsCount * 0.7; // Assuming average step length of 0.7 meters
        double walkingSpeedMetersPerMinute = 5000.0 * 1000 / 60; // Assuming average walking speed of 5 km/h
        return distanceMeters / walkingSpeedMetersPerMinute; // Walking duration in minutes
    }

    private double calculateCaloriesBurned(int stepsCount) {
        // Implementation of calories burned calculation method
        String userId3 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserPersonalManager userPersonalManager = new UserPersonalManager(getContext(), userId3);
        double walkingDurationMinutes = calculateWalkingDuration(stepsCount);
        double weightKg = userPersonalManager.getWeight();
        double MET_WALKING = 3.5; // MET value for walking
        return 1000 * (MET_WALKING * weightKg * walkingDurationMinutes / 60.0); // Calories burned during walking
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepsCount = (int) event.values[0];
        stepCountTextView.setText(String.valueOf(stepsCount));

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean switchState = sharedPreferences.getBoolean("notification_switch_state", false);
        Log.d("Not", "Switch state: " + switchState);

        Date currentDate4 = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = formatter.format(currentDate4);

        boolean savedBooleanValue = MySharedPreferences.getBoolean(getContext());
        Log.e(TAG, "savedBooleanValue before if: " + savedBooleanValue);
        if (savedBooleanValue) {
            stepsCount2 = (int) event.values[0];
            MySharedPreferencesINT.saveInteger(getContext(), stepsCount2);
            MySharedPreferences.saveBoolean(getContext(), false);
            savedBooleanValue = MySharedPreferences.getBoolean(getContext());
            Log.e(TAG, "savedBooleanValue after if: " + savedBooleanValue);
            Log.e(TAG, "stepsCount2: " + stepsCount2);
        }
        Log.e(TAG, "stepsCount: " + (int) event.values[0]);
        int savedIntegerValue = MySharedPreferencesINT.getInteger(getContext());
        Log.e(TAG, "stepsCount2: " + savedIntegerValue);
        FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
        if (user2 != null) {
            String email = user2.getEmail();
            String userId = email;
            Map<String, Integer> userData = MySharedPreferencesSteps.getUserDataForDate(getContext(), userId, formattedDate);
            if (userData != null) {
                // Iterate over the dates and values for the user
                for (Map.Entry<String, Integer> entry : userData.entrySet()) {
                    String savedDate = entry.getKey();
                    int savedValue = entry.getValue();
                    Log.e(TAG, "savedDate: " + savedDate);
                    Log.e(TAG, "savedValue: " + savedValue);
                    if ((int) event.values[0] - savedIntegerValue < 0) {
                        MySharedPreferencesINT.saveInteger(getContext(), 0);
                    }
                    stepsCount = ((int) event.values[0] - savedIntegerValue + savedValue);
                    viewModel.setValue(stepsCount);

                    stepCountTextView.setText(String.valueOf(stepsCount));
                }
            }
        }

        if (switchState) {
            Log.d("Not", "11111111111step count: " + stepsCount);
            Log.d("Not", "11111111111step goal: " + goal);
            showNotification(stepsCount, goal);
        }

        double caloriesBurned = calculateCaloriesBurned(stepsCount);
        String formattedCaloriesBurned = String.format("%.2f", caloriesBurned);
        Burn.setText(formattedCaloriesBurned);

        double distance = stepsCount * strideLength;
        DISTfANCE.setText(String.format(Locale.getDefault(), "%.2f km", distance / 1000));

        // Save step count to Firebase
        saveStepCountToFirebase(currentDate, stepsCount);
    }

    // Show notification if step count goal is reached
    private void showNotification(int stepsCount, int goal) {
        if (stepsCount >= goal) {
            Context context = requireContext(); // or getContext()

            // Create the notification channel if necessary (Android 8.0+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Step Goal Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("Notifications for step count goal achievement");
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("Step Count Goal Reached!")
                    .setContentText("Congratulations! You've reached your step count goal.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    }

    // Save step count to Firebase
    private void saveStepCountToFirebase(String date, int stepCount) {
        if (date != null) {
            fireBaseHandler.stepsDate(date, stepCount);
        } else {
            Log.e("Firebase", "Date is null in saveStepCountToFirebase");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in step counting
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCounting();
        binding = null;
    }

    private void stopCounting() {
        isCounting = false;
        if (sensorManager != null && stepSensor != null) {
            sensorManager.unregisterListener(this, stepSensor);
        }
    }

    public void DayChecker(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean hasDayChanged() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        int lastCheckedDay = sharedPreferences.getInt(LAST_CHECKED_DAY_KEY, -1);

        if (currentDay != lastCheckedDay) {
            // Update last checked day in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(LAST_CHECKED_DAY_KEY, currentDay);
            editor.apply();
            return true; // Day has changed
        } else {
            return false; // Day has not changed
        }
    }
}

