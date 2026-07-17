package com.mapper.appointment;

import com.dto.appointment.AppointmentUpdateDTO;
import com.mapper.implementation.AppointmentMapperImpl;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.factory.AppointmentTestDataFactory.buildValidAppointment;
import static com.factory.AppointmentTestDataFactory.buildValidAppointmentUpdateDTO;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.INSTANT;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.ZONE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppointmentMapperUpdateTest {

    private final AppointmentMapper mapper = new AppointmentMapperImpl();
    private final Clock clock = Clock.fixed(INSTANT, ZONE_ID);
    private final Appointment existingAppointment = buildValidAppointment();
    private AppointmentUpdateDTO updateDTO;

    @BeforeEach
    void init() {

        updateDTO = buildValidAppointmentUpdateDTO();
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar la fecha y hora de inicio, se truncará en minutos")
    void givenStartDateTimeWithRemainingSeconds_WhenUpdating_ThenIsTruncatedOnMinutes() {

        LocalDateTime start = LocalDateTime.now(clock);
        LocalDateTime end = LocalDateTime.now(clock).plusMinutes(30);

        updateDTO.setNewStartDateTime(start);
        updateDTO.setNewEndDateTime(end);

        Appointment result = mapAppointment();

        assertEquals(start.truncatedTo(ChronoUnit.MINUTES), result.getStartDateTime());
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar la fecha y hora de finalización, se truncará en minutos")
    void givenEndDateTimeWithRemainingSeconds_WhenUpdating_ThenIsTruncatedOnMinutes() {

        LocalDateTime start = LocalDateTime.now(clock);
        LocalDateTime end = LocalDateTime.now(clock).plusMinutes(30);

        updateDTO.setNewStartDateTime(start);
        updateDTO.setNewEndDateTime(end);

        Appointment result = mapAppointment();

        assertEquals(end.truncatedTo(ChronoUnit.MINUTES), result.getEndDateTime());
    }

    @Test
    @DisplayName("Dado un turno existente, al intentar actualizar con un ID de empleado NULL, se preservará el empleado actual")
    void givenNullEmployeeID_WhenUpdating_ThenEntityKeepsCurrentEmployee() {

        updateDTO.setNewEmployeeID(null);

        Appointment result = mapAppointmentWithNullEmployee();

        assertEquals(existingAppointment.getEmployee(), result.getEmployee());
    }

    @Test
    @DisplayName("Dado un turno existente, al intentar actualizar con un ID de servicio NULL, se preservará el servicio actual")
    void givenNullBarberServiceID_WhenUpdating_ThenEntityKeepsCurrentService() {

        updateDTO.setNewBarberserviceID(null);

        Appointment result = mapAppointmentWithNullBarberservice();

        assertEquals(existingAppointment.getBarberservice(), result.getBarberservice());
    }

    @Test
    @DisplayName("Dado un turno existente, al intentar actualizar con una fecha y hora de inicio NULL, se preservará la fecha y hora de inicio actual")
    void givenNullNewStartDateTime_WhenUpdating_ThenEntityKeepsCurrentStartDateTime() {

        updateDTO.setNewStartDateTime(null);

        Appointment result = mapAppointment();

        assertEquals(existingAppointment.getStartDateTime(), result.getStartDateTime());
    }

    @Test
    @DisplayName("Dado un turno existente, al intentar actualizar con una fecha y hora de fin NULL, se preservará la fecha y hora de fin actual")
    void givenNullNewEndDateTime_WhenUpdating_ThenEntityKeepsCurrentEndDateTime() {

        updateDTO.setNewEndDateTime(null);

        Appointment result = mapAppointment();

        assertEquals(existingAppointment.getEndDateTime(), result.getEndDateTime());
    }

    @Test
    @DisplayName("Dado un turno existente, al intentar actualizar con un estado de turno NULL, se preservará el estado de turno actual")
    void givenNullNewStatus_WhenUpdating_ThenEntityKeepsCurrentStatus() {

        updateDTO.setNewStatus(null);

        Appointment result = mapAppointment();

        assertEquals(existingAppointment.getCurrentStatus(), result.getCurrentStatus());
    }

    private Appointment mapAppointment() {

        return mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, existingAppointment.getEmployee(), existingAppointment.getBarberservice(), existingAppointment);
    }

    private Appointment mapAppointmentWithNullEmployee() {

        return mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, null, existingAppointment.getBarberservice(), existingAppointment);
    }

    private Appointment mapAppointmentWithNullBarberservice() {

        return mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, existingAppointment.getEmployee(), null, existingAppointment);
    }
}
