package com.parkit.parkingsystem.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RoundUtilCustomArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        return Stream.of(
                arguments(0, 0),
                arguments(0.261, 0.26),
                arguments(0.269, 0.27),
                arguments(0.3, 0.3),
                arguments(0.444, 0.44),
                arguments(0.445, 0.45),
                arguments(0.4999999, 0.5),
                arguments(0.5099999, 0.51),
                arguments(0.682, 0.68),
                arguments(2000.1234, 2000.12)
        );
    }
}
