package com.kambr.challenge.service;

import com.kambr.challenge.dto.FlightResponse;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FlightClassService {
    @Autowired
    private FlightClassRepository flightClassRepo;

    @Autowired
    private CabinRepository cabinRepo;

    @Autowired
    private FlightRepository flightRepo;

    @PersistenceContext
    private EntityManager entityManager;

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
        if (f != null) {
            List<Cabin> c = cabinRepo.findAllByFlightAndCabinType(f, CabinType.valueOf(cabin));
            if (c.size() != 0) {
                fc = flightClassRepo.findByCabinAndClassType(c.get(0), flightClass).orElse(null);
                if (fc != null) {
                    fc.setPrice(newPrice);
                    flightClassRepo.save(fc);
                }
            }
        }
        return fc;
    }

    @Transactional
    public List<FlightClass> updateFlightClass(List<String> flightIds, String cabin, ClassType flightClass, double newPrice) throws PersistenceException{
        List<FlightResponse> flights = flightRepo.findByIdIn(flightIds);
        List<FlightClass> actualFlightClasses = new ArrayList<>();
        if (flights != null) {
            List<Cabin> c = cabinRepo.findCabinsByCabinTypeAndFlightIdIn(CabinType.valueOf(cabin), flights.stream().map(s -> s.getId()).collect(Collectors.toList())).orElse(null);
            if (c != null) {
                actualFlightClasses = flightClassRepo.findByClassTypeAndCabinIdIn(flightClass, c.stream().map(s -> s.getId()).collect(Collectors.toList())).orElse(null);
                flightClassRepo.updatePrice(c, flightClass, newPrice);
                if (actualFlightClasses != null) {
                    for (FlightClass element : actualFlightClasses) {
                        entityManager.refresh(element);
                    }
                }
            }
        }
        return actualFlightClasses;
    }
}
