package com.example.myapplication.ui.status;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStatusBinding;
import com.example.myapplication.ui.home.HomeViewModel;
import com.example.myapplication.ui.library.LibraryViewModel;

public class status extends Fragment {

    private StatusViewModel mViewModel;
    private FragmentStatusBinding binding;


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


         Button button = root.findViewById(R.id.LogOut);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

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