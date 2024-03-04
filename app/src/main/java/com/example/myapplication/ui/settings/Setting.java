package com.example.myapplication.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Locale;


public class Setting extends Fragment {
    private FragmentSettingBinding binding;
    private SettingViewModel mViewModel;
    private FirebaseAuth mAuth;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private Spinner languageSpinner;
    private Button applyButton;
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

        languageSpinner = root.findViewById(R.id.language_spinner);
        applyButton = root.findViewById(R.id.apply_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLanguage = languageSpinner.getSelectedItem().toString();
                //changeLanguage(selectedLanguage);
            }
        });

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


    private void changeLanguage(String selectedLanguage) {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
        getActivity().recreate();

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