package com.factory;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.enums.AppointmentStatus;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;

import java.time.LocalDateTime;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.test_constant.AppointmentTestConstants.CreationValidData.*;
import static com.test_constant.AppointmentTestConstants.UpdateValidData.*;

public class AppointmentTestDataFactory {

    private AppointmentTestDataFactory() {
    }

    public static AppointmentCreationDTO buildValidAppointmentCreationDTO() {

        Client client = buildValidClient();
        BarberService barberService = buildValidBarberService();
        Employee employee = buildValidEmployee();

        return AppointmentCreationDTO.builder()
                .clientID(client.getClientID())
                .employeeID(employee.getEmployeeID())
                .barberserviceID(barberService.getBarbershopServiceID())
                .startDateTime(START_DATETIME)
                .endDateTime(END_DATETIME)
                .optionalNotes(APPOINTMENT_OPTIONAL_NOTES)
                .build();
    }

    public static AppointmentUpdateDTO buildValidAppointmentUpdateDTO() {

        return AppointmentUpdateDTO.builder()
                .newEmployeeID(NEW_EMPLOYEE_ID)
                .newBarberserviceID(NEW_BARBER_SERVICE_ID)
                .newStartDateTime(NEW_START_DATETIME)
                .newEndDateTime(NEW_END_DATETIME)
                .newStatus(NEW_STATUS)
                .optionalNotes(APPOINTMENT_OPTIONAL_NOTES)
                .build();
    }

    public static Appointment buildValidAppointment() {

        Client client = buildValidClient();
        BarberService barberService = buildValidBarberService();
        Employee employee = buildValidEmployee();

        return Appointment.builder()
                .appointmentID(1L)
                .client(client)
                .barberservice(barberService)
                .employee(employee)
                .startDateTime(LocalDateTime.of(2026, 1, 1, 17, 0))
                .endDateTime(LocalDateTime.of(2026, 1, 1, 17, 30))
                .currentStatus(AppointmentStatus.PROGRAMADO)
                .build();
    }
}
