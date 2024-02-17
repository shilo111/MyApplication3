package com.example.myapplication.ui.status;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.ExampleFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStatusBinding;
import com.example.myapplication.ui.finger.Finger;
import com.example.myapplication.ui.home.HomeViewModel;
import com.example.myapplication.ui.library.LibraryViewModel;


import com.example.myapplication.ui.notification.Notification;
import com.example.myapplication.ui.personal.Personal;
import com.example.myapplication.ui.reviews.Reviews;
import com.example.myapplication.ui.settings.Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class status extends Fragment {

    private StatusViewModel mViewModel;
    private FragmentStatusBinding binding;
    private TextView emailTextView;

    public static status newInstance() {
        return new status();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StatusViewModel statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);



        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        emailTextView = root.findViewById(R.id.emailTextView);

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            // Set email to TextView
            emailTextView.setText(email);
        }


         Button button = root.findViewById(R.id.LogOut);
         Button Reviews = root.findViewById(R.id.button);
         Button Account = root.findViewById(R.id.button2);
         Button PersonalIN = root.findViewById(R.id.button3);
         Button Notification = root.findViewById(R.id.button4);
         Button Finger = root.findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        Reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Reviews fragment = new Reviews();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

            }
        });

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Setting fragment = new Setting();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

            }
        });

        PersonalIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Personal fragment = new Personal();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

            }
        });

        Notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Notification fragment = new Notification();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

            }
        });

        Finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Finger fragment = new Finger();
                fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

            }
        });





        return root;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        // TODO: Use the ViewModel
    }

}