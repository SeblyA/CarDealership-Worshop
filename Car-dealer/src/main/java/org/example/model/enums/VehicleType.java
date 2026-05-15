package org.example.model.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum VehicleType {
    CAR("Car"),
    TRUCK("Truck"),
    SUV("SUV"),
    VAN("Van"),
    ELECTRIC("Eletric");
    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Optional<VehicleType> fromString(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }

        String normalizedInput = input.trim().replace(" ", "_");

        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(normalizedInput)
                        || type.displayName.equalsIgnoreCase(input.trim()))
                .findFirst();
    }

    public static String getAllowedValuesText() {
        return Arrays.stream(values())
                .map(VehicleType::getDisplayName)
                .collect(Collectors.joining(", "));
    }
}

