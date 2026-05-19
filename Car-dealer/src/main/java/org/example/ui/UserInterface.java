package org.example.ui;

import java.util.List;

import org.example.data.DealershipFileManager;
import org.example.model.*;
import org.example.model.enums.VehicleType;
import org.example.ui.enums.MenuOption;

import static java.nio.file.Files.readString;
import static org.example.ui.Helper.*;

public class UserInterface {
    private final DealershipFileManager fileManager;
    private Dealership dealership;

    public UserInterface() {
        this.fileManager = new DealershipFileManager();
    }

    public void display() {
        init();

        MenuOption selectedOption;

        do {
            displayHeader();
            displayMenu();

            int choice = readInt("Choose an option: ");
            selectedOption = MenuOption.fromCode(choice).orElse(null);

            handleMenuChoice(selectedOption);

        } while (selectedOption != MenuOption.QUIT);

        System.out.println("Goodbye!");
    }

    private void init() {
        this.dealership = fileManager.getDealership();
    }

    private void displayHeader() {
        System.out.println();


        System.out.println(" Phone: " + dealership.getPhone());
        System.out.println("------------------------------------------------------------");
        System.out.println(" Manage inventory | Search vehicles | Add & remove cars");
        System.out.println("============================================================");
    }

    private void displayMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%-3d - %s%n", option.getCode(), option.getLabel());
        }

        System.out.println();
    }

    private void handleMenuChoice(MenuOption option) {
        if (option == null) {
            System.out.println("Invalid option. Please try again.");
            pause();
            return;
        }

        switch (option) {
            case FIND_BY_PRICE -> processGetByPriceRequest();
            case FIND_BY_MAKE_MODEL -> processGetByMakeModelRequest();
            case FIND_BY_YEAR -> processGetByYearRequest();
            case FIND_BY_COLOR -> processGetByColorRequest();
            case FIND_BY_MILEAGE -> processGetByMileageRequest();
            case FIND_BY_TYPE -> processGetByVehicleTypeRequest();
            case LIST_ALL -> processAllVehiclesRequest();
            case ADD_VEHICLE -> processAddVehicleRequest();
            case REMOVE_VEHICLE -> processRemoveVehicleRequest();
            case QUIT -> {
                // No action needed. The loop will end.
            }
        }
    }

    private void processGetByPriceRequest() {
        double minPrice = readPositiveDouble("Minimum price: ");
        double maxPrice = readPositiveDouble("Maximum price: ");

        if (minPrice > maxPrice) {
            System.out.println("Minimum price cannot be higher than maximum price.");
            pause();
            return;
        }

        displayVehicles(Dealership.getVehiclesByPrice(minPrice, maxPrice));
    }

    private void processGetByMakeModelRequest() {
        String make = readString("Make, leave empty for any: ");
        String model = readString("Model, leave empty for any: ");

        displayVehicles(Dealership.getVehiclesByMakeModel(make, model));
    }

    private void processGetByYearRequest() {
        int minYear = readYear("Minimum year: ");
        int maxYear = readYear("Maximum year: ");

        if (minYear > maxYear) {
            System.out.println("Minimum year cannot be higher than maximum year.");
            pause();
            return;
        }

        displayVehicles(Dealership.getVehiclesByYear(minYear, maxYear));
    }

    private void processGetByColorRequest() {
        String color = readRequiredString("Color: ");

        displayVehicles(Dealership.getVehiclesByColor(color));
    }

    private void processGetByMileageRequest() {
        int minMileage = readPositiveInt("Minimum mileage: ");
        int maxMileage = readPositiveInt("Maximum mileage: ");

        if (minMileage > maxMileage) {
            System.out.println("Minimum mileage cannot be higher than maximum mileage.");
            pause();
            return;
        }

        displayVehicles(Dealership.getVehiclesByMileage(minMileage, maxMileage));
    }

    private void processGetByVehicleTypeRequest() {
        VehicleType vehicleType = readVehicleType(
                "Vehicle type (" + VehicleType.getAllowedValuesText() + "): "
        );

        displayVehicles(Dealership.getVehiclesByType(vehicleType));
    }

    private void processAllVehiclesRequest() {
        displayVehicles(Dealership.getAllVehicles());
    }

    private void processAddVehicleRequest() {
        System.out.println();
        System.out.println("Add a new vehicle");

        int vin = readPositiveInt("VIN: ");

        if (Dealership.findVehicleByVin(vin).isPresent()) {
            System.out.println("A vehicle with this VIN already exists. Vehicle was not added.");
            pause();
            return;
        }

        int year = readYear("Year: ");
        String make = readRequiredString("Make: ");
        String model = readRequiredString("Model: ");

        VehicleType vehicleType = readVehicleType(
                "Vehicle type (" + VehicleType.getAllowedValuesText() + "): "
        );

        String color = readRequiredString("Color: ");
        int odometer = readPositiveInt("Odometer: ");
        double price = readPositiveDouble("Price: ");

        Vehicle vehicle = new Vehicle(
                vin,
                year,
                make,
                model,
                vehicleType,
                color,
                odometer,
                price
        );

        Dealership.addVehicle(vehicle);
        DealershipFileManager fileManager;
        fileManager.saveDealership(dealership);

        System.out.println("Vehicle added and inventory saved.");
        pause();
    }

    private void processRemoveVehicleRequest() {
        int vin = readPositiveInt("Enter VIN of vehicle to remove: ");

        Dealership.findVehicleByVin(vin).ifPresentOrElse(
                vehicle -> confirmAndRemoveVehicle(vehicle, vin),
                () -> {
                    System.out.println("No vehicle found with VIN " + vin + ".");
                    pause();
                }
        );
    }

    private void confirmAndRemoveVehicle(Vehicle vehicle, int vin) {
        System.out.println("Vehicle found:");
        displayVehiclesWithoutPause(List.of(vehicle));

        String confirmation = readString("Remove this vehicle? yes/no: ");

        if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
            Dealership.removeVehicleByVin(vin);

            fileManager.saveDealership(Dealership);

            System.out.println("Vehicle removed and inventory saved.");
        } else {
            System.out.println("Remove cancelled.");
        }

        pause();
    }

    private VehicleType readVehicleType(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);

            var vehicleType = VehicleType.fromString(input);

            if (vehicleType.isPresent()) {
                return vehicleType.get();
            }

            System.out.println("Invalid vehicle type. Allowed values: " + VehicleType.getAllowedValuesText());
        }
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        displayVehiclesWithoutPause(vehicles);
        pause();
    }

    private void displayVehiclesWithoutPause(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println();
        System.out.printf(
                "%-8s %-6s %-12s %-15s %-8s %-10s %10s %11s%n",
                "VIN",
                "YEAR",
                "MAKE",
                "MODEL",
                "TYPE",
                "COLOR",
                "ODOMETER",
                "PRICE"
        );

        System.out.println("---------------------------------------------------------------------------------------");

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Total vehicles: " + vehicles.size());
    }

    private void pause() {
        System.out.println();
        readString("Press Enter to continue...");
    }
}




