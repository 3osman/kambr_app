package com.kambr.challenge.util;

import java.util.Date;
import java.util.Objects;

public class FlightUtils {

    public FlightUtils() {
    }

    public String getFlightId(String origin, String destination, Date departureDate, String flightNumber){
        return Objects.hash(new String[]{origin, destination, departureDate.toString(), flightNumber})+""; //simple hashing to id
    }

}
