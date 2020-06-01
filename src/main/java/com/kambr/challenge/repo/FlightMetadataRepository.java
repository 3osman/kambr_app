package com.kambr.challenge.repo;

import com.kambr.challenge.model.FlightMetadata;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.Optional;

public interface FlightMetadataRepository extends ElasticsearchRepository<FlightMetadata, String> {
    Optional<FlightMetadata> findById(String id);
    Page<FlightMetadata> findAll();
    Page<FlightMetadata> search(QueryBuilder ex, Pageable pageable);
}