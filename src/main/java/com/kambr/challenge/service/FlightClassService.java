package com.kambr.challenge.service;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.repo.FlightClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class FlightClassService {
    @Autowired
    private FlightClassRepository flightClassRepo;

    @Transactional
    public FlightClass createFlightClass(String cabinType, double price, int seatsSold, double revenue, Cabin cabin) throws PersistenceException {
        FlightClass a = new FlightClass(cabinType, price, seatsSold, revenue, cabin);
        flightClassRepo.save(a);
        return a;
    }

    public FlightClass updateFlightClass(long id, double newPrice) throws PersistenceException{
        FlightClass flightClass = flightClassRepo.findById(id).orElse(null);
        if (flightClass != null) {
            flightClassRepo.setFlightClassPriceWithId(newPrice, id);
        }
        return flightClass;
    }
}
