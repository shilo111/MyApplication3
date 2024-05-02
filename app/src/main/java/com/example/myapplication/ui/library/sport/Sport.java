package com.example.myapplication.ui.library.sport;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLibraryBinding;
import com.example.myapplication.databinding.FragmentSportBinding;
import com.example.myapplication.ui.library.LibraryViewModel;
import com.example.myapplication.ui.library.food.Food;

public class Sport extends Fragment {

    private SportViewModel mViewModel;


    private FragmentSportBinding binding;

    public static Sport newInstance() {
        return new Sport();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        WebView webView = root.findViewById(R.id.webView);
        String video = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/eMjyvIQbn9M?si=vPeT7vcgjVYsNbqs\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        WebView webView2 = root.findViewById(R.id.webView2);
        String video2 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/pbyBBCzXtnI?si=NnBhEhiEJI03XsOV\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView2.loadData(video2, "text/html","utf-8");
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.setWebChromeClient(new WebChromeClient());

        WebView webView3 = root.findViewById(R.id.webView3);
        String video3 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/vFXwQSuY_gw?si=L6KamzsqdECUEy31\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView3.loadData(video3, "text/html","utf-8");
        webView3.getSettings().setJavaScriptEnabled(true);
        webView3.setWebChromeClient(new WebChromeClient());

        WebView webView4 = root.findViewById(R.id.webView4);
        String video4 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/0XRKDJdG_rA?si=3pNLMSP2vbLX2onU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView4.loadData(video4, "text/html","utf-8");
        webView4.getSettings().setJavaScriptEnabled(true);
        webView4.setWebChromeClient(new WebChromeClient());


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
        mViewModel = new ViewModelProvider(this).get(SportViewModel.class);
        // TODO: Use the ViewModel
    }

}