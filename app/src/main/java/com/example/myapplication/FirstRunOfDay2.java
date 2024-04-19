package com.example.myapplication;

public class FirstRunOfDay2 {
    private int dayStepsSensor;

    public FirstRunOfDay2() {
        // Default constructor required for Firebase
    }
    public FirstRunOfDay2(int dayStepsSensor) {
        this.dayStepsSensor = dayStepsSensor;
    }
    public int getdayStepsSensor() {
        return dayStepsSensor;
    }
}
