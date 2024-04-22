package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private UserPersonalManager userPersonalManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_calories);
        Button btn = findViewById(R.id.Set);
        Setgoal = findViewById(R.id.Setgoal);


        // Initialize UserPersonalManager with the appropriate context and user identifier
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPersonalManager = new UserPersonalManager(this, userId);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int foodItem = Integer.parseInt(Setgoal.getText().toString());


                if (!Setgoal.getText().toString().isEmpty()) {
                    userPersonalManager.setGoalStep(foodItem);
                    Toast.makeText(SetCalories.this, "Well done!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetCalories.this, HomePage.class);
                    startActivity(intent);
                }
            }
        });


    }
}

