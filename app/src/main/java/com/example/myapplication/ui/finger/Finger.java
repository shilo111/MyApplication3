package com.example.myapplication.ui.finger;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Datasteps;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentFingerBinding;
import com.example.myapplication.databinding.FragmentPersonalBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
public class Finger extends Fragment {

    private FingerViewModel mViewModel;
    private FragmentFingerBinding binding;
    BarChart barChart;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private String currentDate = "";
    private FirebaseAuth auth;
    private int steps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFingerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(new Date());

        auth = FirebaseAuth.getInstance();
        barChart = root.findViewById(R.id.chart);

        if (auth.getCurrentUser() != null) {
            loadChartData();
        } else {
            // Handle the case when the user is not logged in
            // For example, show a message or redirect to the login screen
        }

        return root;
    }

    private void loadChartData() {
        DatabaseReference stepCountRef = database.getReference("dataSteps").child(auth.getCurrentUser().getUid());

        stepCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();

                int index = 0;
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey(); // Get the date
                    Integer steps = dateSnapshot.getValue(Integer.class); // Get the step count for the date

                    // Add data to entries and labels
                    if (steps != null) {
                        entries.add(new BarEntry(index, steps));
                        labels.add(date);
                        index++;
                    }
                }

                // Create a dataset from entries
                BarDataSet dataSet = new BarDataSet(entries, "Steps");
                BarData barData = new BarData(dataSet);

                // Get the chart reference
                barChart.setData(barData);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.invalidate(); // Refresh the chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FingerViewModel.class);
        // TODO: Use the ViewModel
    }
}
