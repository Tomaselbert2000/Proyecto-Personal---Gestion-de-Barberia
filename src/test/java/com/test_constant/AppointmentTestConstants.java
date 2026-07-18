package com.test_constant;

import com.enums.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.test_constant.AppointmentTestConstants.CreationValidData.END_DATETIME;
import static com.test_constant.AppointmentTestConstants.CreationValidData.START_DATETIME;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.INSTANT;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.ZONE_ID;

public final class AppointmentTestConstants {

    private AppointmentTestConstants() {
    }

    public static class TestTimeConfigurationData {

        public static final Instant INSTANT = Instant.parse("2026-01-01T15:00:00Z");
        public static final ZoneId ZONE_ID = ZoneId.of("America/Argentina/Buenos_Aires");
    }

    public static class CreationValidData {
        public static final LocalDateTime START_DATETIME = LocalDateTime.of(2026, 1, 1, 12, 30);
        public static final LocalDateTime END_DATETIME = LocalDateTime.of(2026, 1, 1, 13, 0);
        public static final String APPOINTMENT_OPTIONAL_NOTES = "Sin notas adicionales";
    }

    public static class UpdateValidData {

        public static final Long NEW_BARBER_SERVICE_ID = 11L;
        public static final Long NEW_EMPLOYEE_ID = 101L;
        public static final LocalDateTime NEW_START_DATETIME = START_DATETIME.plusDays(1);
        public static final LocalDateTime NEW_END_DATETIME = END_DATETIME.plusDays(1);
        public static final AppointmentStatus NEW_STATUS = AppointmentStatus.REPROGRAMADO;
    }

    public static class InvalidData {

        public static final LocalDateTime START_DATETIME_BEFORE_CURRENT_DATETIME = LocalDateTime.ofInstant(INSTANT, ZONE_ID).minusHours(1);
        public static final LocalDateTime END_DATETIME_BEFORE_CURRENT_DATETIME = START_DATETIME_BEFORE_CURRENT_DATETIME.plusMinutes(30);

        public static final LocalDateTime END_DATETIME_BEFORE_START_DATETIME = START_DATETIME.minusHours(1);

        public static final LocalDateTime START_DATETIME_BEFORE_OPENING_TIME = LocalDateTime.of(2026, 1, 2, 6, 30);
        public static final LocalDateTime END_DATETIME_FOR_OPENING_HOURS_TEST = START_DATETIME_BEFORE_OPENING_TIME.plusMinutes(30);

        public static final LocalDateTime END_DATETIME_AFTER_CLOSING_TIME = LocalDateTime.of(2026, 1, 2, 21, 15);
        public static final LocalDateTime START_DATETIME_FOR_CLOSING_HOURS_TEST = END_DATETIME_AFTER_CLOSING_TIME.minusHours(2);

        public static final LocalDateTime START_DATETIME_EXACTLY_ON_OPENING_HOURS = LocalDateTime.of(2026, 1, 2, 8, 0);
        public static final LocalDateTime END_DATETIME_FOR_EXACTLY_OPENING_HOURS_TEST = START_DATETIME_EXACTLY_ON_OPENING_HOURS.plusMinutes(30);

        public static final LocalDateTime END_DATETIME_EXACTLY_ON_CLOSING_HOURS = LocalDateTime.of(2026, 1, 2, 20, 0);
        public static final LocalDateTime START_DATETIME_FOR_EXACTLY_CLOSING_HOURS_TEST = END_DATETIME_EXACTLY_ON_CLOSING_HOURS.minusHours(1);
    }
}
