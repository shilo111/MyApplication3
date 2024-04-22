package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class FirstTime extends AppCompatActivity {
EditText heightEditText,weightEditText,ageEditText,bodyFatEditText;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);


        // Attach text watchers
//        weightEditText.addTextChangedListener(textWatcher);
//        heightEditText.addTextChangedListener(textWatcher);


        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        ageEditText = findViewById(R.id.ageEditText);

        bodyFatEditText = findViewById(R.id.bodyFatEditText);
        button = findViewById(R.id.saveButton);

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                validateNumberWeight();
                calculateBMI();


            }
        });
        heightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateNumberHeight();
                }
            }
        });
        bodyFatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateNumberbodyFat();
                }
            }
        });
        ageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateNumberAge();
                }
            }
        });


    }
    private void validateNumberHeight() {
        String input = heightEditText.getText().toString().trim();
        if (input.isEmpty()) {
            // Handle empty input
            Toast.makeText(this, "Please enter a height", Toast.LENGTH_SHORT).show();
            return;
        }

        int number = Integer.parseInt(input);
        if (number < 160 || number > 210) {
            // Handle invalid input
            Toast.makeText(this, "Please enter a height between 160 and 210 cm", Toast.LENGTH_SHORT).show();
            // Clear the input field
            heightEditText.getText().clear();
            return;
        }

        // Input is valid, proceed with desired action
        // For example, you can store the number or perform further processing
    }

    private void validateNumberbodyFat() {
        String input = bodyFatEditText.getText().toString().trim();
        if (input.isEmpty()) {
            // Handle empty input
            Toast.makeText(this, "Please enter a body-Fat", Toast.LENGTH_SHORT).show();
            return;
        }

        int number = Integer.parseInt(input);
        if (number < 10 || number > 40) {
            // Handle invalid input
            Toast.makeText(this, "Please enter a number between 10 and 40", Toast.LENGTH_SHORT).show();
            // Clear the input field
            bodyFatEditText.getText().clear();
            return;
        }

        // Input is valid, proceed with desired action
        // For example, you can store the number or perform further processing
    }
    private void validateNumberAge() {
        String input = ageEditText.getText().toString().trim();
        if (input.isEmpty()) {
            // Handle empty input
            Toast.makeText(this, "Please enter a age", Toast.LENGTH_SHORT).show();
            return;
        }

        int number = Integer.parseInt(input);
        if (number < 18) {
            // Handle invalid input
            Toast.makeText(this, "Please enter a age 18+", Toast.LENGTH_SHORT).show();
            // Clear the input field
            ageEditText.getText().clear();
            return;
        }

        // Input is valid, proceed with desired action
        // For example, you can store the number or perform further processing
    }
    private void validateNumberWeight() {
        String input = weightEditText.getText().toString().trim();
        if (input.isEmpty()) {
            // Handle empty input
            Toast.makeText(this, "Please enter a weight", Toast.LENGTH_SHORT).show();
            return;
        }

        int number = Integer.parseInt(input);
        if (number < 50 || number > 110) {
            // Handle invalid input
            Toast.makeText(this, "Please enter a weight between 50 and 110 KL", Toast.LENGTH_SHORT).show();
            // Clear the input field
            weightEditText.getText().clear();
            return;
        }

        // Input is valid, proceed with desired action
        // For example, you can store the number or perform further processing
    }

   private void calculateBMI() {

       TextInputEditText ageInput = findViewById(R.id.ageEditText);
       TextInputEditText bodeFatInput = findViewById(R.id.bodyFatEditText);

       String age = String.valueOf(ageInput.getText());
       String bodeFat = String.valueOf(bodeFatInput.getText());

// Get weight and height from EditText
       TextInputEditText weightStrInput = findViewById(R.id.weightEditText);
       TextInputEditText heightStrInput = findViewById(R.id.heightEditText);

       String weightStr = String.valueOf(weightStrInput.getText());
       String heightStr = String.valueOf(heightStrInput.getText());


        // Get weight and height from EditText
        float height = 0;
        float weight = 0;
        float bmi = 0;
        if (weightStr.isEmpty() || heightStr.isEmpty() || age.isEmpty()|| bodeFat.isEmpty()) {
            // Clear BMI EditText if weight or height is empty
            Toast.makeText(this, "Please fill out the information", Toast.LENGTH_SHORT).show();

        }
        else {

            // Convert string values to float
            weight = Float.parseFloat(weightStr);
            height = Float.parseFloat(heightStr) / 100;

            // Calculate BMI
            bmi = weight / (height * height);



            height = Float.parseFloat(heightEditText.getText().toString());
            String bodyFat = String.valueOf(Integer.parseInt(bodeFat));
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            UserPersonalManager userPersonalManager = new UserPersonalManager(FirstTime.this, userId);
            userPersonalManager.setPersonalData(height, (int)weight, (int)bmi, bodyFat, Double.parseDouble(age));
            Toast.makeText(FirstTime.this, "You can see your bmi jest go to settings and personal!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FirstTime.this, MainSlider.class);
            startActivity(intent);


        }


    }
}