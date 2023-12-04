package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        f.register(email, password);
    }
}