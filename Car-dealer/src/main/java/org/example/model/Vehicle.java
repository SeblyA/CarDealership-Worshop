package org.example.model;

import org.example.model.enums.VehicleType;

import java.util.Locale;
import java.util.Objects;


    /**
     * Represents one vehicle in the dealership inventory.
     *
     * This model stores the main vehicle data used by the application,
     * such as VIN, year, make, model, type, color, odometer, and price.
     */
    public class Vehicle {

        private final int vin;
        private int year;
        private String make;
        private String model;
        private VehicleType vehicleType;
        private String color;
        private int odometer;
        private double price;

        public Vehicle(
                int vin,
                int year,
                String make,
                String model,
                VehicleType vehicleType,
                String color,
                int odometer,
                double price
        ) {
            this.vin = validateVin(vin);
            this.year = validateYear(year);
            this.make = validateRequiredText(make, "Make");
            this.model = validateRequiredText(model, "Model");
            this.vehicleType = validateVehicleType(vehicleType);
            this.color = validateRequiredText(color, "Color");
            this.odometer = validateNonNegativeInt(odometer, "Odometer");
            this.price = validateNonNegativeDouble(price, "Price");
        }

        public int getVin() {
            return vin;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = validateYear(year);
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = validateRequiredText(make, "Make");
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = validateRequiredText(model, "Model");
        }

        public VehicleType getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(VehicleType vehicleType) {
            this.vehicleType = validateVehicleType(vehicleType);
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = validateRequiredText(color, "Color");
        }

        public int getOdometer() {
            return odometer;
        }

        public void setOdometer(int odometer) {
            this.odometer = validateNonNegativeInt(odometer, "Odometer");
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = validateNonNegativeDouble(price, "Price");
        }

        /**
         * Converts this vehicle into a pipe-separated CSV line.
         *
         * This format is used when saving vehicles to the inventory file.
         * Example:
         * 12345|2020|Toyota|Camry|Sedan|Blue|45000|18995.00
         */
        public String toCsvLine() {
            return String.join("|",
                    String.valueOf(vin),
                    String.valueOf(year),
                    make,
                    model,
                    vehicleType.getDisplayName(),
                    color,
                    String.valueOf(odometer),
                    String.format(Locale.US, "%.2f", price)
            );
        }

        /**
         * Returns a formatted text version of the vehicle.
         *
         * This is useful for displaying vehicles in a clean table-like format
         * inside the console application.
         */
        @Override
        public String toString() {
            return String.format(Locale.US,
                    "%-8d %-6d %-12s %-15s %-8s %-10s %,10d $%,10.2f",
                    vin,
                    year,
                    make,
                    model,
                    vehicleType.getDisplayName(),
                    color,
                    odometer,
                    price
            );
        }

        /**
         * Vehicles are considered equal when they have the same VIN.
         *
         * The VIN is used as the unique identifier for a vehicle.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Vehicle vehicle)) {
                return false;
            }

            return vin == vehicle.vin;
        }

        /**
         * Uses VIN for hash-based collections such as HashSet or HashMap.
         *
         * This must match the logic used in equals().
         */
        @Override
        public int hashCode() {
            return Objects.hash(vin);
        }

        private static int validateVin(int vin) {
            if (vin <= 0) {
                throw new IllegalArgumentException("VIN must be a positive number.");
            }

            return vin;
        }

        private static int validateYear(int year) {
            if (year < 1886 || year > 2100) {
                throw new IllegalArgumentException("Year must be between 1886 and 2100.");
            }

            return year;
        }

        private static String validateRequiredText(String value, String fieldName) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(fieldName + " is required.");
            }

            return value.trim();
        }

        private static VehicleType validateVehicleType(VehicleType vehicleType) {
            if (vehicleType == null) {
                throw new IllegalArgumentException("Vehicle type is required.");
            }

            return vehicleType;
        }

        private static int validateNonNegativeInt(int value, String fieldName) {
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }

            return value;
        }

        private static double validateNonNegativeDouble(double value, String fieldName) {
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }

            return value;
        }
    }

