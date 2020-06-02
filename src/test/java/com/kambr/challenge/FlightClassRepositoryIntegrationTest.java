package com.kambr.challenge;


import com.kambr.challenge.dto.FlightResponse;
import com.kambr.challenge.dto.FlightResponseWithData;
import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.model.enums.ClassType;
import com.kambr.challenge.repo.CabinRepository;
import com.kambr.challenge.repo.FlightClassRepository;
import com.kambr.challenge.repo.FlightRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FlightClassRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightClassRepository fcRepository;

    @Test
    public void whenFindByCabinAndClassType_thenReturnClass() {
        // given
        Date dd = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Flight f = new Flight(
                "test_origin",
                "test_destination",
                new Date(),
                "test_flight_number",
                dd,
                12,
                12,
                "ONE"
        );
        Cabin c = new Cabin(f, CabinType.C);
        Cabin d = new Cabin(f, CabinType.A);
        Cabin r = new Cabin(f, CabinType.B);
        FlightClass fc = new FlightClass("A",2,2,12, r);
        entityManager.persist(f);
        entityManager.flush();

        // when
        FlightClass found = fcRepository.findByCabinAndClassType(r,ClassType.A).orElse(null);

        // then
        assertThat(found)
                .isNotNull();

        // then
        assertThat(found.getId())
                .isEqualTo(fc.getId());
    }

    @Test
    public void whenFindByClassTypeAndCabinIdIn_thenReturnClasses() {
        Date dd = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Flight f = new Flight(
                "test_origin",
                "test_destination",
                new Date(),
                "test_flight_number",
                dd,
                12,
                12,
                "ONE"
        );
        Cabin c = new Cabin(f, CabinType.C);
        Cabin d = new Cabin(f, CabinType.A);
        Cabin r = new Cabin(f, CabinType.B);
        FlightClass fc = new FlightClass("A",2,2,12, r);

        Flight f2 = new Flight(
                "test_origin",
                "test_destination",
                new Date(),
                "test_flight_number",
                dd,
                12,
                12,
                "ONE"
        );
        Cabin cc = new Cabin(f2, CabinType.C);
        Cabin d2 = new Cabin(f2, CabinType.A);
        Cabin rr = new Cabin(f2, CabinType.B);
        FlightClass fc2 = new FlightClass("A",2,2,12, rr);
        entityManager.persist(f);
        entityManager.persist(f2);
        entityManager.flush();
        List<Long> cabins = new ArrayList<>();
        cabins.add(r.getId());
        cabins.add(rr.getId());
        // when
        List<FlightClass> found = fcRepository.findByClassTypeAndCabinIdIn(ClassType.A, cabins).orElse(null);

        // then
        assertThat(found)
                .isNotNull();

        // then
        assertThat(found.get(0).getCabin().getId())
                .isEqualTo(r.getId());

        // then
        assertThat(found.get(0).getClassType())
                .isEqualTo(fc.getClassType());

        // then
        assertThat(found.get(1).getCabin().getId())
                .isEqualTo(rr.getId());

        // then
        assertThat(found.get(1).getClassType())
                .isEqualTo(fc2.getClassType());
    }

    @Test
    public void whenUpdatePrice_thenUpdate() {
        // given
        Date dd = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        Flight f = new Flight(
                "test_origin",
                "test_destination",
                new Date(),
                "test_flight_number",
                dd,
                12,
                12,
                "ONE"
        );

        Flight f2 = new Flight(
                "test_origin2",
                "test_destination",
                new Date(),
                "test_flight_number2",
                dd,
                12,
                12,
                "ONE"
        );
        Cabin c = new Cabin(f, CabinType.C);
        Cabin d = new Cabin(f, CabinType.A);
        Cabin r = new Cabin(f, CabinType.B);
        FlightClass fc = new FlightClass("A",2,2,12, r);
        Cabin cc = new Cabin(f2, CabinType.C);
        Cabin d2 = new Cabin(f2, CabinType.A);
        Cabin rr = new Cabin(f2, CabinType.B);
        FlightClass fc2 = new FlightClass("A",10,2,12, rr);
        entityManager.persist(f);
        entityManager.persist(f2);
        entityManager.flush();
        List<Cabin> cabins = new ArrayList<Cabin>();
        cabins.add(r);
        cabins.add(rr);
        // when
        int updated = fcRepository.updatePrice(cabins, ClassType.A,20);
        entityManager.refresh(fc);
        entityManager.refresh(fc2);
        // then
        assertThat(updated)
                .isEqualTo(2);

        FlightClass first = fcRepository.findByCabinAndClassType(r, ClassType.A).orElse(null);
        // then
        assertThat(first.getPrice())
                .isEqualTo(20);
        FlightClass second = fcRepository.findByCabinAndClassType(rr, ClassType.A).orElse(null);
        // then
        assertThat(second.getPrice())
                .isEqualTo(20);

    }
}
