package com.kambr.challenge.service;

import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightMetadata;
import com.kambr.challenge.repo.FlightMetadataRepository;
import com.kambr.challenge.repo.FlightRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightMetaDataService {
    @Autowired
    private FlightMetadataRepository flightRepo;

    @Autowired
    private FlightRepository flightsRepo;

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

    public List<Flight> findByQuery(QueryBuilder query, Pageable pageable) {
        List<FlightMetadata> metadata = flightRepo.search(query, pageable).getContent();
        return flightsRepo.findByIdIn(metadata.stream().map(f -> f.getId()).collect(Collectors.toList()));
    }
}
