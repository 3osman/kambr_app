package com.kambr.challenge.service;

import com.kambr.challenge.model.FlightMetadata;
import com.kambr.challenge.repo.FlightMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightMetaDataService {
    @Autowired
    private FlightMetadataRepository flightRepo;

    public void setFlightRepository(FlightMetadataRepository flightRepo) {
        this.flightRepo = flightRepo;
    }

    public FlightMetadata save(FlightMetadata book) {
        return flightRepo.save(book);
    }

    public void delete(FlightMetadata book) {
        flightRepo.delete(book);
    }

    public Optional<FlightMetadata> findOne(String id) {
        return flightRepo.findById(id);
    }

    public Iterable<FlightMetadata> findAll() {
        return flightRepo.findAll();
    }

    public Page<FlightMetadata> findBy(Example<FlightMetadata> flight, PageRequest pageRequest){
        return flightRepo.findAllById(flight, pageRequest);
    }
}
