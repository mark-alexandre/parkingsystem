package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double freeParkingDuration = 0.5; // 30 minutes converted in hour
        double initialDuration = (outHour - inHour) / (3600 * 1000);
        double paidDuration = initialDuration - freeParkingDuration;

        if (paidDuration < 0) return;
        if (paidDuration < 1) paidDuration = 1;

        paidDuration = Math.ceil(paidDuration*4)/4f; // Round the paid duration to the

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(Math.round(paidDuration * Fare.CAR_RATE_PER_HOUR * 100) / 100.0);
                break;
            }
            case BIKE: {
                ticket.setPrice(Math.round(paidDuration * Fare.BIKE_RATE_PER_HOUR * 100) / 100.0);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}