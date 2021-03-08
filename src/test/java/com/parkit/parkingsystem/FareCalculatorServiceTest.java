package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.utils.FareCalculatorCustomArgumentProvider;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Date;

public class FareCalculatorServiceTest {
    private static final Logger logger = LogManager.getLogger("FareCalculatorServiceTest");
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private Date inTime;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService(new TicketDAO());
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
    @DisplayName("Calculate fare for bike or car for several durations")
    @ArgumentsSource(FareCalculatorCustomArgumentProvider.class)
    public void calculateFareCarOrBikeAndForSeveralDurations(int minutes, double expected, ParkingType meansOfLocomotion) throws SQLException, ClassNotFoundException {
        String meansOfLocomotionLabel = (meansOfLocomotion == ParkingType.CAR) ? "CAR" : "BIKE";

        Date outTime = DateUtils.addMinutes(inTime, minutes);
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
    @DisplayName("calculateFare method should throw an IllegalArgumentException if outTime is before inTime")
    public void calculateFareWithOutTimeBeforeInTimeShouldThrowIllegalArgumentException() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(DateUtils.addMinutes(inTime, -600));
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("calculateFare method should throw an AssertionError if outTime isn't set")
    public void calculateFareWithOutTimeEqualToNullShouldThrowAssertionError() {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(AssertionError.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("calculateFare method should throw a NullPointerException if parkingType isn't set")
    public void calculateFareWithAnUnknownParkingTypeShouldThrowNullPointerException() {
        Date outTime = DateUtils.addMinutes(this.inTime, 90);
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

}