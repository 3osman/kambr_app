package com.kambr.challenge.service;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.model.enums.ClassType;
import com.kambr.challenge.repo.CabinRepository;
import com.kambr.challenge.repo.FlightClassRepository;
import com.kambr.challenge.repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;


@Service
public class FlightClassService {
    @Autowired
    private FlightClassRepository flightClassRepo;

    @Autowired
    private CabinRepository cabinRepo;

    @Autowired
    private FlightRepository flightRepo;

    @Transactional
    public FlightClass createFlightClass(String cabinType, double price, int seatsSold, double revenue, Cabin cabin) throws PersistenceException {
        FlightClass a = new FlightClass(cabinType, price, seatsSold, revenue, cabin);
        flightClassRepo.save(a);
        return a;
    }

    @Transactional
    public FlightClass updateFlightClass(String flightId, String cabin, ClassType flightClass, double newPrice) throws PersistenceException{
        FlightClass fc = null;
        Flight f = flightRepo.findById(flightId).orElse(null);
        List<Cabin> c = cabinRepo.findAllByFlightAndCabinType(f, CabinType.valueOf(cabin));
        if (c.size() != 0) {
            fc = flightClassRepo.findByCabinAndClassType(c.get(0), flightClass).orElse(null);
            if (fc != null) {
                fc.setPrice(newPrice);
                flightClassRepo.save(fc);
            }
        }
        return fc;
    }
}
