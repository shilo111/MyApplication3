package com.example.myapplication;

public class PersonalData {


   private double height;
   private int weight;
   private int bmi;
   private int bodyFat;
   private double age;




public PersonalData(double height,int weight,int bmi, int bodyFat, double age)
{
    this.height = height;
    this.weight = weight;
    this.bmi = bmi;
    this.bodyFat = bodyFat;
    this.age = age;
}

public PersonalData()
{

}

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBodyFat(int bodyFat) {
        this.bodyFat = bodyFat;
    }

    public int getBodyFat() {
        return bodyFat;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getAge() {
        return age;
    }
}
