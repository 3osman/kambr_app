package com.kambr.challenge.repo;

import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.model.enums.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface FlightClassRepository extends JpaRepository<FlightClass, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FlightClass> findByCabinAndClassType(Cabin c, ClassType classType);
}
