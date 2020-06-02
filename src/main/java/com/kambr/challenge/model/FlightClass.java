package com.kambr.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kambr.challenge.model.enums.ClassType;

import javax.persistence.*;

@Entity
public class FlightClass {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private double price;
    @Column(name = "seats_sold")
    private int seatsSold;
    @Column
    private double revenue;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Cabin cabin;

    @Version
    private long version;

    @Column(name = "class_type")
    private ClassType classType;

    public FlightClass() {
    }

    public FlightClass(String classType, double price, int seatsSold, double revenue, Cabin cabin) {
        this.classType = ClassType.valueOf(classType);
        this.price = price;
        this.seatsSold = seatsSold;
        this.revenue = revenue;
        this.cabin = cabin;
        this.cabin.addClass(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeatsSold() {
        return seatsSold;
    }

    public void setSeatsSold(int seatsSold) {
        this.seatsSold = seatsSold;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

}
