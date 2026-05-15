package org.example.model;

import org.example.model.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public  class Dealership {
    private String name;
    private String address;
    private String phone;

    // The inventory stores all vehicles that belong to this dealership.
    // final means the inventory list reference cannot be replaced after construction.
    private final List<Vehicle> inventory;

    /**
     * Creates a new dealership with an empty inventory.
     */
    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        // Start with an empty ArrayList so vehicles can be added later.
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns all vehicles in the inventory.
     *
     * A new ArrayList is returned instead of the original inventory list.
     * This protects the internal inventory from being changed directly
     * outside this class.
     */
    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(inventory);
    }

    /**
     * Finds all vehicles with a price between the given minimum and maximum price.
     */
    public List<Vehicle> getVehiclesByPrice(double minPrice, double maxPrice) {
        return inventory.stream()
                // Keep only vehicles where the price is inside the selected range.
                .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice)
                .toList();
    }

    /**
     * Finds vehicles by make and/or model.
     *
     * Empty search values are allowed:
     * - If make is empty, all makes are accepted.
     * - If model is empty, all models are accepted.
     */
    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        // Normalize the search text to make searching case-insensitive.
        String makeSearch = make.trim().toLowerCase();
        String modelSearch = model.trim().toLowerCase();

        return inventory.stream()
                // If makeSearch is blank, skip make filtering.
                // Otherwise, check whether the vehicle make contains the search text.
                .filter(vehicle -> makeSearch.isBlank() || vehicle.getMake().toLowerCase().contains(makeSearch))

                // If modelSearch is blank, skip model filtering.
                // Otherwise, check whether the vehicle model contains the search text.
                .filter(vehicle -> modelSearch.isBlank() || vehicle.getModel().toLowerCase().contains(modelSearch))
                .toList();
    }

    /**
     * Finds all vehicles from a year between the given minimum and maximum year.
     */
    public List<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        return inventory.stream()
                // Keep only vehicles where the year is inside the selected range.
                .filter(vehicle -> vehicle.getYear() >= minYear && vehicle.getYear() <= maxYear)
                .toList();
    }

    /**
     * Finds all vehicles where the color contains the given search text.
     */
    public List<Vehicle> getVehiclesByColor(String color) {
        // Normalize the search text to make searching case-insensitive.
        String search = color.trim().toLowerCase();

        return inventory.stream()
                // Example: searching for "bl" can match "Blue" or "Black".
                .filter(vehicle -> vehicle.getColor().toLowerCase().contains(search))
                .toList();
    }

    /**
     * Finds all vehicles with mileage between the given minimum and maximum mileage.
     */
    public List<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        return inventory.stream()
                // Keep only vehicles where the odometer value is inside the selected range.
                .filter(vehicle -> vehicle.getOdometer() >= minMileage && vehicle.getOdometer() <= maxMileage)
                .toList();
    }

    /**
     * Finds all vehicles with the selected vehicle type.
     */
    public List<Vehicle> getVehiclesByType(VehicleType vehicleType) {
        return inventory.stream()
                // Enums can safely be compared with ==.
                .filter(vehicle -> vehicle.getVehicleType() == vehicleType)
                .toList();
    }

    /**
     * Adds a vehicle to the inventory.
     *
     * Before adding the vehicle, the method checks whether another vehicle
     * with the same VIN already exists. VIN should be unique.
     */
    public void addVehicle(Vehicle vehicle) {
        if (findVehicleByVin(vehicle.getVin()).isPresent()) {
            throw new IllegalArgumentException("A vehicle with VIN " + vehicle.getVin() + " already exists.");
        }

        inventory.add(vehicle);
    }

    /**
     * Removes a vehicle from the inventory based on its VIN.
     *
     * @return true if a vehicle was removed, false if no vehicle with that VIN was found.
     */
    public boolean removeVehicleByVin(int vin) {
        return inventory.removeIf(vehicle -> vehicle.getVin() == vin);
    }

    /**
     * Searches for a vehicle by VIN.
     *
     * Optional is used because the vehicle may or may not exist.
     */
    public Optional<Vehicle> findVehicleByVin(int vin) {
        return inventory.stream()
                // Find the vehicle with the matching VIN.
                .filter(vehicle -> vehicle.getVin() == vin)
                .findFirst();
    }

    /**
     * Converts the dealership information to a pipe-separated line.
     *
     * This can be used as the first line in the CSV file,
     * before saving the vehicle inventory.
     */
    public String toCsvHeaderLine() {
        return String.join("|", name, address, phone);
    }
}

