package com.mapper.appointment;

import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.mapper.implementation.AppointmentMapperImpl;
import com.barbershop.model.Appointment;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppointmentMapperImplUpdateTest {

    private AppointmentMapperImpl mapper;

    private BarberService service;
    private Employee employee;

    private Appointment existingAppointment;

    private AppointmentUpdateDTO updateDTO;

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2026, 1, 1, 17, 30, 45);

    private static final ZoneId ZONE_ID = ZoneId.of("America/Argentina/Buenos_Aires");

    private static final Instant INSTANT = LOCAL_DATE_TIME.atZone(ZONE_ID).toInstant();

    private static final Clock CLOCK = Clock.fixed(INSTANT, ZONE_ID);

    @BeforeEach
    void init() {

        mapper = new AppointmentMapperImpl();

        Client client = new Client();
        service = new BarberService();
        employee = new Employee();

        existingAppointment = Appointment.builder()
                .appointmentID(1L)
                .client(client)
                .barberservice(service)
                .employee(employee)
                .startDateTime(LocalDateTime.of(2026, 1, 1, 17, 0))
                .endDateTime(LocalDateTime.of(2026, 1, 1, 17, 30))
                .currentStatus(AppointmentStatus.PROGRAMADO)
                .build();

        updateDTO = AppointmentUpdateDTO.builder()
                .newEmployeeID(1L)
                .newBarberserviceID(10L)
                .newStartDateTime(LocalDateTime.of(2026, 1, 1, 19, 0))
                .newEndDateTime(LocalDateTime.of(2026, 1, 1, 19, 30))
                .newStatus(AppointmentStatus.REPROGRAMADO)
                .build();
    }

    @Test
    void givenStartDateTimeWithRemainingSeconds_WhenUpdating_ThenIsTruncatedOnMinutes() {

        LocalDateTime start = LocalDateTime.now(CLOCK);
        LocalDateTime end = LocalDateTime.now(CLOCK).plusMinutes(30);

        updateDTO.setNewStartDateTime(start);
        updateDTO.setNewEndDateTime(end);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, new Employee(), new BarberService(), new Appointment());

        assertEquals(LocalDateTime.of(2026, 1, 1, 17, 30), result.getStartDateTime());
    }

    @Test
    void givenEndDateTimeWithRemainingSeconds_WhenUpdating_ThenIsTruncatedOnMinutes() {

        LocalDateTime start = LocalDateTime.now(CLOCK);
        LocalDateTime end = LocalDateTime.now(CLOCK).plusMinutes(30);

        updateDTO.setNewStartDateTime(start);
        updateDTO.setNewEndDateTime(end);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, new Employee(), new BarberService(), new Appointment());

        assertEquals(LocalDateTime.of(2026, 1, 1, 18, 0), result.getEndDateTime());
    }

    @Test
    void givenNullEmployeeID_WhenUpdating_ThenEntityKeepsCurrentEmployee() {

        updateDTO.setNewEmployeeID(null);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, null, service, existingAppointment);

        assertEquals(employee, result.getEmployee());
    }

    @Test
    void givenNullBarberServiceID_WhenUpdating_ThenEntityKeepsCurrentService() {

        updateDTO.setNewBarberserviceID(null);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, null, existingAppointment);

        assertEquals(service, result.getBarberservice());
    }

    @Test
    void givenNullNewStartDateTime_WhenUpdating_ThenEntityKeepsCurrentStartDateTime() {

        updateDTO.setNewStartDateTime(null);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, service, existingAppointment);

        assertEquals(LocalDateTime.of(2026, 1, 1, 17, 0), result.getStartDateTime());
    }

    @Test
    void givenNullNewEndDateTime_WhenUpdating_ThenEntityKeepsCurrentEndDateTime() {

        updateDTO.setNewEndDateTime(null);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, service, existingAppointment);

        assertEquals(LocalDateTime.of(2026, 1, 1, 17, 30), result.getEndDateTime());
    }

    @Test
    void givenNullNewStatus_WhenUpdating_ThenEntityKeepsCurrentStatus() {

        updateDTO.setNewStatus(null);

        Appointment result = mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, service, existingAppointment);

        assertEquals(AppointmentStatus.PROGRAMADO, result.getCurrentStatus());
    }
}
