package com.example.myapplication.ui.status;

import androidx.fragment.app.FragmentManager; // Used for managing fragments within the activity
import androidx.fragment.app.FragmentTransaction; // Used for performing fragment transactions
import androidx.lifecycle.ViewModelProvider; // Used for ViewModel instantiation

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.MySharedPreferences;
import com.example.myapplication.MySharedPreferencesSteps;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModelStepsFire;
import com.example.myapplication.databinding.FragmentStatusBinding;
import com.example.myapplication.ui.finger.Finger;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.notification.Notification;
import com.example.myapplication.ui.personal.Personal;
import com.example.myapplication.ui.reviews.Reviews;
import com.example.myapplication.ui.settings.Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat; // Used for formatting dates
import java.util.Date; // Used for getting the current date
import java.util.Locale; // Used for setting locale for date formatting

public class status extends Fragment {

    private StatusViewModel mViewModel; // ViewModel for this fragment
    private FragmentStatusBinding binding; // View binding for this fragment
    private TextView emailTextView; // TextView to display the user's email
    private SharedViewModelStepsFire viewModel; // Shared ViewModel for steps
    Button button, Reviews, Account, PersonalIN, Notification, Finger; // Buttons for various actions

    public static status newInstance() {
        return new status(); // Method to create a new instance of this fragment
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StatusViewModel statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModelStepsFire.class);

        binding = FragmentStatusBinding.inflate(inflater, container, false); // Inflate the layout using view binding
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Indicate that this fragment has options menu

        emailTextView = root.findViewById(R.id.emailTextView); // Initialize email TextView

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            // Set email to TextView
            emailTextView.setText(email);
        }

        // Initialize buttons
        button = root.findViewById(R.id.LogOut);
        Reviews = root.findViewById(R.id.button);
        Account = root.findViewById(R.id.button2);
        PersonalIN = root.findViewById(R.id.button3);
        Notification = root.findViewById(R.id.button4);
        Finger = root.findViewById(R.id.button5);
        int value = viewModel.getValue(); // Get value from the shared ViewModel

        Date currentDate = new Date(); // Get current date

        // Define a date formatter for the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Format the current date using the formatter
        String formattedDate = formatter.format(currentDate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    String email = user.getEmail();
                    MySharedPreferencesSteps.saveUserData(getContext(), email, formattedDate, value); // Save user data to SharedPreferences
                }

                MySharedPreferences.saveBoolean(getContext(), true); // Save a boolean value to SharedPreferences
                Intent intent = new Intent(getContext(), MainActivity.class); // Navigate to MainActivity
                startActivity(intent);
            }
        });

        Reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Reviews fragment = new Reviews();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit(); // Replace the current fragment with Reviews fragment
            }
        });

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Setting fragment = new Setting();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit(); // Replace the current fragment with Setting fragment
            }
        });

        PersonalIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Personal fragment = new Personal();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit(); // Replace the current fragment with Personal fragment
            }
        });

        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Notification fragment = new Notification();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit(); // Replace the current fragment with Notification fragment
            }
        });

        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Finger fragment = new Finger();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit(); // Replace the current fragment with Finger fragment
            }
        });

        return root; // Return the root view
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StatusViewModel.class); // Initialize the ViewModel
        // TODO: Use the ViewModel
    }

}
