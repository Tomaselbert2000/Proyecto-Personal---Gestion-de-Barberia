package com.integration;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class IntegrationTestConstants {

    private IntegrationTestConstants() {
    }

    public static final class AppointmentIntegrationTestConstants {

        public static final int START_HOUR = 8;
        public static final int APPOINTMENT_DURATION_MINUTES = 30;
        public static final LocalDate futureDate = LocalDate.now().plusDays(1);
        public static final LocalDateTime startDateTime = futureDate.atTime(START_HOUR, 0);
        public static final LocalDateTime endDateTime = startDateTime.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        public static LocalDateTime startDateTime1 = futureDate.atTime(START_HOUR, 0);
        public static LocalDateTime endDateTime1 = startDateTime1.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        public static LocalDateTime startDateTime2 = startDateTime1.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        public static LocalDateTime endDateTime2 = startDateTime2.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        public static final LocalDateTime now = LocalDateTime.now();
        public static final LocalDate tomorrow = now.toLocalDate().plusDays(1);
        public static final LocalDateTime start1 = tomorrow.atTime(10, 30);
        public static final LocalDateTime end1 = tomorrow.atTime(11, 0);
        public static final LocalDateTime start2 = tomorrow.atTime(14, 0);
        public static final LocalDateTime end2 = tomorrow.atTime(14, 30);
    }

    public static final class AppointmentInfoAssertions {

        public static final String MESSAGE_ID_MATCH = "ID del turno coincide";
        public static final String MESSAGE_EMPLOYEE_ID_MATCH = "ID del empleado coincide";
        public static final String MESSAGE_SERVICE_ID_MATCH = "ID del servicio de barbería coincide";
        public static final String MESSAGE_CLIENT_FIRST_NAME_MATCH = "Nombre del cliente coincide";
        public static final String MESSAGE_CLIENT_LAST_NAME_MATCH = "Apellido del cliente coincide";
        public static final String MESSAGE_SERVICE_NAME_MATCH = "Nombre del servicio coincide";
        public static final String MESSAGE_SERVICE_PRICE_MATCH = "Precio del servicio coincide";
        public static final String MESSAGE_EMPLOYEE_FIRST_NAME_MATCH = "Nombre del empleado coincide";
        public static final String MESSAGE_EMPLOYEE_LAST_NAME_MATCH = "Apellido del empleado coincide";
        public static final String MESSAGE_REGISTRATION_TIMESTAMP_MATCH = "Timestamp de registro coincide";
        public static final String MESSAGE_START_DATETIME_MATCH = "Hora de inicio coincide";
        public static final String MESSAGE_END_DATETIME_MATCH = "Hora de fin coincide";
        public static final String MESSAGE_CURRENT_STATUS_MATCH = "Estado actual coincide";
        public static final String MESSAGE_OPTIONAL_NOTES_MATCH = "Notas opcionales coinciden";
    }
}
