package com.example.myapplication;

public class Datasteps {
    private int steps; // Assuming this is one of the fields in your Datasteps class

    // Default constructor
    public Datasteps() {
        // Default constructor required by Firebase for deserialization
    }

    // Constructor with arguments if needed
    public Datasteps(int steps) {
        this.steps = steps;
    }

    // Getter and setter methods for steps field
    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}