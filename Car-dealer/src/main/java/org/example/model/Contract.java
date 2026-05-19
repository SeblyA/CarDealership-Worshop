package org.example.model;

import java.time.LocalDate;

public abstract class Contract {
    private String contractDate;
    private String customerName;
    private String customerEmail;
    private Vehicle thevehicle;
    private boolean isVehicleSold;

    public Contract(LocalDate contractDate, String customerEmail, String customerName, Vehicle vehicle) {
        this.contractDate = contractDate.toString();
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.thevehicle = vehicle;
    }



    public Vehicle getTheVehicle() {
        return thevehicle;
    }

    private double totalPrice;
    private double monthPayment;

    public String getContractDate() {
        return contractDate;
    }
    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean isVehicleSold() {
        return isVehicleSold;
    }

    public void setVehicleSold(boolean vehicleSold ) {
        isVehicleSold = vehicleSold;
    }

    public abstract double getTotalprice();

    public abstract double getTotalPrice();

    public abstract double setTotalprice();



    public abstract double getMonthlyPayment();

    public abstract String toCsvLine();
}

