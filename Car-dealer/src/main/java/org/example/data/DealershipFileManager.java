package org.example.data;


import org.example.model.Dealership;
import org.example.model.Vehicle;
import org.example.model.enums.VehicleType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
     * Handles loading and saving dealership inventory data from and to a CSV file.
     *
     * This class is responsible for:
     * - Reading dealership information from inventory.csv
     * - Reading vehicle information from inventory.csv
     * - Saving dealership and vehicle information back to inventory.csv
     * - Creating a backup before overwriting the file
     *
     * This keeps file handling separate from the business logic in the Dealership class.
     */
    public class DealershipFileManager {

        private static final Path INVENTORY_PATH = Path.of("/Applications/Pluralsight/workbook5/Car-dealer/src/main/resources/inventory.csv");

        // Regex delimiter for splitting pipe-separated CSV lines.
        // The pipe symbol | has special meaning in regex, so it must be escaped.
        private static final String DELIMITER = "\\|";

        private static final int DEALERSHIP_FIELD_COUNT = 3;
        private static final int VEHICLE_FIELD_COUNT = 8;

        /**
         * Loads the dealership and all vehicles from the inventory file.
         *
         * Expected first line:
         * name|address|phone
         *
         * Expected vehicle lines:
         * vin|year|make|model|type|color|odometer|price
         *
         * @return a Dealership object containing all vehicles from the file
         */
        public Dealership getDealership() {
            ensureInventoryFileExists();

            try (BufferedReader reader = Files.newBufferedReader(INVENTORY_PATH)) {
                String dealershipLine = reader.readLine();

                if (dealershipLine == null || dealershipLine.isBlank()) {
                    throw new IllegalStateException("The inventory file is empty.");
                }

                Dealership dealership = parseDealership(dealershipLine);

                String vehicleLine;

                while ((vehicleLine = reader.readLine()) != null) {
                    if (vehicleLine.isBlank()) {
                        continue;
                    }

                    Vehicle vehicle = parseVehicle(vehicleLine);
                    dealership.addVehicle(vehicle);
                }

                return dealership;

            } catch (IOException e) {
                throw new IllegalStateException("Could not read inventory file: " + INVENTORY_PATH.toAbsolutePath(), e);
            }
        }

        /**
         * Saves the dealership and all vehicles to the inventory file.
         *
         * A backup file is created before the original file is overwritten.
         *
         * @param dealership the dealership that should be saved
         */
        public void saveDealership(Dealership dealership) {
            if (dealership == null) {
                throw new IllegalArgumentException("Dealership cannot be null.");
            }

            ensureInventoryFileExists();
            createBackupFile();

            try (BufferedWriter writer = Files.newBufferedWriter(INVENTORY_PATH)) {
                writer.write(dealership.toCsvHeaderLine());
                writer.newLine();

                List<Vehicle> vehicles = dealership.getAllVehicles();

                for (Vehicle vehicle : vehicles) {
                    writer.write(vehicle.toCsvLine());
                    writer.newLine();
                }

            } catch (IOException e) {
                throw new IllegalStateException("Could not save inventory file: " + INVENTORY_PATH.toAbsolutePath(), e);
            }
        }

        /**
         * Converts the first CSV line into a Dealership object.
         *
         * Expected format:
         * name|address|phone
         *
         * @param dealershipLine the first line from the inventory file
         * @return a Dealership object
         */
        private Dealership parseDealership(String dealershipLine) {
            String[] fields = splitCsvLine(dealershipLine, DEALERSHIP_FIELD_COUNT, "dealership");

            String name = fields[0].trim();
            String address = fields[1].trim();
            String phone = fields[2].trim();

            validateRequiredField(name, "Dealership name", dealershipLine);
            validateRequiredField(address, "Dealership address", dealershipLine);
            validateRequiredField(phone, "Dealership phone", dealershipLine);

            return new Dealership(name, address, phone);
        }

        /**
         * Converts one CSV vehicle line into a Vehicle object.
         *
         * Expected format:
         * vin|year|make|model|type|color|odometer|price
         *
         * @param vehicleLine one line from the inventory file
         * @return a Vehicle object
         */
        private Vehicle parseVehicle(String vehicleLine) {
            String[] fields = splitCsvLine(vehicleLine, VEHICLE_FIELD_COUNT, "vehicle");

            try {
                int vin = parseInteger(fields[0], "VIN", vehicleLine);
                int year = parseInteger(fields[1], "year", vehicleLine);
                String make = fields[2].trim();
                String model = fields[3].trim();
                String typeText = fields[4].trim();
                String color = fields[5].trim();
                int odometer = parseInteger(fields[6], "odometer", vehicleLine);
                double price = parseDouble(fields[7], "price", vehicleLine);

                validateRequiredField(make, "Make", vehicleLine);
                validateRequiredField(model, "Model", vehicleLine);
                validateRequiredField(typeText, "Vehicle type", vehicleLine);
                validateRequiredField(color, "Color", vehicleLine);

                VehicleType vehicleType = VehicleType.fromString(typeText)
                        .orElseThrow(() -> new IllegalStateException(
                                "Invalid vehicle type '" + typeText + "' in vehicle line: " + vehicleLine
                        ));

                return new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);

            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid number in vehicle line: " + vehicleLine, e);
            }
        }

        /**
         * Splits a CSV line and validates the expected number of fields.
         *
         * The -1 keeps empty values at the end of the line.
         * Without -1, Java removes trailing empty fields.
         *
         * Example:
         * "123|2020|Ford|" keeps the last empty field.
         *
         * @param line the CSV line
         * @param expectedFieldCount the expected number of fields
         * @param lineType describes the type of line for error messages
         * @return the split fields
         */
        private String[] splitCsvLine(String line, int expectedFieldCount, String lineType) {
            String[] fields = line.split(DELIMITER, -1);

            if (fields.length != expectedFieldCount) {
                throw new IllegalStateException(
                        "Invalid " + lineType + " line. Expected "
                                + expectedFieldCount + " fields, but found "
                                + fields.length + ": " + line
                );
            }

            return fields;
        }

        /**
         * Parses a required integer field.
         *
         * @param value the text value from the CSV file
         * @param fieldName the name of the field for error messages
         * @param originalLine the original CSV line
         * @return the parsed integer
         */
        private int parseInteger(String value, String fieldName, String originalLine) {
            String trimmedValue = value.trim();

            validateRequiredField(trimmedValue, fieldName, originalLine);

            try {
                return Integer.parseInt(trimmedValue);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(
                        "Invalid integer for " + fieldName + ": '" + trimmedValue + "' in line: " + originalLine
                );
            }
        }

        /**
         * Parses a required decimal number field.
         *
         * @param value the text value from the CSV file
         * @param fieldName the name of the field for error messages
         * @param originalLine the original CSV line
         * @return the parsed double
         */
        private double parseDouble(String value, String fieldName, String originalLine) {
            String trimmedValue = value.trim();

            validateRequiredField(trimmedValue, fieldName, originalLine);

            try {
                return Double.parseDouble(trimmedValue);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(
                        "Invalid decimal number for " + fieldName + ": '" + trimmedValue + "' in line: " + originalLine
                );
            }
        }

        /**
         * Validates that a required field is not empty.
         *
         * @param value the value to validate
         * @param fieldName the name of the field for error messages
         * @param originalLine the original CSV line
         */
        private void validateRequiredField(String value, String fieldName, String originalLine) {
            if (value == null || value.isBlank()) {
                throw new IllegalStateException(
                        fieldName + " is required in line: " + originalLine
                );
            }
        }

        /**
         * Verifies that the inventory file exists.
         *
         * This method fails fast with a clear error message if the file is missing.
         */
        private void ensureInventoryFileExists() {
            if (!Files.exists(INVENTORY_PATH)) {
                throw new IllegalStateException("Inventory file not found at: " + INVENTORY_PATH.toAbsolutePath());
            }
        }

        /**
         * Creates a timestamped backup copy of the inventory file.
         *
         * Example:
         * inventory-20260514-211530.csv
         */
        private void createBackupFile() {
            try {
                Path backupDirectory = INVENTORY_PATH.getParent().resolve("backups");
                Files.createDirectories(backupDirectory);

                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

                Path backupPath = backupDirectory.resolve("inventory-" + timestamp + ".csv");

                Files.copy(INVENTORY_PATH, backupPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                throw new IllegalStateException("Could not create inventory backup file.", e);
            }
        }
    }

