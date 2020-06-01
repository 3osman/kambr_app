package com.kambr.challenge.service;

import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightMetadata;
import com.kambr.challenge.repo.FlightMetadataRepository;
import com.kambr.challenge.repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.Date;

@Service
public class FlightService {
    @Autowired
    private FlightMetadataRepository flightRepo;

    @Autowired
    private FlightRepository flightCreatorRepo;

    @Transactional
    public Flight createFlight(String origin, String destination, Date departureDate, String flightNumber, Date arrivalDate, long departureTime, long arrivalTime, String aircraft) throws PersistenceException {
        Flight a = new Flight(origin, destination, departureDate, flightNumber, arrivalDate, departureTime, arrivalTime, aircraft);
        FlightMetadata flightMeta = new FlightMetadata(a.getId(), origin, destination, departureDate, flightNumber, departureTime);
        flightCreatorRepo.save(a);
        flightRepo.save(flightMeta);
        return a;
    }

    public Flight save(Flight f){
        flightCreatorRepo.save(f);
        return f;
    }
    @Transactional
    public Flight findById(String Id) {
        return flightCreatorRepo.findById(Id);
    }
}
