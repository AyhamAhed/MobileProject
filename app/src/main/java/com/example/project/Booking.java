package com.example.project;

public class Booking {
    private int id;
    private int carID;
    private int accountID;
    private int totalPrice;
    private String status;
    private String startDate;
    private String endDate;
    private String carImage;
    private String carModel;

    public Booking(int id, int carID, int accountID, int totalPrice, String status, String startDate, String endDate, String carImage, String carModel) {
        this.id = id;
        this.carID = carID;
        this.accountID = accountID;
        this.totalPrice = totalPrice;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carImage = carImage;
        this.carModel = carModel;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getCarID() { return carID; }
    public int getAccountID() { return accountID; }
    public int getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getCarImage() { return carImage; }
    public String getCarModel() { return carModel; }
}
