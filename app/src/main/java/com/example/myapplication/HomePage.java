package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.myapplication.databinding.ActivityHomePageBinding;
import com.example.myapplication.ui.dashboard.DashboardFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.library.LibraryFragment;
import com.example.myapplication.ui.library.sport.Sport;
import com.example.myapplication.ui.status.status;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class HomePage extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ActivityHomePageBinding binding;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private static final int CAMERA_REQUEST = 1888;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getRoot().getContext()
        setupHomePage(savedInstanceState, binding.fab.getContext());

        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // Method to capture photo and upload to Firebase Storage



    }

    private void setupHomePage(Bundle savedInstanceState, Context context) {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.navigation_dashboard) {
                replaceFragment(new DashboardFragment());
            } else if (itemId == R.id.navigation_library) {
                replaceFragment(new Sport());
            } else if (itemId == R.id.navigation_status) {
                replaceFragment(new status());
            }

            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(context);
            }
        });
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog(Context context) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });















        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomePage.this,"Set step goal",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SetCalories.class);
                startActivity(intent);

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomePage.this,"Share app",Toast.LENGTH_SHORT).show();

                String message = "Check out this awesome app!";

// Create an Intent with the action set to ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

// Set the type of content to text
                shareIntent.setType("text/plain");

// Put the message to be shared into the Intent
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);

// Start the activity with the Intent
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photoBitmap = (Bitmap) extras.get("data");

            // Convert Bitmap to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoByteArray = baos.toByteArray();

            // Upload photo to Firebase Storage
            uploadPhoto(photoByteArray);
        }
    }

    private void uploadPhoto(byte[] photoByteArray) {
        // Create a reference to the location where you want to upload the photo
        auth = FirebaseAuth.getInstance();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference photoRef = storageRef.child(auth.getCurrentUser().getUid() +"/"+ randomKey);

        // Upload the photo
        UploadTask uploadTask = photoRef.putBytes(photoByteArray);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Photo uploaded successfully
            // Get the download URL of the uploaded photo
            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String photoUrl = uri.toString();
                // Save photo URL and description to Firebase Firestore

                savePhotoData(photoUrl, "Description of the photo");
            });
        }).addOnFailureListener(exception -> {
            // Handle any errors that occur during the upload
            Toast.makeText(HomePage.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void savePhotoData(String photoUrl, String description) {
        // Create a new document in the "photos" collection with auto-generated ID
        db.collection("photos")
                .add(new Photo(photoUrl, description))
                .addOnSuccessListener(documentReference -> {
                    // DocumentSnapshot added with ID: documentReference.getId()
                    Toast.makeText(HomePage.this, "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(HomePage.this, "Failed to upload photo data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }







}