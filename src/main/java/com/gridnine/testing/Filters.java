package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

public class Filters {

    public static enum Relation {
        MORE, EQ, LESS
    }

    /**
     * @param rel       -> enum,имеет 3 значения: больше (MORE), равно (EQ), меньше (LESS)
     * @param departure -> дата отправления
     * @return Predicate<Flight> с датой отправления меньше, равно или больше заданной даты
     */
    public static Predicate<Flight> departureShouldHasRelationToParameter(Relation rel, LocalDateTime departure) {
        switch (rel) {
            case LESS:
                return flight -> flight.getDeparture().compareTo(departure) < 0;
            case EQ:
                return flight -> flight.getDeparture().compareTo(departure) == 0;
            case MORE:
                return flight -> flight.getDeparture().compareTo(departure) > 0;
            default:
                throw new IllegalArgumentException("invalid relation");
        }
    }

    /**
     * @param rel     -> enum,имеет 3 значения: больше (MORE), равно (EQ), меньше (LESS)
     * @param arrival -> дата прибытия
     * @return Predicate<Flight>  с датой прибытия меньше,равно или больше заданной даты
     */
    public static Predicate<Flight> arrivalShouldHasRelationToParameter(Relation rel, LocalDateTime arrival) {
        switch (rel) {
            case LESS:
                return flight -> flight.getArrival().compareTo(arrival) < 0;
            case EQ:
                return flight -> flight.getArrival().compareTo(arrival) == 0;
            case MORE:
                return flight -> flight.getArrival().compareTo(arrival) > 0;
            default:
                throw new IllegalArgumentException("invalid relation");
        }
    }

    /**
     * @param rel -> enum,имеет 3 значения: больше (MORE), равно (EQ), меньше (LESS)
     * @param dur -> разница во времени между пересадками
     * @return Predicate<Flight> c разницей между пересадками меньше,равно или больше заданной
     */
    public static Predicate<Flight> gapBetweenAllSegmentsShouldHasRelationToDuration(Relation rel, Duration dur) {
        return flight -> {
            for (int i = 1; i < flight.getGetSegments().size(); i++) {
                Duration gap = Duration.between(flight.getGetSegments().get(i - 1).getArrivalDate(),
                        flight.getGetSegments().get(i).getDepartureDate());

                switch (rel) {
                    case LESS:
                        if (gap.compareTo(dur) >= 0) return false;
                        break;
                    case EQ:
                        if (gap.compareTo(dur) != 0) return false;
                        break;
                    case MORE:
                        if (gap.compareTo(dur) <= 0) return false;
                        break;
                    default:
                        throw new IllegalArgumentException("invalid relation");
                }
            }
            return true;
        };
    }

    /**
     * @param segments -> количество пересадок
     * @return Predicate<Flight> с количеством пересадок не больше заданной
     */
    public static Predicate<Flight> flightHasNoMoreSegmentsThanParameter(int segments) {
        return flight -> {
            if (flight.getGetSegments().size() > segments)
                return false;
            else {
                return true;
            }
        };
    }

    public static Predicate<Flight> getDepartureNoToTheCurrentMoment() {
        LocalDateTime now = LocalDateTime.now();
        return flight -> flight.getDeparture().compareTo(now) >= 0;

    }


    public static Predicate<Flight> getNoArrivalLessDeparture() {
        return flight -> {
            List<Segment> getSegments = flight.getGetSegments();
            for (int i = 0; i < flight.getGetSegments().size() - 1; i++) {
                LocalDateTime departureDate = getSegments.get(i).getDepartureDate();
                LocalDateTime arrivalDate = getSegments.get(getSegments.size() - 1).getArrivalDate();
                if (departureDate.compareTo(arrivalDate) <= 0) {
                    return true;
                }
            }
            return false;
        };
    }


    public static Predicate<Flight> getNoTimeOnTransferEqualTwoHours() {
        return flight -> {
            long gap = 0L;
            for (int i = 1; i < flight.getGetSegments().size(); i++) {
                gap += Duration.between(flight.getGetSegments().get(i - 1).getArrivalDate(),
                        flight.getGetSegments().get(i).getDepartureDate()).toMinutes();

            }
            if (gap > 120) {
                return false;
            } else {
                return true;
            }

        };
    }

}

