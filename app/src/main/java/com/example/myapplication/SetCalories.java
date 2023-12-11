package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
    private FireBaseHandler fireBaseHandler;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_calories);
        Button btn = findViewById(R.id.Set);
        Setgoal = findViewById(R.id.Setgoal);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");


        fireBaseHandler = new FireBaseHandler(auth, Setgoal.getContext());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int foodItem = Integer.parseInt(Setgoal.getText().toString());


                if (!Setgoal.getText().toString().isEmpty()) {


                    fireBaseHandler.getUserDetails(myRef, user -> {

                        myRef.child(auth.getCurrentUser().getUid()).child("GoalStep").setValue(foodItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SetCalories.this, "Well done!!!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SetCalories.this, "Please try again :(", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
            }
        });


    }
}

