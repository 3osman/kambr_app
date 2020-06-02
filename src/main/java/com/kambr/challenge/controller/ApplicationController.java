package com.kambr.challenge.controller;

import com.kambr.challenge.dto.FlightResponse;
import com.kambr.challenge.model.*;
import com.kambr.challenge.model.enums.CabinType;
import com.kambr.challenge.model.enums.ClassType;
import com.kambr.challenge.service.CabinService;
import com.kambr.challenge.service.FlightClassService;
import com.kambr.challenge.service.FlightMetaDataService;
import com.kambr.challenge.service.FlightService;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.QueryBuilder;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ApplicationController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	@Autowired
	FlightMetaDataService flightMetaService;

	@Autowired
	FlightService flightCreatorService;

	@Autowired
	CabinService cabinService;

	@Autowired
	FlightClassService classService;

	@GetMapping("/hello-world")
	@ResponseBody
	public String startUp() {
//		Date dd = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
//		Flight f = flightCreatorService.createFlight(
//				"test_origin",
//				"test_destination",
//				new Date(),
//				"test_flight_number",
//				dd,
//				12,
//				12,
//				"ONE"
//		);
//
//
//		FlightMetadata flight = new FlightMetadata();
//		flight.setOrigin("test_origin");
//		Cabin c = cabinService.createCabin(f, CabinType.C);
//		Cabin d = cabinService.createCabin(f, CabinType.A);
//		Cabin r = cabinService.createCabin(f, CabinType.B);
//		FlightClass fc = new FlightClass("A",2,2,12, r);
//
//		flightCreatorService.save(f);
//
//		Flight f2 = flightCreatorService.createFlight(
//				"test_origin_2",
//				"test_destination_2",
//				dd,
//				"test_flight_number_2",
//				dd,
//				12,
//				12,
//				"ONE"
//		);
//		Cabin cc = cabinService.createCabin(f2, CabinType.C);
//		Cabin d2 = cabinService.createCabin(f2, CabinType.A);
//		Cabin rr = cabinService.createCabin(f2, CabinType.B);
//		FlightClass fc2 = new FlightClass("A",10,2,12, rr);
//		flightCreatorService.save(f2);
//		Example<FlightMetadata> flightExample = Example.of(flight);
//		FlightMetadata x = flightMetaService.findOne(f.getId()).get();
//		Flight fReturned = flightCreatorService.findById(x.getId());
		return "";
	}

	@GetMapping("/flights")
	@ResponseBody
	@ApiOperation(value="Searches for flights", produces="application/json", response=FlightResponse.class, responseContainer = "List")
	public List<Object> findFlight(FlightSearchRequest parameters, @RequestParam(name="includeData", required=false, defaultValue="false") String includeData, @RequestParam(name="page", required=false, defaultValue="0") String page, @RequestParam(name="size", required=false, defaultValue="10") String size) {
		Map<String, Object> json = new HashMap();
		QueryBuilder q = parameters.toQuery();
		return flightMetaService.findByQuery(q, PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)), Boolean.parseBoolean(includeData));
	}

	//update one flight
	//params: Flight id, Flight cabin, Flight class
	@PutMapping("/update")
	@ResponseBody
	@ApiOperation(value="Updates a class for a flight", produces="application/json", response=FlightClass.class)
	public ResponseEntity<Object> update(@RequestParam(name="flightId", required=true) String flightId, @RequestParam(name="cabin", required=true) String cabin, @RequestParam(name="class", required=true) String flightClass, @RequestParam(name="newPrice", required=true) double newPrice) throws JSONException {
		FlightClass output = classService.updateFlightClass(flightId, cabin, ClassType.valueOf(flightClass), newPrice);
		Map<String, Object> json = new HashMap();
		if (output == null) {
			json.put("result", "update unsuccessful");
			json.put("reason", "combination of flight and cabin and flight class does not exist");
			return new ResponseEntity<Object>(json, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		}
	}

	// update multiple  flight
	// params: Flight ids list, cabin, class, new price
	@PostMapping("/batchUpdate")
	@ResponseBody
	@ApiOperation(value="Updates a class for list of flights", produces="application/json", responseContainer = "List", response=FlightClass.class)
	public ResponseEntity<Object> batchUpdate(@RequestParam(name="flightIds", required=true) List<String> flightIds, @RequestParam(name="cabin", required=true) String cabin, @RequestParam(name="class", required=true) String flightClass, @RequestParam(name="newPrice", required=true) double newPrice) {
 		List<FlightClass> updated = classService.updateFlightClass(flightIds, cabin, ClassType.valueOf(flightClass), newPrice);
		Map<String, Object> json = new HashMap();
		if (updated.size() == 0) {
			json.put("result", "update unsuccessful");
			json.put("reason", "combination of flights and cabin and flight class does not exist");
			return new ResponseEntity<Object>(json, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
	}

}
