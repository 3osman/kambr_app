package com.kambr.challenge.dto;

public class FlightCabinClassInfo {
    private String flightNumber;
    private String cabinName;
    private String className;

    public FlightCabinClassInfo(String flightNumber, String cabinName, String className) {
        this.flightNumber = flightNumber;
        this.cabinName = cabinName;
        this.className = className;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCabinName() {
        return cabinName;
    }

    public void setCabinName(String cabinName) {
        this.cabinName = cabinName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String classToString() {
        return this.flightNumber + ":" + this.cabinName + ":" + this.className;
    }
}
