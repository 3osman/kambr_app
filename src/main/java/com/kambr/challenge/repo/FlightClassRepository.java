package com.kambr.challenge.repo;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.FlightClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface FlightClassRepository extends JpaRepository<FlightClass, Long> {
    FlightClass findAllByCabinAndClassType(Cabin c, String classType);
    @Modifying
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("update FlightClass u set u.price = ?1 where u.id = ?2")
    void setFlightClassPriceWithId(double newPrice, long flightClassId);
}
