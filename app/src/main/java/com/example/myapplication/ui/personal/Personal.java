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

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.databinding.FragmentPersonalBinding;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.ui.settings.SettingViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Personal extends Fragment {
    private FragmentPersonalBinding binding;
    private PersonalViewModel mViewModel;


    private EditText nameEditText, emailEditText, ageEditText;
    private Button saveButton;
    private DatabaseReference databaseReference;
    public static Personal newInstance() {
        return new Personal();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        nameEditText = root.findViewById(R.id.nameEditText);
        emailEditText = root.findViewById(R.id.emailEditText);
        ageEditText = root.findViewById(R.id.ageEditText);
        saveButton = root.findViewById(R.id.saveButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });




        return root;
    }


    private void saveUserInfo() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        int age = Integer.parseInt(ageEditText.getText().toString().trim());

        User user = new User(name, email, age);

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "User information saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to save user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        // TODO: Use the ViewModel
    }

}