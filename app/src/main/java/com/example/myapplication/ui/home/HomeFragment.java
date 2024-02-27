package com.example.myapplication.ui.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.HomePage;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements SensorEventListener {
    private TextView stepCountTextView;


    private int goal;


    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;


    private FragmentHomeBinding binding;
    private TextView caloriesT;
    private TextView GoalT;
    private TextView DinerT;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepsCount = 0;
    private boolean isCounting = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, root.getContext());
        caloriesT = root.findViewById(R.id.CaloriesT);
        GoalT = root.findViewById(R.id.GoalT);
//        DinerT = root.findViewById(R.id.DinerT);

        try {
            showHomePageDesign(root);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
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
}

