package com.kambr.challenge.util;

import java.util.Date;

public class FlightUtils {

    public FlightUtils() {
    }

    public String getFlightId(String origin, String destination, Date departureDate, String flightNumber){
        return origin + ":" + destination + ":" + departureDate.getTime() + ":" + flightNumber;
    }

}
