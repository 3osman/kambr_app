package com.kambr.challenge.model;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FlightSearchRequest {
    private String origin;
    private String destination;
    private Date departureDateFrom;
    private Date departureDateTo;
    private String flightNumber;

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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getDepartureDateFrom() {
        return departureDateFrom;
    }

    public void setDepartureDateFrom(String departureDateFrom) throws ParseException {
        this.departureDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateFrom);
    }

    public Date getDepartureDateTo() {
        return departureDateTo;
    }

    public void setDepartureDateTo(String departureDateTo) throws ParseException {
        this.departureDateTo = new SimpleDateFormat("yyyy-MM-dd").parse(departureDateTo);
    }

    public void setDepartureDateToDate(Date departureDateTo) {
        this.departureDateTo = departureDateTo;
    }

    public QueryBuilder toQuery(){
        BoolQueryBuilder s = new BoolQueryBuilder();
        if (this.getOrigin() != null) {
            s.should(QueryBuilders.matchQuery("origin", this.getOrigin()).fuzziness(Fuzziness.TWO)
                    .operator(Operator.AND));
        }

        if (this.getDestination() != null) {
            s.should(QueryBuilders.matchQuery("destination", this.getDestination()).fuzziness(Fuzziness.TWO)
                    .operator(Operator.AND));
        }

        if (this.getFlightNumber() != null) {
            s.should(QueryBuilders.matchQuery("flightNumber", this.getFlightNumber()).fuzziness(Fuzziness.TWO)
                    .operator(Operator.AND));
        }

        if (this.getDepartureDateFrom() != null || this.getDepartureDateTo() != null) {
            RangeQueryBuilder dateRange = QueryBuilders.rangeQuery("departureDate");
            if (this.getDepartureDateFrom() != null && this.getDepartureDateTo() != null && this.getDepartureDateFrom().getTime() == this.getDepartureDateTo().getTime()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(this.getDepartureDateFrom());
                cal.add(Calendar.DATE, 1); //minus number would decrement the days
                this.setDepartureDateToDate(cal.getTime());
            }
            if (this.getDepartureDateFrom() != null) dateRange.from(this.getDepartureDateFrom().getTime());
            if (this.getDepartureDateTo() != null) dateRange.to(this.getDepartureDateTo().getTime());
            s.should(dateRange.includeLower(true).includeUpper(true));
        }
        System.out.println(s.toString());
        return s;
    }
}
