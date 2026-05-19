package org.example.model;

public class SalesContract extends Contract {
    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100.00;
    private static final double PROCESSING_FEE_UNDER_10000 = 295.00;
    private static final double PROCESSING_FEE_10000_OR_MORE = 495.00;

    private final double salesTax;
    private final double processingFee;

    private boolean finance;

    public SalesContract(
            LocalDate contractDate,
            String customerName,
            String customerEmail,
            Vehicle vehicleSold) {
        //Call parent constructor
        super(contractDate, customerName, customerEmail, vehicleSold);
        this.salesTax = vehicleSold.getPrice() * SALES_TAX_RATE;//calculate 5% tax
        this.finance = finance;
        //processing thresold fee for vehicles under  and more than $10,000
        this.processingFee = vehicleSold.getPrice() < 10000 ? PROCESSING_FEE_UNDER_10000 : PROCESSING_FEE_10000_OR_MORE;
    }

    @Override
    public double getTotalprice() {
        return  getTheVehicle().getPrice() + getSalesTaxAmount() + RECORDING_FEE + getProcessingFee();
    }

    @Override
    public double getTotalPrice() {
        return 0;
    }


    @Override
    public double setTotalprice() {
        return 0;
    }

    @Override
    public double getMonthlyPayment() {
        if (!finance) {

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
                / (1 - Math.pow(1 + monthlyInterestRate, -numberOfMonths));
    }

    @Override
    public String toCsvLine() {


        return "SALE|"
                + contractDate() + "|"
                + getCustomerName() + "|"
                + getCustomerEmail() + "|"
                + vehicleSold.getVin() + "|"
                + vehicleSold.getYear() + "|"
                + vehicleSold.getMake() + "|"
                + vehicleSold.getModel() + "|"
                + vehicleSold.getPrice() + "|"
                + isFinanced() + "|"
                + getTotalPrice() + "|"
                + getMonthlyPayment();
    }
}


public double getSalesTaxAmount() {
    return getVehicleSold().getPrice() * SalesContract.SALES_TAX_RATE;
}


private Vehicle getVehicleSold() {
    return null;
}

public double getRecordingFee() {
    return SalesContract.RECORDING_FEE;
}

public double getProcessingFee() {
    if (getVehicleSold().getPrice() < 10000) {
        return SalesContract.PROCESSING_FEE_UNDER_10000;
    }

    return SalesContract.PROCESSING_FEE_10000_OR_MORE;
}

public boolean isFinanced() {
    return finance;
}

public void setFinance(boolean finance) {
    finance = finance;
}






