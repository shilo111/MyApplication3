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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFingerBinding;
import com.example.myapplication.databinding.FragmentPersonalBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Finger extends Fragment {

    private FingerViewModel mViewModel;
    private FragmentFingerBinding binding;
    BarChart barChart;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFingerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barChart = root.findViewById(R.id.bar_chart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int i = 0; i<7; i++)
        {
            float value = (float) (i*10.0);
            BarEntry barEntry = new BarEntry(i,value);

            barEntries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"steps");
        barDataSet.setColor(Color.YELLOW);
        barDataSet.setDrawValues(false);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(5000);
        barChart.getDescription().setText("steps chart");
        barChart.getDescription().setTextColor(Color.BLUE);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FingerViewModel.class);
        // TODO: Use the ViewModel
    }

}