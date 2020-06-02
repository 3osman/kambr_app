package com.kambr.challenge.repo;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.enums.CabinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabinRepository extends JpaRepository<Cabin, Long>{
    Page<Cabin> findAllByFlightId(String flight, Pageable pageable);
    List<Cabin> findAllByFlightAndCabinType(Flight flight, CabinType ct);
    Optional<List<Cabin>> findCabinsByCabinTypeAndFlightIdIn(CabinType ct, List<String> ids);
}
