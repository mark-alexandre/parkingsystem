package com.parkit.parkingsystem;

import com.parkit.parkingsystem.utils.RoundUtilCustomArgumentProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static com.parkit.parkingsystem.util.RoundUtil.roundAt2Decimals;
import static org.junit.jupiter.api.Assertions.*;

public class RoundUtilTest {
    private static final Logger logger = LogManager.getLogger("RoundUtilTest");

    @ParameterizedTest
    @ArgumentsSource(RoundUtilCustomArgumentProvider.class)
    public void RoundAt2DecimalsShouldReturnDoubleValuesWith2Decimals(double input, double expected) {
        assertEquals(expected, roundAt2Decimals(input));
        logger.info("Expected: " + expected + " - Actual: " + roundAt2Decimals(input));
    }
}
