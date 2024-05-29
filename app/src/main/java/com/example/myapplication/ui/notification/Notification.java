package com.example.myapplication.ui.notification;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import com.example.myapplication.Datasteps;
import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification extends Fragment {

    private FragmentNotificationBinding binding;
    private DatabaseReference myRef, myRef2;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler; // Assuming this is a custom handler for Firebase operations
    private FirebaseAuth auth;
    private int stepGoal;
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "StepCounterChannel";
    private String currentDate = "";
    private int stepCount; // Assume this is retrieved from Firebase
    private Switch notificationSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SharedPreferences for saving notification switch state
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment using data binding
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef2 = database.getReference("dataSteps");
        auth = FirebaseAuth.getInstance();



        // Initialize notification switch and set its state based on saved preferences
        notificationSwitch = root.findViewById(R.id.notificationSwitch);
        boolean switchState = sharedPreferences.getBoolean("notification_switch_state", false);
        notificationSwitch.setChecked(switchState);

        // Set up listener for notification switch state changes
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notification_switch_state", isChecked);
                editor.apply();
            }
        });

        return root; // Return the root view
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NotificationViewModel mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        // TODO: Use the ViewModel
    }






}