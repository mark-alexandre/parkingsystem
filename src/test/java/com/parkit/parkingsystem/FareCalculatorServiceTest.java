package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.utils.FareCalculatorCustomArgumentProvider;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {
    private static final Logger logger = LogManager.getLogger("FareCalculatorServiceTest");
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private Date inTime;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        inTime = new Date();
    }

    /**
     * Values used for minutes parameter : {0, 29, 30, 31, 60, 90, 91, 104, 105, 106, 360, 720}
     * Values used for meansOfLocomotion : {ParkingType.CAR, ParkingType.BIKE}
     */
    @ParameterizedTest
    @ArgumentsSource(FareCalculatorCustomArgumentProvider.class)
    public void calculateFareCarOrBikeAndForSeveralDurations(int minutes, double expected, ParkingType meansOfLocomotion) {
        String meansOfLocomotionLabel = (meansOfLocomotion == ParkingType.CAR) ? "CAR" : "BIKE";

        Date outTime = DateUtils.addMinutes(this.inTime, minutes);
        ParkingSpot parkingSpot = new ParkingSpot(1, meansOfLocomotion, false);

        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        fareCalculatorService.calculateFare(ticket);

        assertEquals(expected, ticket.getPrice());

        logger.info(
                meansOfLocomotionLabel + "'s test for a duration of " +
                        minutes + " minutes: EXPECTED: " + expected + "€ - ACTUAL: " + ticket.getPrice() + "€."
        );
    }

    @Test
    public void calculateFareUnknownType() {
        Date inTime = new Date(), outTime = DateUtils.addMinutes(inTime, 90);
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

}
