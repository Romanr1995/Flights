package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {


        List<Flight> test = FlightBuilder.createFlights().stream()
                .filter(Filters.getDepartureNoToTheCurrentMoment())
                .filter(Filters.getNoTimeOnTransferEqualTwoHours())
                .filter(Filters.getNoArrivalLessDeparture())
                .collect(Collectors.toList());

        List<Flight> testDepartureShouldHasRelationToParameter = test.stream()
                .filter(Filters.departureShouldHasRelationToParameter(Filters.Relation.MORE, LocalDateTime.now()))
                .collect(Collectors.toList());

        List<Flight> testArrivalShouldHasRelationToParameter = test.stream()
                .filter(Filters.arrivalShouldHasRelationToParameter(Filters.Relation.MORE, LocalDateTime.now()))
                .collect(Collectors.toList());

        List<Flight> testGapBetweenAllSegmentsShouldHasRelationToDuration = test.stream()
                .filter(Filters.gapBetweenAllSegmentsShouldHasRelationToDuration(Filters.Relation.MORE,
                        Duration.ofMinutes(30)))
                .collect(Collectors.toList());

        List<Flight> testFlightHasNoMoreSegmentsThanParameter = test.stream()
                .filter(Filters.flightHasNoMoreSegmentsThanParameter(2))
                .collect(Collectors.toList());

        System.out.println(testDepartureShouldHasRelationToParameter);
        System.out.println(testArrivalShouldHasRelationToParameter);
        System.out.println(testGapBetweenAllSegmentsShouldHasRelationToDuration);
        System.out.println(testFlightHasNoMoreSegmentsThanParameter);
    }
}
