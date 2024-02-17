package com.example.myapplication.ui.library;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLibraryBinding;
import com.example.myapplication.ui.dashboard.DashboardFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.library.food.Food;
import com.example.myapplication.ui.library.sport.Sport;
import com.example.myapplication.ui.reviews.Reviews;
import com.example.myapplication.ui.status.status;

import java.util.List;

public class LibraryFragment extends Fragment {

    private LibraryViewModel mViewModel;
    private FragmentLibraryBinding binding;

    private RecyclerView recyclerView;
Button sport, food;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LibraryViewModel libraryViewModel =
                new ViewModelProvider(this).get(LibraryViewModel.class);


        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //food = root.findViewById(R.id.)




//        WebView webView = root.findViewById(R.id.webView);
//        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eMjyvIQbn9M?si=vPeT7vcgjVYsNbqs\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
//        webView.loadData(video, "text/html","utf-8");
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());



        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.your_menu_resource, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_item1) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Sport fragment = new Sport();
            fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
            return true;
        } else if (itemId == R.id.menu_item2) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Food fragment = new Food();
            fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
            return true;
        } else {
            // Handle other cases or call the superclass method
            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        // TODO: Use the ViewModel
    }



}