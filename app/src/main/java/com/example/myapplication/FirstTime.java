package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;

public class FirstTime extends AppCompatActivity {
EditText heightEditText,weightEditText,ageEditText,bmiEditText,bodyFatEditText;
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
        bmiEditText = findViewById(R.id.bmiEditText);
        bodyFatEditText = findViewById(R.id.bodyFatEditText);
        button = findViewById(R.id.saveButton);

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                calculateBMI();


            }
        });


    }

   private void calculateBMI() {


        double age = Double.parseDouble(ageEditText.getText().toString());

        int bodeFat = Integer.parseInt(bodyFatEditText.getText().toString());


        // Get weight and height from EditText
        String weightStr = weightEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();

        float height = 0;
        float weight = 0;
        float bmi = 0;
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Convert string values to float
            weight = Float.parseFloat(weightStr);
            height = Float.parseFloat(heightStr) / 100;

            // Calculate BMI
            bmi = weight / (height * height);

            // Display BMI in the BMI EditText
            bmiEditText.setText(String.format("%.2f", bmi));
            height = Float.parseFloat(heightEditText.getText().toString());

        } else {
            // Clear BMI EditText if weight or height is empty
            bmiEditText.setText("");
        }

        FireBaseHandler.setNewData((double) height, (int) weight, (int) bmi, bodeFat, age);
    }
}