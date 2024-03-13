package com.example.myapplication;
import com.google.firebase.database.DataSnapshot;
import java.util.HashMap;
import java.util.Map;

public class FoodItem {
    private String name;
    private int calories;

    // Required default constructor for Firebase
    public FoodItem() {}

    public FoodItem(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    // Method to convert FoodItem object to HashMap for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("calories", calories);
        return result;
    }

    // Method to create FoodItem object from Firebase snapshot
    public static FoodItem fromSnapshot(DataSnapshot snapshot) {
        String name = (String) snapshot.child("name").getValue();
        long caloriesLong = (long) snapshot.child("calories").getValue();
        int calories = (int) caloriesLong;
        return new FoodItem(name, calories);
    }
}




