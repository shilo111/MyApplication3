package com.example.myapplication.ui.personal;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.PersonalData;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentPersonalBinding;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.ui.settings.SettingViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Personal extends Fragment {
    private FragmentPersonalBinding binding;
    private PersonalViewModel mViewModel;

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth auth;


    EditText heightEditText,weightEditText,ageEditText,bmiEditText,bodyFatEditText;

    private Button button;
    private DatabaseReference databaseReference;
    public static Personal newInstance() {
        return new Personal();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        heightEditText = root.findViewById(R.id.heightEditText);
        weightEditText = root.findViewById(R.id.weightEditText);
        ageEditText = root.findViewById(R.id.ageEditText);
        bmiEditText = root.findViewById(R.id.bmiEditText);
        bodyFatEditText = root.findViewById(R.id.bodyFatEditText);
        button = root.findViewById(R.id.saveButton);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        myRef.child("dataUser").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PersonalData value = dataSnapshot.getValue(PersonalData.class);

                    heightEditText.setText(("" + value.getHeight()));
                    weightEditText.setText(("" + value.getWeight()));
                    ageEditText.setText(("" + value.getAge()));
                    bmiEditText.setText(("" + value.getBmi()));
                    bodyFatEditText.setText(("" + value.getBodyFat()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double height = Double.parseDouble(heightEditText.getText().toString());
                int weight = Integer.parseInt(weightEditText.getText().toString());
                double age = Double.parseDouble(ageEditText.getText().toString());
                int bmi = Integer.parseInt(bmiEditText.getText().toString());
                int bodeFat = Integer.parseInt(bodyFatEditText.getText().toString());

                calculateBMI();
            }
        });




        return root;
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



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        // TODO: Use the ViewModel
    }

}