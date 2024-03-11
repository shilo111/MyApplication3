package com.example.myapplication.ui.dashboard;


import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.FireBaseHandler;
import com.example.myapplication.FoodAdapter;
import com.example.myapplication.FoodItem;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private EditText editTextFood, editTextCalories;
    private TextView textViewTotalCalories;
    private int totalCalories = 0;

    private FireBaseHandler fireBaseHandler;

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItemList;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewTotalCalories = root.findViewById(R.id.textViewTotalCalories);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        auth = FirebaseAuth.getInstance();
        fireBaseHandler = new FireBaseHandler(auth, root.getContext());


        try {
            showHomePageDesign(root);
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }

        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users value = dataSnapshot.getValue(Users.class);
                if (value != null) {
                    textViewTotalCalories.setText("Total Calories: " + value.getCalories());
                    totalCalories = value.getCalories();
                } else {

                    totalCalories = 0;
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());}
        });



        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        foodItemList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodItemList);
        recyclerView.setAdapter(foodAdapter);

//        retrieveFoodItemsFromFirebase();

        return root;
    }



//    private void retrieveFoodItemsFromFirebase() {
//        DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("Fooditems").child("Fooditem");
//        foodItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
//                    if (foodItem != null) {
//                        foodItemList.add(foodItem);
//                    }
//                }
//                foodAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void showHomePageDesign(View view) throws IllegalAccessException, java.lang.InstantiationException {
        editTextFood = view.findViewById(R.id.editTextFood);
        editTextCalories = view.findViewById(R.id.editTextCalories);
        Button btn = view.findViewById(R.id.buttonAdd);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodItem = editTextFood.getText().toString();
                String caloriesStr = editTextCalories.getText().toString();

                if (foodItem.isEmpty() || caloriesStr.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter both food name and calories", Toast.LENGTH_SHORT).show();
                    return;
                }
        int calories = Integer.parseInt(caloriesStr);
        totalCalories += calories;
        //textViewTotalCalories.setText("Total Calories: " + totalCalories);

        fireBaseHandler.getUserDetails(myRef, user -> {

            myRef.child(auth.getCurrentUser().getUid()).child("calories").setValue(totalCalories).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity().getApplicationContext(), "Well done!!!", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please try again :(", Toast.LENGTH_SHORT).show();
                }
            });

            myRef.child(auth.getCurrentUser().getUid()).child("Fooditems").child("Fooditem").setValue(foodItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(getActivity().getApplicationContext(), "Well done!!! food", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please try again :(", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Clear the input fields
        editTextFood.getText().clear();
        editTextCalories.getText().clear();
    }



        });
    }
}