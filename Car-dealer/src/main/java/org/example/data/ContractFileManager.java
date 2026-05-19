package org.example.data;

import org.example.model.*;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;

public class ContractFileManager {
    private static final Path CONTRACTS_PATH = Path.of("src/contract.csv");

    public ArrayList<Contract> getContract() {
        ArrayList<Contract> contracts = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(CONTRACTS_PATH)) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }
                Contract contract = createContractFromCsv(line);
                contracts.add(contract);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read contract file");
        }

        return contracts;

    }

    public void saveContract(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null.");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(CONTRACTS_PATH, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            writer.write(contract.toCsvLine());
            writer.newLine();

        } catch (IOException e) {
            throw new IllegalStateException("Could not save contract file: " + CONTRACTS_PATH.toAbsolutePath(), e);
        }
    }

    private void ensureContractFileExists() throws IOException {

        if (!Files.exists(ContractFileManager.CONTRACTS_PATH)) {
            Files.createFile(ContractFileManager.CONTRACTS_PATH);
        }
    }

    //Create and break it to usable data
//convert text to object using parse
    private Contract createContractFromCsv(String line) {
        String[] p = line.split("\\|");
        String type = p[0];

        String contractType = p[0];
        String date = p[1];
        String name = p[2];
        String email = p[3];
        int vin = Integer.parseInt(p[4]);
        int year = Integer.parseInt(p[5]);
        String make = p[6];
        String model = p[7];
        String vehicleType = p[8];
        String color = p[9];
        int odometer = Integer.parseInt(p[10]);
        double price = Double.parseDouble(p[11]);
        boolean isFinanced = p[16].equalsIgnoreCase("YES");
        Vehicle v = new Vehicle(

                vin,
                year,
                make,
                model,
                vehicle,
                color,
                odometer,
                price
        );
//create object
        if (contractType.equalsIgnoreCase("SALE")) {
            return new SalesContract(date, name, email, vehicle, isFinanced);
        }

    }
}



