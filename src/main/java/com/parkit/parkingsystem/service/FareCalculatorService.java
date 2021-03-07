package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.PropertyFileReading;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.parkit.parkingsystem.util.RoundUtil.roundAt2Decimals;

public class FareCalculatorService {
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");
    private static final String[] dbInfo = PropertyFileReading.getDbInfo();
    private static final TicketDAO ticketDAO = new TicketDAO();
    double paidDuration;

    private Boolean isRecurrentMember(String regNumber) {
        try {
            Connection con = null;
            con = new DataBaseConfig().getConnection();

            // create the java statement
            Statement st = con.createStatement();

            // execute the query, and get the result
            ResultSet rs = st.executeQuery(DBConstants.GET_TICKET_BY_REG_NUMBER);

            // return true or false if a regNumber has ben found
            int count = -1;
            while (rs.next()) {
                ++count;
            }
            // close the connexion
            st.close();
            return count >= 1;
        }
        catch (Exception e) {
            logger.error("Got an exception! ");
            logger.error(e.getMessage());
        }
        return false;
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

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (isRecurrentMember(ticket.getVehicleRegNumber()) == true) {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.CAR_RATE_PER_HOUR * 0.95));
                } else {
                    ticket.setPrice(roundAt2Decimals(paidDuration * Fare.CAR_RATE_PER_HOUR));
                }
                break;
            }
            case BIKE: {
                if (isRecurrentMember(ticket.getVehicleRegNumber())) {
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