package org.example.model;

import java.time.LocalDate;

public  class LeaseContract extends Contract {
    private double EXPECTED_ENDING_VALUE;
    private double LEASE_FEE;

    public LeaseContract(
            LocalDate contractDate,
            String customerName,
            String customerEmail,
            Vehicle vehicle
    ) {
        super(contractDate, customerName, customerEmail, vehicle);
        this.EXPECTED_ENDING_VALUE = vehicle.getPrice() * 0.50;
        this.LEASE_FEE = vehicle.getPrice() * 0.07;
    }

    @Override
    public double getTotalprice() {
        double price = getTheVehicle().getPrice();
        return (price - EXPECTED_ENDING_VALUE) + LEASE_FEE;
    }


    @Override
    public double getMonthlyPayment() {
        return getTotalprice();
    }

    @Override
    public String toCsvLine() {
        Vehicle v = getTheVehicle();
        return "LEASE|"
                + getContractDate() + "|"
                + getCustomerName() + "|"
                + getCustomerEmail() + "|"
                + v.getVin() + "|"
                + v.getYear() + "|"
                + v.getMake() + "|"
                + v.getModel() + "|"
                + v.getPrice();
    }

}





