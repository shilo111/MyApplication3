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

public class Finger extends Fragment {

    private FingerViewModel mViewModel;
    private FragmentFingerBinding binding;
    BarChart barChart;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference();
    private String currentDate = "";
    private FirebaseAuth auth;
    private int steps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFingerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        loadChartData();
      //  barChart = root.findViewById(R.id.bar_chart);

//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//        // Initialize FirebaseAuth instance after the user is authenticated
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        if (auth.getCurrentUser() != null) {
//            myRef.child("dataSteps").child(auth.getCurrentUser().getUid()).child(currentDate).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Datasteps value = dataSnapshot.getValue(Datasteps.class);
//                    if (value != null) {
//                        steps = value.getSteps();
//
//                        // Update chart data after retrieving steps
//                        BarEntry barEntry = new BarEntry(day, steps);
//                        barEntries.add(barEntry);
//                        BarDataSet barDataSet = new BarDataSet(barEntries, "steps");
//                        barDataSet.setColor(Color.YELLOW);
//                        barDataSet.setDrawValues(false);
//                        barChart.setData(new BarData(barDataSet));
//                        barChart.animateY(5000);
//                        barChart.getDescription().setText("steps chart");
//                        barChart.getDescription().setTextColor(Color.BLUE);
//                        barChart.invalidate(); // Invalidate chart to redraw
//                    } else {
//                        // Handle the case when value is null
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle onCancelled event
//                }
//            });
//        }

        return root;
    }

    private void loadChartData() {
        DatabaseReference stepCountRef = FirebaseDatabase.getInstance().getReference("dataSteps").child(auth.getCurrentUser().getUid());

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
                BarChart chart = getView().findViewById(R.id.chart);
                chart.setData(barData);
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.invalidate(); // Refresh the chart
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