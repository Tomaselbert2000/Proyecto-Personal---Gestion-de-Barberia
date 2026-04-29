package com.barbershop.utils.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class TimeCalculation {

    public static final LocalTime FIRST_SECOND_OF_DAY = LocalTime.MIN;
    public static final LocalTime LAST_SECOND_OF_DAY = LocalTime.MAX;

    public static LocalDate getCurrentDate() {

        return LocalDate.now();
    }

    public static LocalDateTime getStartOfToday() {

        LocalDate today = getCurrentDate();

        return LocalDateTime.of(today, FIRST_SECOND_OF_DAY);
    }

    public static LocalDateTime getEndOfToday() {

        LocalDate today = getCurrentDate();

        return LocalDateTime.of(today, LAST_SECOND_OF_DAY);
    }

    public static LocalDate getStartOfCurrentMonth() {

        LocalDate today = getCurrentDate();

        return today.withDayOfMonth(1);
    }

    public static LocalDate getEndOfCurrentMonth() {

        LocalDate today = getCurrentDate();

        return today.withDayOfMonth(today.lengthOfMonth());
    }

    public static LocalDateTime getStartOfLastMonth() {

        LocalDate today = getCurrentDate();

        LocalDate theLastMonth = today.minusMonths(1);

        return theLastMonth.with(TemporalAdjusters.firstDayOfMonth()).atTime(FIRST_SECOND_OF_DAY);
    }

    public static LocalDateTime getEndOfLastMonth() {

        LocalDate today = getCurrentDate();

        LocalDate theLastMonth = today.minusMonths(1);

        return theLastMonth.with(TemporalAdjusters.lastDayOfMonth()).atTime(LAST_SECOND_OF_DAY);
    }
}
