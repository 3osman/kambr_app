package com.kambr.challenge.dto;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.enums.AircraftType;
import java.util.Date;
import java.util.List;
public interface FlightResponseWithData {

    public String getId();
    public String getOrigin();
    public String getDestination();
    public Date getDepartureDate();
    public String getFlightNumber();
    public long getDepartureTime();
    public Date getArrivalDate();
    public long getArrivalTime();
    public AircraftType getAircraft();
    public List<Cabin> getCabins();

}
