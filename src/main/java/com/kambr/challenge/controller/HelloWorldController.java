package com.kambr.challenge.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.kambr.challenge.model.*;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.service.CabinService;
import com.kambr.challenge.service.FlightMetaDataService;
import com.kambr.challenge.service.FlightService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;

@Controller
public class HelloWorldController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	FlightMetaDataService flightMetaService;

	@Autowired
	FlightService flightCreatorService;

	@Autowired
	CabinService cabinService;

	@GetMapping("/hello-world")
	@ResponseBody
	//public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
	public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {

		Flight f = flightCreatorService.createFlight(
				"test_origin",
				"test_destination",
				new Date(),
				"test_flight_number",
				new Date(),
				12,
				12,
				"ONE"
		);
		FlightMetadata flight = new FlightMetadata();
		flight.setOrigin("test_origin");


		//
		Cabin c = cabinService.createCabin(f, CabinType.C);
		Cabin d = cabinService.createCabin(f, CabinType.A);
		Cabin r = cabinService.createCabin(f, CabinType.B);
		flightCreatorService.save(f);
		Example<FlightMetadata> flightExample = Example.of(flight);
		//ArrayList<FlightMetadata> x = (ArrayList)(flightMetaService.findBy(flight. PageRequest.of(0,1))).getContent();
		FlightMetadata x = flightMetaService.findOne(f.getId()).get();
		//Iterator iter = x.iterator();
		Flight fReturned = flightCreatorService.findById(x.getId());
		return new Greeting(counter.incrementAndGet(), String.format(template, fReturned.getCabins().size()));
	}

	@GetMapping("/flights")
	@ResponseBody
	public Iterable<Flight> findFlight(FlightSearchRequest parameters, @RequestParam(name="page", required=false, defaultValue="0") String page, @RequestParam(name="size", required=false, defaultValue="10") String size) {
		QueryBuilder q = parameters.toQuery();
		return flightMetaService.findByQuery(q, PageRequest.of(Integer.parseInt(page),Integer.parseInt(size)));
	}

}
