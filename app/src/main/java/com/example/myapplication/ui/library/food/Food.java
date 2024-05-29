package com.example.myapplication.ui.library.food;

import androidx.fragment.app.FragmentManager; // Used for managing fragments within the activity
import androidx.fragment.app.FragmentTransaction; // Used for performing fragment transactions
import androidx.lifecycle.ViewModelProvider; // Used for ViewModel instantiation

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
import android.webkit.WebChromeClient; // Used for webview settings and handling JavaScript
import android.webkit.WebView; // Used for displaying web content
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFoodBinding;
import com.example.myapplication.ui.library.sport.Sport;

public class Food extends Fragment {

    private FoodViewModel mViewModel; // ViewModel for this fragment
    private FragmentFoodBinding binding; // View binding for this fragment
    private TextView textView20;

    public static Food newInstance() {
        return new Food(); // Method to create a new instance of this fragment
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodBinding.inflate(inflater, container, false); // Inflate the layout using view binding
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Indicate that this fragment has options menu

        // Initialize the first WebView and load a YouTube video
        WebView webView = root.findViewById(R.id.webView);
        String video = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/PM8kiHcAD7Q?si=j5X9MU2atBn1v6FJ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        // Initialize the second WebView and load a YouTube video
        WebView webView2 = root.findViewById(R.id.webView2);
        String video2 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/osqvOUJjaCo?si=slLagHmhgLpfkp42\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView2.loadData(video2, "text/html","utf-8");
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.setWebChromeClient(new WebChromeClient());

        // Initialize the third WebView and load a YouTube video
        WebView webView3 = root.findViewById(R.id.webView3);
        String video3 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/1N6hbRbyAeQ?si=2FALjZh-e_aJEvxA\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView3.loadData(video3, "text/html","utf-8");
        webView3.getSettings().setJavaScriptEnabled(true);
        webView3.setWebChromeClient(new WebChromeClient());

        // Initialize the fourth WebView and load a YouTube video
        WebView webView4 = root.findViewById(R.id.webView4);
        String video4 = "<iframe width=\"100%\" height=\"85%\" src=\"https://www.youtube.com/embed/dLairfd8bZU?si=ssmCjce_JJzTy-W2\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView4.loadData(video4, "text/html","utf-8");
        webView4.getSettings().setJavaScriptEnabled(true);
        webView4.setWebChromeClient(new WebChromeClient());

        return root; // Return the root view
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.your_menu_resource, menu); // Inflate the menu resource file
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the selected menu item ID

        if (itemId == R.id.menu_item1) {
            // If menu item 1 is selected, replace the current fragment with the Sport fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Sport fragment = new Sport();
            fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
            return true;
        } else if (itemId == R.id.menu_item2) {
            // If menu item 2 is selected, replace the current fragment with the Food fragment
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
        mViewModel = new ViewModelProvider(this).get(FoodViewModel.class); // Initialize the ViewModel
        // TODO: Use the ViewModel
    }

}
