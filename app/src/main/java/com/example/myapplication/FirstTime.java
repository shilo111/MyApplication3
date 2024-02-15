package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstTime extends AppCompatActivity {
EditText heightEditText,weightEditText,ageEditText,bmiEditText,bodyFatEditText;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);


       heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        ageEditText = findViewById(R.id.ageEditText);
        bmiEditText = findViewById(R.id.bmiEditText);
        bodyFatEditText = findViewById(R.id.bodyFatEditText);
button = findViewById(R.id.saveButton);

button.setOnClickListener(new View.OnClickListener() {


    @Override
    public void onClick(View view) {
        double height = Double.parseDouble(heightEditText.getText().toString());
        int weight = Integer.parseInt(weightEditText.getText().toString());
        double age = Double.parseDouble(ageEditText.getText().toString());
        int bmi = Integer.parseInt(bmiEditText.getText().toString());
        int bodeFat = Integer.parseInt(bodyFatEditText.getText().toString());

        FireBaseHandler.setNewData(height,weight,bmi,bodeFat,age);

    }
});






    }


}