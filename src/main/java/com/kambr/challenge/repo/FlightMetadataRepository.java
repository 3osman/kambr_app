package com.kambr.challenge.repo;

import com.kambr.challenge.model.FlightMetadata;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface FlightMetadataRepository extends ElasticsearchRepository<FlightMetadata, String> {
    Optional<FlightMetadata> findById(String id);
    Page<FlightMetadata> findAll();
    @Cacheable("flight_meta")
    Page<FlightMetadata> search(QueryBuilder ex, Pageable pageable);
}