package com.example.myapplication;

public class Datasteps {
    private int steps; // Assuming steps is an integer value

    public Datasteps() {
        // Default constructor required for Firebase
    }

    public Datasteps(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}