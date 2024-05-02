package com.example.myapplication.ui.finger;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Datasteps;
import com.example.myapplication.MySharedPreferencesSteps;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.example.myapplication.databinding.FragmentFingerBinding;
import com.example.myapplication.databinding.FragmentPersonalBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
import java.util.Map;

public class Finger extends Fragment {

    private FingerViewModel mViewModel;
    private FragmentFingerBinding binding;
    BarChart barChart;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFingerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barChart = root.findViewById(R.id.chart);

        // Initialize FirebaseAuth instance after the user is authenticated
        auth = FirebaseAuth.getInstance();

        // Load chart data from SharedPreferences
        loadChartData();

        return root;
    }

    private void loadChartData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Integer> userData = MySharedPreferencesSteps.getUserData(getContext(), userId);

        if (userData != null) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            int index = 0;
            for (Map.Entry<String, Integer> entry : userData.entrySet()) {
                String date = entry.getKey();
                int steps = entry.getValue();

                // Add data to entries and labels
                entries.add(new BarEntry(index, steps));
                labels.add(date);
                index++;
            }

            // Create a dataset from entries
            BarDataSet dataSet = new BarDataSet(entries, "Steps");
            BarData barData = new BarData(dataSet);

            // Set labels for the X-axis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);

            // Set chart data
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.invalidate(); // Refresh the chart
        } else {
            Log.d("PathActivity", "no data");
            // Handle the case when no data is available
            // For example, display a message or hide the chart
        }
    }







    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FingerViewModel.class);
        // TODO: Use the ViewModel
    }

}