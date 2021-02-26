package com.parkit.parkingsystem.utils;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CustomArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        return Stream.of(
                arguments(0, ParkingType.CAR),
                arguments(29, ParkingType.CAR),
                arguments(30, ParkingType.CAR),
                arguments(31, ParkingType.CAR),
                arguments(60, ParkingType.CAR),
                arguments(90, ParkingType.CAR),
                arguments(91, ParkingType.CAR),
                arguments(104, ParkingType.CAR),
                arguments(105, ParkingType.CAR),
                arguments(106, ParkingType.CAR),
                arguments(360, ParkingType.CAR),
                arguments(720, ParkingType.CAR),
                arguments(0, ParkingType.BIKE),
                arguments(29, ParkingType.BIKE),
                arguments(30, ParkingType.BIKE),
                arguments(31, ParkingType.BIKE),
                arguments(60, ParkingType.BIKE),
                arguments(90, ParkingType.BIKE),
                arguments(91, ParkingType.BIKE),
                arguments(104, ParkingType.BIKE),
                arguments(105, ParkingType.BIKE),
                arguments(106, ParkingType.BIKE),
                arguments(180, ParkingType.BIKE),
                arguments(360, ParkingType.BIKE),
                arguments(720, ParkingType.BIKE)
        );
    }
}
