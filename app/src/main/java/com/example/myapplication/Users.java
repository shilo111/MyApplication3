package com.example.myapplication;

public class Users {

    private int calories;
    private int steps;

    public  Users( ){

    }

    public  Users(int calories, int steps)
    {
        this.calories = calories;
        this.steps = steps;
    }


    public int getCalories() {
        return calories;
    }
    public int getSteps() {
        return steps;
    }

    // Setter method for calories
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }
}
