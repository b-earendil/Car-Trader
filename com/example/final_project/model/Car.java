package com.example.final_project.model;

public class Car {
    private String make;
    private String model;
    private int year;
    private int price;
    private int mileage;
    private String color;
    private int vin;

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return make + "\t\t" + model + "\t\t" + year + "\t\t" + price;
    }
}
