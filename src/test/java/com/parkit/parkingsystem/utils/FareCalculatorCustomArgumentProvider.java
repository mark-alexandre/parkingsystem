package com.parkit.parkingsystem.utils;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class FareCalculatorCustomArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        return Stream.of(
                arguments(0, 0, ParkingType.CAR),
                arguments(29, 0, ParkingType.CAR),
                arguments(30, 1.5, ParkingType.CAR),
                arguments(31, 1.5, ParkingType.CAR),
                arguments(60, 1.5, ParkingType.CAR),
                arguments(90, 1.5, ParkingType.CAR),
                arguments(91, 1.88, ParkingType.CAR),
                arguments(104, 1.88, ParkingType.CAR),
                arguments(105, 1.88, ParkingType.CAR),
                arguments(106, 2.25, ParkingType.CAR),
                arguments(360, 8.25, ParkingType.CAR),
                arguments(720, 17.25, ParkingType.CAR),
                arguments(0, 0, ParkingType.BIKE),
                arguments(29, 0, ParkingType.BIKE),
                arguments(30, 1, ParkingType.BIKE),
                arguments(31, 1, ParkingType.BIKE),
                arguments(60, 1, ParkingType.BIKE),
                arguments(90, 1, ParkingType.BIKE),
                arguments(91, 1.25, ParkingType.BIKE),
                arguments(104, 1.25, ParkingType.BIKE),
                arguments(105, 1.25, ParkingType.BIKE),
                arguments(106, 1.5, ParkingType.BIKE),
                arguments(360, 5.5, ParkingType.BIKE),
                arguments(720, 11.5, ParkingType.BIKE)
        );
    }
}
