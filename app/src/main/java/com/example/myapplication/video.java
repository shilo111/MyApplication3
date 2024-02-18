package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        RelativeLayout splashLayout = findViewById(R.id.splashLayout);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashLayout.startAnimation(fadeIn);

        // Delay for a few seconds and then start the main activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(video.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the splash screen activity
        }, 2000); //
    }
}