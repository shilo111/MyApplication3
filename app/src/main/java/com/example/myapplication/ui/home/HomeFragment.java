package com.example.myapplication.ui.home;



import android.Manifest;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DayChangeReceiver;
import com.example.myapplication.DayChecker;
import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MySharedPreferences;
import com.example.myapplication.MySharedPreferencesINT;
import com.example.myapplication.MySharedPreferencesSteps;
import com.example.myapplication.NetworkChangeReceiver;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModelStepsFire;
import com.example.myapplication.UserPersonalManager;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements SensorEventListener {
    private TextView stepCountTextView;
    private DatabaseReference myRef3, myRef;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;


    private FragmentHomeBinding binding;
    private TextView caloriesT;
    private TextView GoalT;
    private TextView Burn, DISTfANCE;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepsCount = 0;
    private int stepsCount2 = 0;
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
    private DayChecker dayChecker;
    private static final String TAG = "YourFragment";
    private NetworkChangeReceiver receiver;

    private DayChangeReceiver dayChangeReceiver;
    private SharedViewModelStepsFire viewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        receiver = new NetworkChangeReceiver();
//        getActivity().registerReceiver(receiver, filter);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();


        myRef3 = database.getReference("FirstRunOfDay");
        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, root.getContext());
        caloriesT = root.findViewById(R.id.CaloriesT);
        GoalT = root.findViewById(R.id.GoalT);
        Burn = root.findViewById(R.id.Burn);
        DISTfANCE = root.findViewById(R.id.DISTfANCE);
//        DinerT = root.findViewById(R.id.DinerT);
        strideLength = 0.415 * (userHeightCm / 100);
        myRef = database.getReference("dataSteps");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModelStepsFire.class);
        dayChecker = new DayChecker(getContext());

        // Get the current date
        Date currentDate = new Date();

// Define a date formatter for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

// Format the current date using the formatter
        String formattedDate = formatter.format(currentDate);


        // Check if the boolean value has been set before
        if (!MySharedPreferences.isBooleanSet(getContext())) {
            // Save a boolean value as true for the first time
            MySharedPreferences.saveBoolean(getContext(), true);
        }

        if (user != null) {
            String email = user.getEmail();
            String userId = email;
            boolean valueSet = MySharedPreferencesSteps.isValueSet(getContext(), userId, formattedDate);
            if (valueSet) {
                // The value has been set for the user and date

            } else {
                MySharedPreferencesSteps.saveUserData(getContext(), userId, formattedDate, 0);
            }

            // Check if the day has changed
            if (dayChecker.hasDayChanged()) {
                // Do something when the day changes
                Log.e(TAG, "day changed");
              MySharedPreferencesSteps.saveUserData(getContext(), email, formattedDate, stepsCount);

             MySharedPreferences.saveBoolean(getContext(), true);

            }

        }

        try {
            showHomePageDesign(root);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

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
        myRef.child(auth.getCurrentUser().getUid()).child(formattedDate).setValue(stepsCount);

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
        String userId3 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserPersonalManager userPersonalManager = new UserPersonalManager(getContext(), userId3);
        double walkingDurationMinutes = calculateWalkingDuration(stepsCount);
        double weightKg = userPersonalManager.getWeight();
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
            //stepCountTextView.setText(String.valueOf(stepsCount));
            if (stepSensor != null) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(requireActivity(), "Step counter sensor not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String userId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserPersonalManager userPersonalManager = new UserPersonalManager(getContext(), userId2);
        int stepGoal = userPersonalManager.getGoalStep();
        GoalT.setText(String.valueOf(stepGoal));
        caloriesT.setText(String.valueOf(userPersonalManager.getCalories()));



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String email2 = user.getEmail();

        Date currentDate = new Date();

// Define a date formatter for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

// Format the current date using the formatter
        String formattedDate = formatter.format(currentDate);





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
            Map<String, Integer> userData = MySharedPreferencesSteps.getUserDataForDate(getContext(), userId,formattedDate);
            if (userData != null) {
                // Iterate over the dates and values for the user
                for (Map.Entry<String, Integer> entry : userData.entrySet()) {
                    String savedDate = entry.getKey();
                    int savedValue = entry.getValue();
                    Log.e(TAG, "savedDate: " + savedDate);
                    Log.e(TAG, "savedValue: " + savedValue);
                    if((int) event.values[0] - savedIntegerValue < 0)
                    {
                        MySharedPreferencesINT.saveInteger(getContext(), 0);
                    }
                    stepsCount = ((int) event.values[0] - savedIntegerValue + savedValue);
                    viewModel.setValue(stepsCount);

                    stepCountTextView.setText(String.valueOf(stepsCount));
                }
                // Check if the day has changed
                if (dayChecker.hasDayChanged()) {
                    // Do something when the day changes
                    Log.e(TAG, "day changed33");
                    MySharedPreferencesSteps.saveUserData(getContext(), email, formattedDate, stepsCount);
                    Log.e(TAG, "savedValue22: " + stepsCount);
                    MySharedPreferences.saveBoolean(getContext(), true);

                }
            } else {
                stepCountTextView.setText("no data");
            }
        }



        double caloriesBurned = calculateCaloriesBurned(stepsCount); // Calculate calories burned during walking
        String formattedCaloriesBurned = String.format("%.2f", caloriesBurned);
        Burn.setText(formattedCaloriesBurned);
        //FireBaseHandler.stepsDate(formattedDate,stepsCount);
        myRef.child(auth.getCurrentUser().getUid()).child(formattedDate).setValue(stepsCount);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        currentDate = dateFormat.format(new Date());

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean switchState = sharedPreferences.getBoolean("notification_switch_state", false);
        Log.d(TAG, "Switch state: " + switchState);

        if (switchState == true) {
            Log.d(TAG, "step count: " + stepsCount);
            showNotification(stepsCount, stepGoal);

        } else {
            Log.d("YourFragment", "OtherFragment not found");
        }


        Log.d(TAG, "Switch state: " + switchState);



        double distance = stepsCount * strideLength;
        DISTfANCE.setText(String.format(Locale.getDefault(), "%.2f km", distance / 1000)); // Display distance in kilometers


    }

//לבדוק לא עובד
    private int showNotification(int stepsCount, int goal) {
        if(stepsCount>=goal) {
            Intent notificationIntent = new Intent(getContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Check for date change when the fragment is resumed
//        checkForDateChange();
//    }

    private void checkForDateChange() {

        SharedPreferences sharedPreferences = MySharedPreferencesSteps.getSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Date currentDate = new Date();

// Define a date formatter for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

// Format the current date using the formatter
        String formattedDate2 = formatter.format(currentDate);

        FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
        if (user2 != null) {
            String email = user2.getEmail();
            String userId = email;
            Map<String, Integer> userData = MySharedPreferencesSteps.getUserDataForDate(getContext(), userId, formattedDate2);
            if (userData != null) {
                // Iterate over the dates and values for the user
                for (Map.Entry<String, Integer> entry : userData.entrySet()) {
                    String savedDate = entry.getKey();
                    int savedValue = entry.getValue();
                    Log.d("getUserDataForDate", "Date parameter value2: " + savedDate);
                    if (!savedDate.equals(formattedDate2)) {
                        // Perform action for day change
                        // For example, you can show a notification or update some data
                        // Here, we'll just log the day change

                        // Save boolean value to SharedPreferences using MySharedPreferencesSteps
                        MySharedPreferences.saveBoolean(getContext(), true);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        String formattedDate = dateFormat.format(currentDate);
                        Log.d("getUserDataForDate", "Date parameter value new: " + formattedDate);



                    }
                }
            } else {
                stepCountTextView.setText("no data");
            }
        }
        // Check if day has changed

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
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Unregister the receiver to avoid memory leaks
//        getContext().unregisterReceiver(dayChangeReceiver);
//    }
}

