package com.utils.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculation {

    public static final LocalTime FIRST_SECOND_OF_DAY = LocalTime.MIN;
    public static final LocalTime LAST_SECOND_OF_DAY = LocalTime.MAX;

    public static LocalDate getCurrentDate() {

        return LocalDate.now();
    }

    public static LocalDateTime getCurrentDateTime() {

        return LocalDateTime.now();
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

}
