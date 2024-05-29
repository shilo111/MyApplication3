package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FireBaseHandler f = new FireBaseHandler(FirebaseAuth.getInstance(),this);
    TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signIn = findViewById(R.id.reg_sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
                finish();
            }
        });





    }


    public void reg(View view)
    {
        EditText emailInput = findViewById(R.id.regEmail);
        EditText passInput = findViewById(R.id.regPasssword);

        String email = String.valueOf(emailInput.getText());
        String password = String.valueOf(passInput.getText());


        if((email.isEmpty()|| password.isEmpty())) {
            Toast.makeText(this, "please fill out the full email and password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            f.register(email, password);
        }

    }
}