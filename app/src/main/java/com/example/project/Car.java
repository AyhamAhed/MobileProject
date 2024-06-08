package com.example.project;

public class Car {
    private String name;
    private String details;
    private int imageResource;

    public Car(String name, String details, int imageResource) {
        this.name = name;
        this.details = details;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getImageResource() {
        return imageResource;
    }
}
