package com.example.project;

import java.io.Serializable;

public class Car implements Serializable {
    private int ID;
    private String company;
    private String modelYear; // Ensure this matches the JSON key
    private int mileage;
    private int seatsNumber;
    private int monthlyPrice;
    private int dailyPrice;
    private int price;
    private String color;
    private String status;
    private String image;

    // Constructor
    public Car(int ID, String company, String modelYear, int mileage, int seatsNumber, int monthlyPrice, int dailyPrice, int price, String color, String status, String image) {
        this.ID = ID;
        this.company = company;
        this.modelYear = modelYear;
        this.mileage = mileage;
        this.seatsNumber = seatsNumber;
        this.monthlyPrice = monthlyPrice;
        this.dailyPrice = dailyPrice;
        this.price = price;
        this.color = color;
        this.status = status;
        this.image = image;
    }

    // Getters
    public int getID() {
        return ID;
    }

    public String getCompany() {
        return company;
    }

    public String getModelYear() {
        return modelYear;
    }

    public int getMileage() {
        return mileage;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public int getMonthlyPrice() {
        return monthlyPrice;
    }

    public int getDailyPrice() {
        return dailyPrice;
    }

    public int getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }
}
