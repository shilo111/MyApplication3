package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

public class SetCalories extends AppCompatActivity {
    private EditText Setgoal;

    private EditText editTextFood, editTextCalories;
    private TextView textViewTotalCalories;
    private int totalCalories = 0;
    private UserPersonalManager userPersonalManager;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_calories);
        Button btn = findViewById(R.id.Set);
        Setgoal = findViewById(R.id.Setgoal);
textViewTotalCalories = findViewById(R.id.textViewTotalCalories);

        // Initialize UserPersonalManager with the appropriate context and user identifier dddd
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPersonalManager = new UserPersonalManager(this, userId);

        textViewTotalCalories.setText("Total calories: " + userPersonalManager.getCalories());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int foodItem = Integer.parseInt(Setgoal.getText().toString());


                if (!Setgoal.getText().toString().isEmpty()) {
                    FireBaseHandler.saveStepGoal(foodItem);
                    userPersonalManager.setGoalStep(foodItem);
                    Toast.makeText(SetCalories.this, "Well done!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetCalories.this, HomePage.class);
                    startActivity(intent);
                }
            }
        });
        Button addButton = findViewById(R.id.buttonAdd);
        editTextFood = findViewById(R.id.editTextFood);
        editTextCalories = findViewById(R.id.editTextCalories);


        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String foodItem = editTextFood.getText().toString();
                String caloriesStr = editTextCalories.getText().toString();

                if (foodItem.isEmpty() || caloriesStr.isEmpty()) {
                    Toast.makeText(SetCalories.this, "Please enter both food name and calories", Toast.LENGTH_SHORT).show();
                    return;
                }
                int calories = Integer.parseInt(caloriesStr);
                totalCalories = userPersonalManager.getCalories();
                totalCalories += calories;
                FireBaseHandler.saveClories(totalCalories);
                userPersonalManager.setCalories(totalCalories);
                textViewTotalCalories.setText("Total calories: " + userPersonalManager.getCalories());
                // Clear the input fields
                editTextFood.getText().clear();
                editTextCalories.getText().clear();
                Intent intent = new Intent(SetCalories.this, HomePage.class);
                startActivity(intent);
            }



        });


    }
}

