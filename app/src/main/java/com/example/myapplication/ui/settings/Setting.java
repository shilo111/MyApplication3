package com.example.myapplication.ui.settings;

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
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Setting extends Fragment {
    private FragmentSettingBinding binding;
    private SettingViewModel mViewModel;
    private FirebaseAuth mAuth;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;

    public static Setting newInstance() {
        return new Setting();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();


        currentPasswordEditText = root.findViewById(R.id.current_password_edittext);
        newPasswordEditText = root.findViewById(R.id.new_password_edittext);

        // Find views and set up UI listeners
        // e.g., EditText fields for current password and new password,
        // and a button to trigger the password update
        Button updatePasswordButton = root.findViewById(R.id.update_password_button);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        return root;
    }


    private void updatePassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Get current password and new password from EditText fields

            String currentPassword = currentPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            // Reauthenticate the user with their current password
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Update password
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Password updated successfully
                                                    Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Password update failed
                                                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Reauthentication failed
                                Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel
    }

}