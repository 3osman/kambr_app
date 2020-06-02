package com.kambr.challenge.repo;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.model.enums.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightClassRepository extends JpaRepository<FlightClass, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FlightClass> findByCabinAndClassType(Cabin c, ClassType classType);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<List<FlightClass>> findByClassTypeAndCabinIdIn(ClassType classType, List<Long> cabins);
    @Modifying
    @Query("UPDATE FlightClass c SET c.price = :price WHERE c.cabin IN (:cabins) AND c.classType = :type")
    int updatePrice(@Param("cabins") List<Cabin> ids, @Param("type") ClassType type, @Param("price") double newPrice);

}
