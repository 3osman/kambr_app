package com.kambr.challenge.repo;

import com.kambr.challenge.dto.FlightResponse;
import com.kambr.challenge.dto.FlightResponseWithData;
import com.kambr.challenge.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findById(String id);
    List<FlightResponse> findByIdIn(List<String> ids);
    List<FlightResponseWithData> findWithDataByIdIn(List<String> ids);
//    @Query("SELECT p FROM Flight p FETCH ALL PROPERTIES WHERE p.id IN (:ids)")
//    List<Flight> findByIdAndFetchCabinsAndClassesEagerly(@Param("ids") List<String> ids);
}
