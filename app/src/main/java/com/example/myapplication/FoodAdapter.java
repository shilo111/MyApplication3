package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodItems;
    private OnItemClickListener listener;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(FoodItem foodItem);
    }

    // Constructor
    public FoodAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;

    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.bind(foodItem, listener);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    // ViewHolder class
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView foodNameTextView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
        }

        // Bind method to bind data to views and set click listener
        public void bind(final FoodItem foodItem, final OnItemClickListener listener) {
            foodNameTextView.setText(foodItem.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(foodItem);
                }
            });
        }
    }
}
