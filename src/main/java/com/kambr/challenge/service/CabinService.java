package com.kambr.challenge.service;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.repo.CabinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

@Service
public class CabinService {
    @Autowired
    private CabinRepository cabinRepo;
    @Transactional
    public Page<Cabin> findByFlight(Flight flight, PageRequest pageRequest){
        return cabinRepo.findAllByFlight(flight, pageRequest);
    }

    @Transactional
    public Cabin createCabin(Flight flight, CabinType type) throws PersistenceException {
        Cabin a = new Cabin(flight, type);
        return cabinRepo.save(a);
    }
}
