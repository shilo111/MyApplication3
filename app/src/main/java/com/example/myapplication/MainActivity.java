package com.example.myapplication;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private TextView signup;
    private FirebaseAuth auth;
    private FireBaseHandler f;

    private NetworkChangeReceiver receiver;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);


        signup=findViewById(R.id.sign_up);

        auth = FirebaseAuth.getInstance();
        f = new FireBaseHandler(auth, this);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
                finish();
            }
        });



    }

    public void onClick(View view) {

        TextInputEditText emailInput = findViewById(R.id.email);
        TextInputEditText passInput = findViewById(R.id.password);

        String email = String.valueOf(emailInput.getText());
        String password = String.valueOf(passInput.getText());

        if((email.isEmpty()|| password.isEmpty())) {
            Toast.makeText(this, "please fill out the full email and passwords", Toast.LENGTH_SHORT).show();
        }
        else
        {
            f.signIn(email, password);
        }


    }

   @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the network change receiver
        unregisterReceiver(receiver);
    }




}