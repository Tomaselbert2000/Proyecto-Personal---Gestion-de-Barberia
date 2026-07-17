package com.mapper.appointment;

import com.dto.appointment.AppointmentCreationDTO;
import com.enums.AppointmentStatus;
import com.mapper.implementation.AppointmentMapperImpl;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.AppointmentTestDataFactory.buildValidAppointmentCreationDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppointmentMapperCreationTest {

    private final AppointmentMapper appointmentMapperImpl = new AppointmentMapperImpl();

    private final AppointmentCreationDTO inputDTO = buildValidAppointmentCreationDTO();

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, la fecha y hora de inicio se trunca en minutos")
    void givenStartDateTimeWithRemainingSeconds_WhenCreating_ThenStartDateTimeIsTruncatedOnMinutes() {

        Appointment result = mapAppointment();

        assertEquals(inputDTO.getStartDateTime(), result.getStartDateTime());
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, la fecha y hora de finalización se trunca en minutos")
    void givenEndDateTimeWithRemainingSeconds_WhenCreating_ThenEndIsTruncatedOnMinutes() {

        Appointment result = mapAppointment();

        assertEquals(inputDTO.getEndDateTime(), result.getEndDateTime());
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, el estado por defecto es PROGRAMADO")
    void givenNewAppointment_WhenCreating_ThenTheDefaultStatusIsPROGRAMADO() {

        Appointment result = mapAppointment();

        assertEquals(AppointmentStatus.PROGRAMADO, result.getCurrentStatus());
    }

    private Appointment mapAppointment() {

        return appointmentMapperImpl.mapAppointmentCreationDtoToAppointmentEntity(inputDTO, new Client(), new Employee(), new BarberService());
    }
}

