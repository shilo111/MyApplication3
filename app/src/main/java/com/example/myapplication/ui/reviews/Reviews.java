package com.example.myapplication.ui.reviews;

import androidx.lifecycle.ViewModelProvider; // Used for ViewModel instantiation

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager; // Used for RecyclerView layout
import androidx.recyclerview.widget.RecyclerView; // Used for displaying lists of data

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.DataClass; // Custom data class
import com.example.myapplication.MyAdapter; // Custom adapter for RecyclerView
import com.example.myapplication.R;
import com.example.myapplication.UploadActivity; // Activity for uploading data
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth; // Firebase Authentication
import com.google.firebase.firestore.FirebaseFirestore; // Firestore database
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Used for FloatingActionButton
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference; // Firebase Realtime Database reference
import com.google.firebase.database.FirebaseDatabase; // Firebase Realtime Database
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reviews extends Fragment {
    FloatingActionButton fab; // FloatingActionButton to trigger upload activity
    private RecyclerView recyclerView; // RecyclerView to display list of reviews
    private ArrayList<DataClass> dataList; // List to store review data
    private MyAdapter adapter; // Adapter for the RecyclerView

    public static Reviews newInstance() {
        return new Reviews(); // Method to create a new instance of this fragment
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reviews, container, false); // Inflate the layout
        fab = root.findViewById(R.id.fab); // Initialize the FloatingActionButton
        recyclerView = root.findViewById(R.id.recyclerView); // Initialize the RecyclerView
        recyclerView.setHasFixedSize(true); // Improve performance if the size of the RecyclerView does not change
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager for RecyclerView
        dataList = retrieveDataFromSharedPreferences(); // Retrieve data from SharedPreferences
        adapter = new MyAdapter(getContext(), dataList); // Initialize the adapter with context and data
        recyclerView.setAdapter(adapter); // Set the adapter to the RecyclerView

        fab.setOnClickListener(new View.OnClickListener() { // Set onClickListener for the FloatingActionButton
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadActivity.class); // Intent to start UploadActivity
                startActivity(intent);
            }
        });

        return root; // Return the root view
    }

    private ArrayList<DataClass> retrieveDataFromSharedPreferences() {
        ArrayList<DataClass> dataList = new ArrayList<>(); // Initialize the list to store data
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UploadData", Context.MODE_PRIVATE); // Get SharedPreferences
        String json = sharedPreferences.getString("imageData", ""); // Retrieve JSON string containing image URIs and captions
        if (!TextUtils.isEmpty(json)) { // Check if JSON string is not empty
            try {
                JSONArray jsonArray = new JSONArray(json); // Convert JSON string to JSONArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i); // Get each JSONObject
                    String imageUrl = jsonObject.getString("imageUrl"); // Get image URL
                    String caption = jsonObject.getString("caption"); // Get caption
                    dataList.add(new DataClass(imageUrl, caption)); // Add data to the list
                }
            } catch (JSONException e) {
                e.printStackTrace(); // Handle JSON parsing exception
            }
        }
        return dataList; // Return the list of data
    }
}
