package com.kambr.challenge.repo;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinRepository extends JpaRepository<Cabin, Long> {
    Page<Cabin> findAllByFlight(Flight flight, Pageable pageable);
}
