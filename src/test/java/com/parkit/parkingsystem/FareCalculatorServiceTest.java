package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.utils.CustomArgumentProvider;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static com.parkit.parkingsystem.util.Round.roundAt2Decimals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

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
     * Values used for minutes parameter : {0, 29, 30, 31, 60, 90, 91, 104, 105, 106, 180, 360, 720}
     * Values used for meansOfLocomotion : {ParkingType.CAR, ParkingType.BIKE}
     */
    @ParameterizedTest(name = "Calculate car's and bike's parking fare for different durations")
    @ArgumentsSource(CustomArgumentProvider.class)
    public void calculateFareCarOrBikeAndForSeveralDurations(int minutes, ParkingType meansOfLocomotion) {
        String meansOfLocomotionLabel = (meansOfLocomotion == ParkingType.CAR) ? "CAR" : "BIKE";

        Date outTime = DateUtils.addMinutes(this.inTime, minutes);
        ParkingSpot parkingSpot = new ParkingSpot(1, meansOfLocomotion, false);

        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        fareCalculatorService.calculateFare(ticket);
        double paidDuration = fareCalculatorService.calculatePaidDuration(ticket);

        if ("CAR".equals(meansOfLocomotionLabel)) {
            assertEquals(ticket.getPrice(), roundAt2Decimals(Fare.CAR_RATE_PER_HOUR * paidDuration));
        } else {
            assertEquals(ticket.getPrice(), roundAt2Decimals(Fare.BIKE_RATE_PER_HOUR * paidDuration));
        }

        System.out.println(
                meansOfLocomotionLabel + "'s test for a duration of " +
                        minutes + " minutes ===> Parking Fare is equal to " + ticket.getPrice() + "€."
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
