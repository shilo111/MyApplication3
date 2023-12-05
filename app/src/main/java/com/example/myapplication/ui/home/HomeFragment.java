package com.example.myapplication.ui.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
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
    private EditText editgoal;
    private  int progress;
    private SensorManager sensorManager;
    private TextView stepCountTextView;
    private TextView goalTextView;
    private ProgressBar progressBar;
    private int stepCount = 0;
    private int goal;
    private EditText goalEditText;
    private Button button;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;

    private static final int REQUEST_SENSOR_PERMISSION = 1;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, root.getContext());

        try {
            showHomePageDesign(root);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
/*
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users value = dataSnapshot.getValue(Users.class);
                if (value != null) {
                    goalTextView.setText("Total Calories: " + value.getSteps());

                } else {

                    goalTextView.setText("Total Calories: " + 0);
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());}
        });

 */



        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showHomePageDesign(View view) throws IllegalAccessException, java.lang.InstantiationException {
        stepCountTextView = view.findViewById(R.id.stepCountTextView1);
        goalTextView = view.findViewById(R.id.goalTextView);
        progressBar = view.findViewById(R.id.progressBar);
        goalEditText = view.findViewById(R.id.goalEditText);


        Toast.makeText(view.getContext(), "Works!", Toast.LENGTH_SHORT).show();

        // Initialize the sensor manager
        sensorManager = (SensorManager) view.getContext().getSystemService(view.getContext().SENSOR_SERVICE);


        // Check for permission to use the step counter sensor
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_SENSOR_PERMISSION);
        } else {
            registerStepCounterSensor();
        }







//      Set up listener for goal input
        goalEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Update goal when the user finishes entering a new goal
                updateGoalFromEditText();
                return true;
           }
         return false;
        });


        // Check for permission to use the step counter sensor





        Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progress = 0;
                stepCount = 0;
                setStepCount(stepCount);
                stepCountTextView.setText("Step Count: " + stepCount);
                progressBar.setProgress(progress);
            }
        });



        String Goal = goalEditText.getText().toString();




    }
    private void updateGoalFromEditText() {
        String goalString = goalEditText.getText().toString();

        if (!TextUtils.isEmpty(goalString)) {
            goal = Integer.parseInt(goalString);
            updateGoalTextView();
           // int finalGoal = goal;
            /*
            fireBaseHandler.getUserDetails(myRef, user -> {

                myRef.child(auth.getCurrentUser().getUid()).child("steps").setValue(finalGoal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity().getApplicationContext(), "Well done!!!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please try again :(", Toast.LENGTH_SHORT).show();
                    }
                });
            });

             */
        }
    }

    private void updateGoalTextView() {
        goalTextView.setText("Goal: " + goal);
        updateProgressBar();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SENSOR_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            registerStepCounterSensor();
        }
    }

    private void registerStepCounterSensor() {
        Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            updateStepCountTextView();
            updateProgressBar();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for step counter sensor
    }

    private void updateStepCountTextView() {
        stepCountTextView.setText("Step Count: " + stepCount);
    }

    private void updateProgressBar() {
        progress = (int) ((stepCount * 100.0) / goal);
        progressBar.setProgress(progress);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerStepCounterSensor();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }
    public int setStepCount(int step)
    {
        return (this.stepCount = stepCount);

    }



}