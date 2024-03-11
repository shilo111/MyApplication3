package com.example.myapplication;public class FoodItem {
    private String name;
    private int calories;

    // Constructor
    public FoodItem(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

}


