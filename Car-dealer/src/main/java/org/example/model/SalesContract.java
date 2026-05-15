package org.example.model;

import java.time.LocalDate;

public abstract class SalesContract extends Contract  {
    private static final double SALES_TAX_RATE =0.05;
    private static final double RECORDING_FEE = 100.00;
    private static final double PROCESSING_FEE_UNDER_10000 = 295.00;
    private static final double PROCESSING_FEE_10000_OR_MORE = 495.00;
    private boolean finance;
public SalesContract(
        LocalDate contractDate,
        String customerName,
        String customerEmail,
        Vehicle vehicleSold,
        boolean finance) {
    super (contractDate,customerName,customerEmail,vehicleSold);
    this.finance = finance;
}
    public double getSalesTaxAmount() {
        return getVehicleSold().getPrice() * SALES_TAX_RATE;
    }
    @Override
    public  double getTotalPrice() {
        return 0;
    }

    private Vehicle getVehicleSold() {
    return null;
    }

    public double getRecordingFee() {
        return RECORDING_FEE;
    }

    public double getProcessingFee() {
        if (getVehicleSold().getPrice() < 10000) {
            return PROCESSING_FEE_UNDER_10000;
        }

        return PROCESSING_FEE_10000_OR_MORE;
    }

    public boolean isFinance() {
        return finance;
    }

    public void setFinance(boolean finance) {
        this.finance = finance;
    }

    @Override
    public double setTotalprice() {
        return 0;
    }

    @Override
    public double getMonthlyPayment() {
        if (!finance) {
            return 0;
        }
        double monthlyInterestRate;
        int numberOfMonths;

        if (getVehicleSold().getPrice() >= 10000) {
            monthlyInterestRate = 0.0425 / 12;
            numberOfMonths = 48;
        } else {
            monthlyInterestRate = 0.0525 / 12;
            numberOfMonths = 24;
        }

        double loanAmount = getTotalPrice();

        return loanAmount * monthlyInterestRate
                / (1 - Math.pow(1 + monthlyInterestRate,-numberOfMonths));
    }
}


