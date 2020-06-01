package com.kambr.challenge.model;

import com.kambr.challenge.model.enums.ClassType;

import javax.persistence.*;

@Entity
public class FlightClass {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column
    private double price;
    @Column(name = "seats_sold")
    private int seatsSold;
    @Column
    private double revenue;

    @ManyToOne
    Cabin cabin;

    @Version
    private long version;

    @Column(name = "class_type")
    private ClassType classType;

    public FlightClass(String cabinType, double price, int seatsSold, double revenue, Cabin cabin) {
        this.classType = ClassType.valueOf(cabinType);
        this.price = price;
        this.seatsSold = seatsSold;
        this.revenue = revenue;
        this.cabin = cabin;
    }

    public ClassType getCabinType() {
        return classType;
    }

    public void setCabinType(ClassType cabinType) {
        this.classType = cabinType;
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
