package com.example.myapplication;

// User.java
public class User {
    private String name;
    private String email;
    private int age;
    private int dayStepsSensor;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email, int age, int dayStepsSensor) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.dayStepsSensor = dayStepsSensor;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

