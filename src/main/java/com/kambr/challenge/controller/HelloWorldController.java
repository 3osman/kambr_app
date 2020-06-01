package com.kambr.challenge.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.kambr.challenge.model.*;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.service.CabinService;
import com.kambr.challenge.service.FlightMetaDataService;
import com.kambr.challenge.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;

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
	public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		Flight f = flightCreatorService.createFlight("test_origin",
				"test_destination",
				new Date(),
				"test_flight_number",
				new Date(),
				12,
				12,
				"ONE");
		//FlightMetadata flight = new FlightMetadata();
		//flight.setOrigin("test_origin");

		//Example<FlightMetadata> flightExample = Example.of(flight);
		//List<FlightMetadata> x = flightMetaService.findBy(flightExample, PageRequest.of(0,1)).getContent();
		//Iterable x = flightMetaService.findAll();
		//Iterator iter = x.iterator();
		//FlightMetadata first = (FlightMetadata) iter.next();
		//Flight f = flightCreatorService.findById(first.getId());
		Cabin c = cabinService.createCabin(f, CabinType.C);
		Cabin d = cabinService.createCabin(f, CabinType.A);
		Cabin r = cabinService.createCabin(f, CabinType.B);
		return new Greeting(counter.incrementAndGet(), String.format(template, f.getCabins().size()));
	}

	@GetMapping("/flights")
	@ResponseBody
	public Iterable<FlightMetadata> sayHello(FlightSearchRequest parameters) {
		return flightMetaService.findAll();
	}

}
