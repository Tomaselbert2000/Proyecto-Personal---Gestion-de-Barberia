package com.mapper.appointment;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
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

public class AppointmentMapperImplCreationTest {

    private AppointmentMapperImpl appointmentMapperImpl;

    private Clock clock;

    @BeforeEach
    void init(){

        ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

        LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 1, 17, 30, 45);

        Instant instant = localDateTime.atZone(zoneId).toInstant();

        clock = Clock.fixed(instant, zoneId);

        appointmentMapperImpl = new AppointmentMapperImpl();
    }

    @Test
    void givenStartDateTimeWithRemainingSeconds_WhenCreating_ThenStartDateTimeIsTruncatedOnMinutes(){

        LocalDateTime start = LocalDateTime.now(clock);
        LocalDateTime end = LocalDateTime.now(clock).plusMinutes(30);

        AppointmentCreationDTO newAppointment = AppointmentCreationDTO.builder()
                .clientID(1L)
                .barberserviceID(1L)
                .startDateTime(start)
                .endDateTime(end)
                .build();

        Appointment result = appointmentMapperImpl.mapAppointmentCreationDtoToAppointmentEntity(newAppointment, new Client(), new Employee(),new BarberService());

        assertEquals(LocalDateTime.of(2026, 1, 1, 17, 30), result.getStartDateTime());
    }

    @Test
    void givenEndDateTimeWithRemainingSeconds_WhenCreating_ThenEndIsTruncatedOnMinutes(){

        LocalDateTime start = LocalDateTime.now(clock);
        LocalDateTime end = LocalDateTime.now(clock).plusMinutes(30);

        AppointmentCreationDTO newAppointment = AppointmentCreationDTO.builder()
                .clientID(1L)
                .barberserviceID(1L)
                .startDateTime(start)
                .endDateTime(end)
                .build();

        Appointment result = appointmentMapperImpl.mapAppointmentCreationDtoToAppointmentEntity(newAppointment, new Client(), new Employee(),new BarberService());

        assertEquals(LocalDateTime.of(2026, 1, 1, 18, 0), result.getEndDateTime());
    }

    @Test
    void givenNewAppointment_WhenCreating_ThenTheDefaultStatusIsPROGRAMADO(){

        LocalDateTime start = LocalDateTime.now(clock);
        LocalDateTime end = LocalDateTime.now(clock).plusMinutes(30);

        AppointmentCreationDTO newAppointment = AppointmentCreationDTO.builder()
                .clientID(1L)
                .barberserviceID(1L)
                .startDateTime(start)
                .endDateTime(end)
                .build();

        Appointment result = appointmentMapperImpl.mapAppointmentCreationDtoToAppointmentEntity(newAppointment, new Client(), new Employee(),new BarberService());

        assertEquals(AppointmentStatus.PROGRAMADO, result.getCurrentStatus());
    }
}

