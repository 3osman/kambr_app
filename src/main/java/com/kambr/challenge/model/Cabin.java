package com.kambr.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kambr.challenge.model.enums.CabinType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CABIN")
public class Cabin {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Flight flight;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "cabin_id")
    private List<FlightClass> classes;
    @Column(name = "cabin_type")
    private CabinType cabinType;

    public Cabin() {
    }

    public Cabin(Flight flight, CabinType cabinName) {
        this.flight = flight;
        this.cabinType = cabinName;
        this.classes = new ArrayList<FlightClass>();
        this.flight.addCabin(this);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public CabinType getCabinName() {
        return cabinType;
    }

    public void setCabinName(CabinType cabinName) {
        this.cabinType = cabinName;
    }

    public List<FlightClass> getClasses() {
        return classes;
    }

    public void setClasses(List<FlightClass> classes) {
        this.classes = classes;
    }

    public void addClass(FlightClass c) {
        classes.add(c);
    }
}
