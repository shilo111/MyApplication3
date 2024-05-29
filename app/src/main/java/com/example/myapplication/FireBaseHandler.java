package com.example.myapplication;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth; // Firebase Authentication
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference; // Firebase Realtime Database reference
import com.google.firebase.database.FirebaseDatabase; // Firebase Realtime Database
import com.google.firebase.database.ValueEventListener;

public class FireBaseHandler {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance(); // Initialize FirebaseDatabase instance
    private static DatabaseReference myRef = database.getReference("dataSteps"); // Reference to the "dataSteps" node
    private static DatabaseReference myRef2 = database.getReference(); // General database reference
    private static FirebaseAuth auth; // Firebase Authentication instance
    private static Context context; // Context for various operations

    public FireBaseHandler(FirebaseAuth auth, Context context) {
        this.auth = auth; // Initialize FirebaseAuth
        this.context = context; // Initialize context
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get user ID
                    UserPersonalManager userPersonalManager = new UserPersonalManager(context, userId);
                    double height = userPersonalManager.getHeight();
                    int weight = userPersonalManager.getWeight();
                    double age = userPersonalManager.getAge();

                    if (height > 0 || weight > 0 || age > 0) {
                        // If user personal data exists, navigate to HomePage
                        Intent intent = new Intent(context.getApplicationContext(), HomePage.class);
                        context.startActivity(intent);
                    } else {
                        // If user personal data does not exist, navigate to FirstTime
                        Intent intent = new Intent(context.getApplicationContext(), FirstTime.class);
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "sign in failed!", Toast.LENGTH_SHORT).show(); // Show sign-in failed message
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show(); // Show error message
            }
        });
    }

    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show(); // Show success message
                    Intent intent = new Intent(context.getApplicationContext(), MainActivity.class); // Navigate to MainActivity
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Registration failed!", Toast.LENGTH_SHORT).show(); // Show registration failed message
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show(); // Show error message
            }
        });
    }

    public FirebaseAuth getAuth() {
        return auth; // Return FirebaseAuth instance
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth; // Set FirebaseAuth instance
    }

    public Users getUserDetails(DatabaseReference myRef, Listener<Users> listener) {
        final Users[] user = new Users[1]; // Array to store user details
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users value = dataSnapshot.getValue(Users.class); // Retrieve user data
                listener.onListen(value); // Notify listener with user data
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException()); // Log error message
            }
        });

        return user[0]; // Return user details
    }

    public static void newUser() {
        // Implement method to handle new user creation
    }

    public static void stepsDate(String date, int stepCount) {
        myRef.child(auth.getCurrentUser().getUid()).child(date).setValue(stepCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful step count update
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("good", "Failed to read value.90989"); // Log error message
            }
        });
    }

    public static void setNewData(double height, int weight, int bmi, int bodyFat, double age) {
        String userId = auth.getCurrentUser().getUid(); // Get current user ID
        myRef.child("dataUser").child(userId).child("height").setValue(height).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful height update
            }
        });
        myRef.child("dataUser").child(userId).child("weight").setValue(weight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful weight update
            }
        });
        myRef.child("dataUser").child(userId).child("bmi").setValue(bmi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful BMI update
            }
        });
        myRef.child("dataUser").child(userId).child("bodyFat").setValue(bodyFat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful body fat update
            }
        });
        myRef.child("dataUser").child(userId).child("age").setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful age update
            }
        });

        Toast.makeText(context, "You can see your bmi just go to settings and personal!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context.getApplicationContext(), MainSlider.class); // Navigate to MainSlider
        context.startActivity(intent);
    }

    public static void saveStepGoal(int step) {
        myRef2.child("users").child(auth.getCurrentUser().getUid()).child("GoalStep").setValue(step).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful step goal update
            }
        });
    }

    public static void saveCalories(int calories) {
        myRef2.child("users").child(auth.getCurrentUser().getUid()).child("calories").setValue(calories).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Handle successful calories update
            }
        });
    }
}
