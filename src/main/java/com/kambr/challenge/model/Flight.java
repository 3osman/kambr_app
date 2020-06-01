package com.kambr.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kambr.challenge.model.enums.AircraftType;
import com.kambr.challenge.util.FlightUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="FLIGHT")
public class Flight {
	@Id
	private String id;
	@Column
	private String origin;
	@Column
	private String destination;
	@Temporal(TemporalType.DATE)
	@Column(name = "departure_date")
	private Date departureDate;
	@Column(name = "flight_number")
	private String flightNumber;
	@Column(name = "departure_time")
	private long departureTime;
	@Temporal(TemporalType.DATE)
	@Column(name = "arrival_date")
	private Date arrivalDate;
	@Column(name = "arrival_time")
	private long arrivalTime;
	@Column(name = "aircraft_type")
	private AircraftType aircraft;
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "flight_id")
	private List<Cabin> cabins;

	public Flight() {

	}
	public Flight(String origin, String destination, Date departureDate, String flightNumber, Date arrivalDate, long departureTime, long arrivalTime, String aircraft) {
		FlightUtils util = new FlightUtils();
		this.id = util.getFlightId(origin, destination,departureDate,flightNumber);
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
		this.flightNumber = flightNumber;
		this.arrivalDate = arrivalDate;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.aircraft = AircraftType.valueOf(aircraft);
		this.cabins = new ArrayList<Cabin>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public long getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(long departureTime) {
		this.departureTime = departureTime;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public AircraftType getAircraft() {
		return aircraft;
	}

	public void setAircraft(AircraftType aircraft) {
		this.aircraft = aircraft;
	}

	public List<Cabin> getCabins() {
		return cabins;
	}

	public void setCabins(List<Cabin> cabins) {
		this.cabins = cabins;
	}

	public void addCabin(Cabin c){
		cabins.add(c);
	}
}
