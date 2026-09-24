package com.utils.time;

import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;

public class TimeCalculation {

    public static final LocalTime FIRST_SECOND_OF_DAY = LocalTime.MIN;
    public static final LocalTime LAST_SECOND_OF_DAY = LocalTime.MAX;

    @Setter
    private static Supplier<LocalDateTime> datetimeProvider = LocalDateTime::now;

    @Setter
    private static Supplier<LocalDate> dateProvider = LocalDate::now;

    public static LocalDate getCurrentDate() {

        return dateProvider.get();
    }

    public static LocalDateTime getCurrentDateTime() {

        return datetimeProvider.get();
    }

    public static LocalDateTime getStartOfToday() {

        return LocalDateTime.of(dateProvider.get(), FIRST_SECOND_OF_DAY);
    }

    public static LocalDateTime getEndOfToday() {

        return LocalDateTime.of(dateProvider.get(), LAST_SECOND_OF_DAY);
    }

    public static LocalDate getStartOfCurrentMonth() {

        return dateProvider.get().withDayOfMonth(1);
    }

    public static LocalDate getEndOfCurrentMonth() {

        return dateProvider.get().withDayOfMonth(dateProvider.get().lengthOfMonth());
    }
}
