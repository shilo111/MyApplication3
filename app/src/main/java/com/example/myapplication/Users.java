package com.example.myapplication;

public class Users {

    private int calories;

    public  Users( ){

    }

    public  Users(int calories)
    {
        this.calories = calories;
    }


    public int getCalories() {
        return calories;
    }

    // Setter method for calories
    public void setCalories(int calories) {
        this.calories = calories;
    }
}
