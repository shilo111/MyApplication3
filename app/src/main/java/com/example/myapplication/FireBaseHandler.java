package com.example.myapplication;


import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseHandler {
    private static FirebaseDatabase database;
    private static DatabaseReference myRef;

    private static FirebaseAuth auth;
    private static Context context;



    public FireBaseHandler(FirebaseAuth auth, Context context)
    {
        this.auth = auth;
        this.context = context;
    }

    public void signIn(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    newUser();


//                        Toast.makeText(context, "sign in successfully!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context.getApplicationContext(), HomePage.class);
//                        context.startActivity(intent);

                }
                else
                    Toast.makeText(context, "sign in failed!", Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                    context.startActivity(intent);

                }
                else{
                    Toast.makeText(context, "Registered failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public FirebaseAuth getAuth(){return auth;}
    public  void setAuth(FirebaseAuth auth){this.auth = auth;}

    public Users getUserDetails(DatabaseReference myRef, Listener<Users> listener) {
        final Users[] user = new Users[1];
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users value = dataSnapshot.getValue(Users.class);
                listener.onListen(value);
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());}
        });

        return user[0];
    }

    public static void newUser()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        myRef.child("dataUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for(DataSnapshot snapshot : task.getResult().getChildren())
                    {
                        if(snapshot.child(auth.getCurrentUser().getUid()).exists())
                        {
                            Intent intent = new Intent(context.getApplicationContext(), HomePage.class);
                            context.startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(context.getApplicationContext(), FirstTime.class);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });


    }

    public static void setNewData(double height,int weight,int bmi, int bodyFat, double age){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).setValue(height).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).setValue(weight).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).setValue(bmi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).setValue(bodyFat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }
}