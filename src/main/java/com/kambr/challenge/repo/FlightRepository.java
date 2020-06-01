package com.kambr.challenge.repo;

import com.kambr.challenge.dto.FlightResponse;
import com.kambr.challenge.dto.FlightResponseWithData;
import com.kambr.challenge.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<FlightResponse> findByIdIn(List<String> ids);
    List<FlightResponseWithData> findWithDataByIdIn(List<String> ids);
    Flight findById(String id);
//    @Query("SELECT p FROM Flight p FETCH ALL PROPERTIES WHERE p.id IN (:ids)")
//    List<Flight> findByIdAndFetchCabinsAndClassesEagerly(@Param("ids") List<String> ids);
}
