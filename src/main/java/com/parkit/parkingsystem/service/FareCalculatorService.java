package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import static com.parkit.parkingsystem.util.RoundUtil.roundAt2Decimals;

public class FareCalculatorService {
    double paidDuration;

    private double calculatePaidDuration(Ticket ticket) {
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double freeParkingDuration = 0.5; // 30 minutes converted in hour
        double initialDuration = (outHour - inHour) / (3600 * 1000);
        this.paidDuration = initialDuration - freeParkingDuration;

        if (this.paidDuration < 0) return 0;
        if (this.paidDuration < 1) return 1;
        return Math.ceil(this.paidDuration*4)/4f; // Round the paid duration to the next quarter
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            assert ticket.getOutTime() != null;
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        this.paidDuration = this.calculatePaidDuration(ticket);

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(roundAt2Decimals(paidDuration * Fare.CAR_RATE_PER_HOUR));
                break;
            }
            case BIKE: {
                ticket.setPrice(roundAt2Decimals(paidDuration * Fare.BIKE_RATE_PER_HOUR));
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}