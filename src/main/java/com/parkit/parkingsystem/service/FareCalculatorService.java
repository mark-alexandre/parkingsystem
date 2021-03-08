package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.parkit.parkingsystem.util.RoundUtil.roundAt2Decimals;

public class FareCalculatorService {
    private final TicketDAO ticketDAO;
    double paidDuration;
    private static final Logger logger = LogManager.getLogger("FareCalculatorService");

    public FareCalculatorService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

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
        boolean isRecurrentMember = false; // Default value in case of failure
        try {
            isRecurrentMember = ticketDAO.isRecurrentMember(ticket.getVehicleRegNumber());
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("An error has occurred during the database process", e);
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (isRecurrentMember) {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.CAR_RATE_PER_HOUR * 0.95));
                } else {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.CAR_RATE_PER_HOUR));
                }
                break;
            }
            case BIKE: {
                if (isRecurrentMember) {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.BIKE_RATE_PER_HOUR * 0.95));
                } else {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.BIKE_RATE_PER_HOUR));
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}