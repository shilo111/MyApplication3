package com.example.myapplication;

public class Users {

    private int calories;
    private int steps;
    private int GoalStep;

    public  Users( ){

    }

    public  Users(int calories, int steps, int GoalStep)
    {
        this.calories = calories;
        this.steps = steps;
        this.GoalStep = GoalStep;
    }


    public int getCalories() {
        return calories;
    }
    public int getSteps() {
        return steps;
    }

    public int getGoalStep() {
        return GoalStep;
    }

    // Setter method for calories
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setGoalStep(int goalStep) {
        GoalStep = goalStep;
    }
}
