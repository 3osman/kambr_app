package com.kambr.challenge;


import com.kambr.challenge.dto.FlightResponse;
import com.kambr.challenge.dto.FlightResponseWithData;
import com.kambr.challenge.model.Cabin;
import com.kambr.challenge.model.Flight;
import com.kambr.challenge.model.FlightClass;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.repo.FlightRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FlightRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void whenFindById_thenReturnFlight() {
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

        entityManager.persist(f);
        entityManager.flush();

        // when
        Flight found = flightRepository.findById(f.getId()).orElse(null);

        // then
        assertThat(found.getId())
                .isEqualTo(f.getId());
    }

    @Test
    public void whenFindByIdIn_thenReturnFlights() {
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

        entityManager.persist(f);
        entityManager.persist(f2);
        entityManager.flush();
        List<String> ids = new ArrayList<String>();
        ids.add(f.getId());
        ids.add(f2.getId());
        // when
        List<FlightResponse> found = flightRepository.findByIdIn(ids);

        // then
        assertThat(found.size())
                .isEqualTo(2);

        // then
        assertThat(found.get(0).getId())
                .isEqualTo(f.getId());

        // then
        assertThat(found.get(1).getId())
                .isEqualTo(f2.getId());
    }

    @Test
    public void whenFindWithDataByIdIn_thenReturnFlightsWithData() {
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
        List<String> ids = new ArrayList<String>();
        ids.add(f.getId());
        ids.add(f2.getId());
        // when
        List<FlightResponseWithData> found = flightRepository.findWithDataByIdIn(ids);

        // then
        assertThat(found.size())
                .isEqualTo(2);

        // then
        assertThat(found.get(0).getId())
                .isEqualTo(f.getId());

        // then
        assertThat(found.get(1).getId())
                .isEqualTo(f2.getId());

        // then
        assertThat(found.get(0).getCabins().size())
                .isEqualTo(3);

        // then
        assertThat(found.get(1).getCabins().size())
                .isEqualTo(3);
    }
}
